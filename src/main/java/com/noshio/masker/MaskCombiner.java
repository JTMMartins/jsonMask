package com.noshio.masker;

import com.noshio.api.JsonMask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Combines masks with same path, to make iterations smaller
 * when processing masks.
 */
final class MaskCombiner {

    public Map<String, List<MaskingElement>> combine(final List<JsonMask> jsonMasks) {
        if (jsonMasks == null) {
            throw new IllegalArgumentException("JsonMasks can not be null");
        }

        final Map<String, List<MaskingElement>> combinedMasks = new HashMap<>();
        final List<MaskingElement> maskingElements = new ArrayList<>();
        //add first element
        maskingElements.add(new MaskingElement(jsonMasks.get(0).getJsonKey(), jsonMasks.get(0).getMask()));
        combinedMasks.put(jsonMasks.get(0).getJsonPath(), new ArrayList<>(maskingElements));
        // if only one...we are done here.
        if (jsonMasks.size() == 1) {
            return combinedMasks;
        }
        // combine elements with equal path.
        for (int i = 1; i < jsonMasks.size(); i++) {
            maskingElements.clear();
            List<MaskingElement> pathDefinedKeys = null;
            if (combinedMasks.containsKey(jsonMasks.get(i).getJsonPath())) {
                pathDefinedKeys = combinedMasks.get(jsonMasks.get(i).getJsonPath());
                pathDefinedKeys.add(new MaskingElement(jsonMasks.get(i).getJsonKey(), jsonMasks.get(i).getMask()));
            } else {
                pathDefinedKeys = new ArrayList<>();
                pathDefinedKeys.add(new MaskingElement(jsonMasks.get(i).getJsonKey(), jsonMasks.get(i).getMask()));
                combinedMasks.put(jsonMasks.get(i).getJsonPath(), pathDefinedKeys);
            }
        }
        return combinedMasks;
    }
}
