package com.myown.app.sample.service;

import org.springframework.cloud.context.config.annotation.RefreshScope;

@RefreshScope
public interface CredentialProvider {

    public String getCredential(String id);
}
