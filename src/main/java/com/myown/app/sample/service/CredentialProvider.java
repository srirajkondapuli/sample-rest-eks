package com.myown.app.sample.service;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import io.opentelemetry.extension.annotations.WithSpan;

@RefreshScope
public interface CredentialProvider {

    @WithSpan
    public String getCredential(String id);
}
