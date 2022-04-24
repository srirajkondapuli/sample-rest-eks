package com.myown.app.sample.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.annotation.PostConstruct;
import lombok.CustomLog;

//@Slf4j
@CustomLog
public class DefaultCredentialProvider  implements CredentialProvider{


    public DefaultCredentialProvider() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// dd/MM/yyyy
        String strDate = sdfDate.format(Calendar.getInstance().getTime());
        log.info().message("Creating DefaultCredentialProvider Bean At : %s", strDate).log();
    }
    @Override
    public String getCredential(String id) {

        log.info().message("Returning Dummy Secret!!").log();
        return "dummySecret";
    }
    @PostConstruct
    public void refresh() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// dd/MM/yyyy
        String strDate = sdfDate.format(Calendar.getInstance().getTime());
        log.info().message("Finalizing DefaultCredentialProvider Bean At : %", strDate).log();
    }

}
