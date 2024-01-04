
package com.myown.app.sample.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myown.app.sample.service.EncryptDecryptProvider;

@RestController
@RequestMapping("/api/encryption")
// @Slf4j
// @CustomLog
public class EncryptionController {

    private static final Logger log = LoggerFactory.getLogger(EncryptionController.class);
    @Autowired
    EncryptDecryptProvider provider;

    @GetMapping(path = "/enc/{id}")

    public String encrypt(@PathVariable String id) {
        log.info("Encrypt Value for id = {}", id);

        String encString = "";
        try {
            encString = provider.encrypt(id);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        log.info("Encrypted Value for id = {}", encString);
        return encString;
    }

    @PostMapping(path = "/decrypt", produces = { "text/plain" }, consumes = { "text/plain", "application/json",
            "application/xml" })

    public String deCrypt(@RequestBody String id) {
        log.info("DeCrypt Value for id = {}", id);
        ;

        String decString = "";
        try {
            decString = provider.decrypt(id);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        log.info("Encrypted Value for id = {}", decString);
        return decString;
    }

}
