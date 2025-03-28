package com.jcondotta.bank_account_transfers.infrastructure.config;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

public class TestRestClient {

    private int connectTimeout = 1000;
    private int readTimeout = 1000;

    private TestRestClient() {
    }

    public static TestRestClient builder() {
        return new TestRestClient();
    }

    public TestRestClient connectTimeout(int timeout) {
        this.connectTimeout = timeout;
        return this;
    }

    public TestRestClient readTimeout(int timeout) {
        this.readTimeout = timeout;
        return this;
    }

    public RestClient build() {
        return RestClient.builder()
                .requestFactory(new SimpleClientHttpRequestFactory() {{
                    setConnectTimeout(connectTimeout);
                    setReadTimeout(readTimeout);
                }})
                .build();
    }
}
