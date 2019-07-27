package com.noshio.masker;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
final class MaskingElement {
    private static final String DEFAULT_MASK = "********";
    private String jsonKey;
    private String mask;

    public MaskingElement(final String jsonKey) {
        this.jsonKey = jsonKey;
    }

    public MaskingElement(final String jsonKey, final String mask) {
        this.jsonKey = jsonKey;
        this.mask = mask;
    }

    public String getMask() {
        return mask == null ? DEFAULT_MASK : mask;
    }


}
