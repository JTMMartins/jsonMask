package com.noshio.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JsonMask {
    private static final String DEFAULT_MASK = "********";
    private String jsonPath;
    private String jsonKey;
    private String mask;

    public JsonMask(final String jsonPath, final String jsonKey) {
        this.jsonPath = jsonPath;
        this.jsonKey = jsonKey;
    }

    public JsonMask(final String jsonPath, final String jsonKey, final String mask) {
        this.jsonPath = jsonPath;
        this.jsonKey = jsonKey;
        this.mask = mask;
    }

    public String getMask() {
        return mask == null ? DEFAULT_MASK : mask;
    }
}
