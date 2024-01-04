
package com.myown.app.sample.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.myown.app.sample.service.CredentialProvider;

@RestController
@RequestMapping("/api/credential")
// @Slf4j
// @CustomLog
public class CredentialController {
    private final CredentialProvider credentialProvider;
    public static Logger log =
    LoggerFactory.getLogger(CredentialController.class);

    public CredentialController(CredentialProvider credentialProvider) {

        this.credentialProvider = credentialProvider;
    }

    @Value("${application.encryption.url}")
    String encryptionUrl;

    @GetMapping("/{id}")
    public String index(@PathVariable String id) {
        String credential = "";

        // Log something

        credential = credentialProvider.getCredential(id);
        log.info("Response Credential for is: {}", credential);

        return credential;

    }

    @GetMapping("/encrypted/{id}")
    public String encryptedCredential(@PathVariable String id) {

        String encryptedCredential = id;
        try {

            String credential = credentialProvider.getCredential(id);

            log.info("Encryption URL : {}", encryptionUrl);
            ;

            WebClient webClient = WebClient.create(encryptionUrl);

            encryptedCredential = webClient.get().uri("/api/encryption/enc/" + credential)
                    .retrieve().bodyToMono(String.class).block();
        } finally {

        }

        return encryptedCredential;
    }

}
