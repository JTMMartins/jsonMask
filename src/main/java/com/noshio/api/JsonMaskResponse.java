package com.noshio.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JsonMaskResponse {

    private JsonParseStatus jsonParseStatus;
    private String jsonMessage;
    private String errorCause;
}
