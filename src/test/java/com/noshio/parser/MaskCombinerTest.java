package com.noshio.parser;

import com.noshio.api.JsonMask;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MaskCombinerTest {

    @Test(expected = IllegalArgumentException.class)
    public void combineWithNullParameter() {
        MaskCombiner maskCombiner = new MaskCombiner();
        Map<String, List<MaskingElement>> result = maskCombiner.combine(null);
    }

    @Test
    public void combineWithOneKeyAndDefaultMask() {
        JsonMask jsonMask1 = new JsonMask("path1/path2", "key1");
        JsonMask jsonMask2 = new JsonMask("path1/path2", "key2");
        JsonMask jsonMask3 = new JsonMask("path1/path2", "key3");
        List<JsonMask> jsonMasks = Arrays.asList(jsonMask1, jsonMask2, jsonMask3);
        MaskCombiner maskCombiner = new MaskCombiner();

        Map<String, List<MaskingElement>> result = maskCombiner.combine(jsonMasks);

        Assert.assertTrue(result.size() == 1);
        Assert.assertTrue(result.get("path1/path2").size() == 3);
    }

    @Test
    public void combineWithOneKeyAndCustomMasks() {
        JsonMask jsonMask1 = new JsonMask("path1/path2", "key1", "mask1");
        JsonMask jsonMask2 = new JsonMask("path1/path2", "key2", "mask2");
        JsonMask jsonMask3 = new JsonMask("path1/path2", "key3", "mask3");
        List<JsonMask> jsonMasks = Arrays.asList(jsonMask1, jsonMask2, jsonMask3);
        MaskCombiner maskCombiner = new MaskCombiner();

        Map<String, List<MaskingElement>> result = maskCombiner.combine(jsonMasks);

        Assert.assertTrue(result.size() == 1);
        Assert.assertTrue(result.get("path1/path2").size() == 3);

        List<MaskingElement> maskingElementsResult = result.get("path1/path2");

        Assert.assertTrue(maskingElementsResult.get(0).getMask() == "mask1");
        Assert.assertTrue(maskingElementsResult.get(1).getMask() == "mask2");
        Assert.assertTrue(maskingElementsResult.get(2).getMask() == "mask3");
    }

    @Test
    public void combineWithMultipleKeysAndAMixOfCustomAndDefaultMask() {
        JsonMask jsonMask1 = new JsonMask("path1/path2", "key1", "mask1");
        JsonMask jsonMask2 = new JsonMask("path1/path2", "key2", "mask2");
        JsonMask jsonMask3 = new JsonMask("path1/path2", "key3", "mask3");
        JsonMask jsonMask4 = new JsonMask("path1", "key4");
        JsonMask jsonMask5 = new JsonMask("path1/path3", "key5", "mask5");
        List<JsonMask> jsonMasks = Arrays.asList(jsonMask1, jsonMask2, jsonMask3, jsonMask4, jsonMask5);
        MaskCombiner maskCombiner = new MaskCombiner();

        Map<String, List<MaskingElement>> result = maskCombiner.combine(jsonMasks);

        Assert.assertEquals("Result Map with wrong size", 3, result.size());

        List<MaskingElement> maskingElementsResult1 = result.get("path1/path2");
        List<MaskingElement> maskingElementsResult2 = result.get("path1");
        List<MaskingElement> maskingElementsResult3 = result.get("path1/path3");

        Assert.assertEquals("result 1 with wrong size", 3, maskingElementsResult1.size());
        Assert.assertEquals("result 2 with wrong size", 1, maskingElementsResult2.size());
        Assert.assertEquals("result 3 with wrong size", 1, maskingElementsResult3.size());
    }
}