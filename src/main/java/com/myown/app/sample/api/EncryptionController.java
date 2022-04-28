
package com.myown.app.sample.api;
import com.myown.app.sample.service.EncryptDecryptProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.CustomLog;
@RestController
@RequestMapping("/api/encryption")
//@Slf4j
@CustomLog
public class EncryptionController {


    @Autowired
    EncryptDecryptProvider provider;

    @GetMapping(path="/enc/{id}")
    public String encrypt(@PathVariable String id) {
        log.info().message("Encrypt Value for id = %s", id).log();;

        String encString="";
        try {
            encString = provider.encrypt(id);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        log.info().message("Encrypted Value for id = %s", encString).log();;
        return encString;
    }


    @PostMapping(path = "/decrypt",produces = { "text/plain" }, consumes = { "text/plain","application/json", "application/xml" })
    public String deCrypt(@RequestBody String id) {
        log.info().message("DeCrypt Value for id = %s", id).log();;

        String decString="";
        try {
            decString = provider.decrypt(id);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        log.info().message("Encrypted Value for id = %s", decString).log();;
        return decString;
    }


}

