
package com.myown.app.sample.api;

import com.myown.app.sample.component.Logger;
import com.myown.app.sample.component.LoggerFactory;
import com.myown.app.sample.service.CredentialProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.extension.annotations.WithSpan;
import lombok.CustomLog;

@RestController
@RequestMapping("/api/credential")
//@Slf4j
@CustomLog
public class CredentialController {
    private final CredentialProvider credentialProvider;
    public static Logger logger = LoggerFactory.getLogger(CredentialController.class);

    public CredentialController(CredentialProvider credentialProvider) {

        this.credentialProvider = credentialProvider;
    }

    @Value("${application.encryption.url}")
    String encryptionUrl;

    @GetMapping("/{id}")
    @WithSpan
    public String index(@PathVariable String id) {
        String credential = "";
        try {
            Span currentSpan = Span.current();
            //MDC.put("span_id", currentSpan.getSpanContext().getSpanId());
            //MDC.put("trace_id", currentSpan.getSpanContext().getSpanId());

            log.info().message("Get Credential for id = {%s}", id).log();;
            logger.info().message("Get Credential for id = %s", id);

            credential = credentialProvider.getCredential(id);
            log.info().message("Response Credential for is: {%s}", credential);
            logger.info().message("Response Credential for is = %s",credential);
            doSomeWorkNewSpan();
        } finally {
            //MDC.remove("trace_id");
            //MDC.remove("span_id");
        }
        return credential;

    }

    @GetMapping("/encrypted/{id}")
    @WithSpan

    public String encryptedCredential(@PathVariable String id) {

        String encryptedCredential = id;
        try {

            log.info().message("Get Encrypted Credential for id = %s", id).log();;
            logger.info().message("Get Encrypted Credential for id = 23456").log();
            String credential = credentialProvider.getCredential(id);
            Span currentSpan = Span.current();
            //MDC.put("span_id", currentSpan.getSpanContext().getSpanId());
            //MDC.put("trace_id", currentSpan.getSpanContext().getSpanId());


            log.info().message("Encryption URL : %s" , encryptionUrl).log();;
            logger.info().message("Encryption URL : = %s", encryptionUrl).log();
            WebClient webClient = WebClient.create(encryptionUrl);

            encryptedCredential = webClient.get().uri("/api/encryption/enc/" + credential)
                    .retrieve().bodyToMono(String.class).block();
        } finally {
            //MDC.remove("trace_id");
           // MDC.remove("span_id");
        }


        return encryptedCredential;
    }

    @WithSpan
    private void doSomeWorkNewSpan() {
        log.info().message("Doing some work In New span").log();;
        Span span = Span.current();

        span.setAttribute("attribute.a2", "some value");

        span.addEvent("app.processing2.start", atttributes("321"));
        span.addEvent("app.processing2.end", atttributes("321"));
    }

    private Attributes atttributes(String id) {
        return Attributes.of(AttributeKey.stringKey("app.id"), id);
    }

}

