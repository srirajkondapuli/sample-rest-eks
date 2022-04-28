package com.myown.app.sample.component;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.function.Consumer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.slf4j.Marker;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class HttpAppender extends HttpAppenderAbstract {

    /**
     * Defines default method to send data.
     */
    protected final static String DEFAULT_METHOD = "POST";

    protected String method = "POST";

    @Override
    public void start() {
        normalizeMethodName();

        super.start();
    }

    protected void checkProperties() {
        if (isStringEmptyOrNull(url)) {
            url = DEFAULT_URL;
            addInfo(String.format(MSG_NOT_SET, "url", url));
        } else {
            addInfo(String.format(MSG_USING, "url", url));
        }

        if (isStringEmptyOrNull(method)) {
            method = DEFAULT_METHOD;
            addInfo(String.format(MSG_NOT_SET, "method", method));
        } else {
            addInfo(String.format(MSG_USING, "method", method));
        }
    }

    @Override
    public void append(ILoggingEvent event) {
        createIssue(event);
    }

    public void createIssue(ILoggingEvent event) {
        HttpURLConnection conn = null;

        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        try {



            System.out.println("Body : " + body);
            byte[] data = encoder.encode(event);
            String strData = new String(data, StandardCharsets.UTF_8);

            System.out.println("Encoded Data : " + strData);
            JsonObject convertedObject = gson.fromJson(strData, JsonObject.class);
            String service = convertedObject.get("service").getAsString();
            String host = convertedObject.get("host").getAsString();
            String source = convertedObject.get("source").getAsString();
            String pid = convertedObject.get("pid").getAsString();
            System.out.println("Service: " + service);
            System.out.println("Service: " + host);
            System.out.println("Source: " + source);

            System.out.println("PID: " + pid);
            System.out.println("String Encoded Data : " + strData);
            String formattedMessage = event.getFormattedMessage();
            Map<String,String> mdcMap = event.getMDCPropertyMap();

            Map<String,String> propertyMap = event.getLoggerContextVO().getPropertyMap();
            Level level = event.getLevel();
            Object[] arguments = event.getArgumentArray();
            Marker marker = event.getMarker();
            Long timeStamp = event.getTimeStamp();
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timestamp = sdfDate.format(new Date(timeStamp));
            System.out.println("TIme Stamp: " + timestamp);

            System.out.println(formattedMessage+":"+mdcMap+":"+propertyMap+":"+arguments+":"+marker+":"+timeStamp);

            JsonObject jsonObject = new JsonObject();
            jsonObject.add("message", gson.toJsonTree(formattedMessage));
            jsonObject.add("@timestamp", gson.toJsonTree(timestamp));
            jsonObject.add("severity", gson.toJsonTree(level.levelStr));
            jsonObject.add("event", gson.toJsonTree(formattedMessage));
            jsonObject.add("host", gson.toJsonTree(host));
            jsonObject.add("service", gson.toJsonTree(service));
            jsonObject.add("ddsource", gson.toJsonTree(source));
            jsonObject.add("pid", gson.toJsonTree(pid));
            String traceId = mdcMap.get("trace_id");
            if(null!=traceId && traceId.length() > 0){
                jsonObject.add("dd.trace_id", gson.toJsonTree(traceId));
            }
            String spanId = mdcMap.get("span_id");
            if(null!=spanId && spanId.length() > 0){
                jsonObject.add("dd.span_id", gson.toJsonTree(spanId));
            }

            URL urlObj = new URL(url);
            addInfo("URL: " + url);
            conn = (HttpURLConnection) urlObj.openConnection();
            conn.setRequestMethod(method);
            transformHeaders(conn);
            boolean isOk = false;

            HttpHeaders httpHeaders = new HttpHeaders();
            JSONObject jObj = new JSONObject(headers);
            for (String key : jObj.keySet()) {
                String value = (String) jObj.get(key);
                httpHeaders.add(key.toUpperCase(), value);

            }


            Consumer<HttpHeaders> consumer = it -> it.addAll(httpHeaders);
            WebClient webClient = WebClient.builder().defaultHeaders(hHeaders -> {
                hHeaders.setContentType(MediaType.APPLICATION_JSON);
                hHeaders.addAll(httpHeaders);
            }).build();




            String payload = gson.toJson(jsonObject);
            System.out.println("Logging URL : " + url);
            String response = webClient.post()
                //.uri("http://localhost:8000/logs")
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(payload))
                .retrieve()
                .bodyToMono(String.class)
                .block();

            isOk = true;
            // byte[] objEncoded = encoder.encode(event);
            // if (method.equals("GET") || method.equals("DELETE")) {
            //     isOk = sendNoBodyRequest(conn);
            // } else if (method.equals("POST") || method.equals("PUT")) {
            //     isOk = sendBodyRequest(objEncoded, conn);
            // }

            if (!isOk) {
                addError("Not OK");
                return;
            }
        } catch (Exception e) {
            addError("Exception", e);
            return;
        } finally {
            try {
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (Exception e) {
                addError("Exception", e);
                return;
            }
        }
    }

    private void normalizeMethodName() {
        method = method.toUpperCase();
    }

    protected void transformHeaders(HttpURLConnection conn) {
        conn.setRequestProperty("Content-Type", contentType);
        if (headers == null || headers.isEmpty()) {
            return;
        }

        JSONObject jObj = new JSONObject(headers);
        for (String key : jObj.keySet()) {
            String value = (String) jObj.get(key);
            conn.setRequestProperty(key, value);
        }

    }

    protected boolean sendNoBodyRequest(HttpURLConnection conn) throws IOException {
        return showResponse(conn);
    }

    protected boolean sendBodyRequest(byte[] objEncoded, HttpURLConnection conn)
            throws IOException {
        conn.setDoOutput(true);
        if (body != null) {
            addInfo("Body: " + body);
            IOUtils.write(body, conn.getOutputStream(), Charset.defaultCharset());
        } else {
            IOUtils.write(objEncoded, conn.getOutputStream());
        }
        return showResponse(conn);
    }
}
