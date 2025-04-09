package com.juaracoding.handler;

import feign.Response;
import feign.codec.ErrorDecoder;
import feign.FeignException;
import feign.Util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CustomFeignErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            String body = Util.toString(response.body().asReader(StandardCharsets.UTF_8));
            return FeignException.errorStatus(methodKey, response.toBuilder().body(body, StandardCharsets.UTF_8).build());
        } catch (IOException e) {
            return defaultDecoder.decode(methodKey, response);
        }
    }
}
