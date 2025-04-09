package com.juaracoding.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResponseDTO {
    private String path;
    private String data;
    private boolean success;
    private String message;

    @JsonProperty("error-code")
    private String errorCode;

    private int status;
    private String timestamp;

}
