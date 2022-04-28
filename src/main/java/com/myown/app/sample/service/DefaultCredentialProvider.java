package com.myown.app.sample.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.annotation.PostConstruct;
import org.slf4j.MDC;
import io.opentelemetry.api.trace.Span;
import lombok.extern.slf4j.Slf4j;
@Slf4j

public class DefaultCredentialProvider  implements CredentialProvider{


    public DefaultCredentialProvider() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// dd/MM/yyyy
        String strDate = sdfDate.format(Calendar.getInstance().getTime());
        log.info("Creating DefaultCredentialProvider Bean At : {}", strDate);
    }
    @Override
    public String getCredential(String id) {



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

        log.info("Span Id: {}, Trace Id: {}",datadogSpanIdString, datadogTraceIdString);


        log.info("Returning Dummy Secret!!");



        return "dummySecret";
    }
    @PostConstruct
    public void refresh() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// dd/MM/yyyy
        String strDate = sdfDate.format(Calendar.getInstance().getTime());
        log.info("Finalizing DefaultCredentialProvider Bean At : {}", strDate);
    }

}
