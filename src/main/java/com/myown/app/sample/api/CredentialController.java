
package com.myown.app.sample.api;

import com.myown.app.sample.service.CredentialProvider;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;
import lombok.extern.slf4j.Slf4j;
@RestController
@RequestMapping("/api/credential")
@Slf4j
//@CustomLog
public class CredentialController {
    private final CredentialProvider credentialProvider;
    //public static Logger logger = LoggerFactory.getLogger(CredentialController.class);

    public CredentialController(CredentialProvider credentialProvider) {

        this.credentialProvider = credentialProvider;
    }

    @Value("${application.encryption.url}")
    String encryptionUrl;

    @GetMapping("/{id}")
    public String index(@PathVariable String id) {
        String credential = "";

        Span currentSpan = Span.current();
        String spanId = currentSpan.getSpanContext().getTraceId();
        String traceId = currentSpan.getSpanContext().getSpanId();

        String traceIdValue = Span.current().getSpanContext().getTraceId();
        String traceIdHexString = traceIdValue.substring(traceIdValue.length() - 16 );
        long datadogTraceId = Long.parseUnsignedLong(traceIdHexString, 16);
        String datadogTraceIdString = Long.toUnsignedString(datadogTraceId);

        String spanIdValue = Span.current().getSpanContext().getSpanId();
        String spanIdHexString = spanIdValue.substring(spanIdValue.length() - 16 );
        long datadogSpanId = Long.parseUnsignedLong(spanIdHexString, 16);
        String datadogSpanIdString = Long.toUnsignedString(datadogSpanId);

        MDC.put("trace_id", datadogTraceIdString);
        MDC.put("span_id",datadogSpanIdString);

        // Log something


        log.info("Original Span Id: {}, Original Trace Id: {}",spanId, traceIdValue);
        log.info("Span Id: {}, Trace Id: {}",datadogSpanIdString, datadogTraceIdString);
        log.info("Get Credential for id = {}", id);


        credential = credentialProvider.getCredential(id);
        log.info("Response Credential for is: {}", credential);

        doSomeWorkNewSpan();




        return credential;

    }

    @GetMapping("/encrypted/{id}")
    public String encryptedCredential(@PathVariable String id) {

        String encryptedCredential = id;
        try {

            String credential = credentialProvider.getCredential(id);


            log.info("Encryption URL : {}" , encryptionUrl);;

            WebClient webClient = WebClient.create(encryptionUrl);

            encryptedCredential = webClient.get().uri("/api/encryption/enc/" + credential)
                    .retrieve().bodyToMono(String.class).block();
        } finally {

        }


        return encryptedCredential;
    }


    private void doSomeWorkNewSpan() {
        log.info("Doing some work In New span");
        Span currentSpan = Span.current();
        String spanId = currentSpan.getSpanContext().getTraceId();
        String traceId = currentSpan.getSpanContext().getSpanId();
        log.info("Doing some work in doSomeWorkNewSpan");

        currentSpan.setAttribute("attribute.a2", "some value");

        currentSpan.addEvent("app.processing2.start", atttributes("321"));
        currentSpan.addEvent("app.processing2.end", atttributes("321"));
    }

    private Attributes atttributes(String id) {
        return Attributes.of(AttributeKey.stringKey("app.id"), id);
    }

}

