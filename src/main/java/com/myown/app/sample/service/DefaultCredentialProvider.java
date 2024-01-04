package com.myown.app.sample.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import lombok.extern.slf4j.Slf4j;

@Slf4j

public class DefaultCredentialProvider implements CredentialProvider {

    public DefaultCredentialProvider() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// dd/MM/yyyy
        String strDate = sdfDate.format(Calendar.getInstance().getTime());
        log.info("Creating DefaultCredentialProvider Bean At : {}", strDate);
    }

    @Override
    public String getCredential(String id) {

        log.info("Returning Dummy Secret!!");

        return "dummySecret";
    }

    @jakarta.annotation.PostConstruct
    public void refresh() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// dd/MM/yyyy
        String strDate = sdfDate.format(Calendar.getInstance().getTime());
        log.info("Finalizing DefaultCredentialProvider Bean At : {}", strDate);
    }

}
