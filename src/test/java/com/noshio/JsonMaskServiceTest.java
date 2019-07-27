package com.noshio;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.noshio.api.JsonMask;
import com.noshio.api.JsonMaskResponse;
import com.noshio.masker.JsonMaskService;
import com.noshio.api.JsonParseStatus;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonMaskServiceTest {

    private static final String JSON_MESSAGE = "{\n" +
            "  \"httpStatusCode\": 200,\n" +
            "  \"resultSet\": {\n" +
            "    \"result\": {\n" +
            "      \"totalSize\": 1,\n" +
            "      \"records\": [\n" +
            "        {\n" +
            "          \"attributes\": {\n" +
            "            \"type\": \"Asset\",\n" +
            "            \"url\": \"http://google.com\"\n" +
            "          },\n" +
            "          \"Name\": \"Some test name\",\n" +
            "          \"type\": \"Animal\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"done\": true\n" +
            "    }\n" +
            "  },\n" +
            "  \"messages\": [],\n" +
            "  \"startedTime\": 1441969739375,\n" +
            "  \"endTime\": 1441969750317\n" +
            "}";

    @Test
    public void parseRootLevelWithOneKey() throws JsonProcessingException {

        final JsonMask jsonMask = new JsonMask("", "startedTime");
        final List<JsonMask> jsonMasks = new ArrayList<>(Arrays.asList(jsonMask));

        final JsonMaskResponse result = JsonMaskService.mask(JSON_MESSAGE, jsonMasks);

        Assert.assertEquals(JsonParseStatus.PARSED, result.getJsonParseStatus());
        Assert.assertEquals(null, result.getErrorCause());
        Assert.assertFalse(result.getJsonMessage().contains("1441969739375"));
    }

    @Test
    public void parseRootLevelWithOneKeyAndCustomMask() throws JsonProcessingException {
        final List<JsonMask> jsonMasks = new ArrayList<>(Arrays.asList(
                new JsonMask("", "startedTime", "####")));

        final JsonMaskResponse result = JsonMaskService.mask(JSON_MESSAGE, jsonMasks);

        Assert.assertEquals(JsonParseStatus.PARSED, result.getJsonParseStatus());
        Assert.assertEquals(null, result.getErrorCause());
        Assert.assertFalse(result.getJsonMessage().contains("1441969739375"));
        Assert.assertTrue(result.getJsonMessage().contains("####"));
    }

    @Test
    public void parseRootLevelWithTwoKeys() throws JsonProcessingException {
        final List<JsonMask> jsonMasks = new ArrayList<>(Arrays.asList(
                new JsonMask("", "startedTime"),
                new JsonMask("", "resultSet")));

        final JsonMaskResponse result = JsonMaskService.mask(JSON_MESSAGE, jsonMasks);

        Assert.assertEquals(JsonParseStatus.PARSED, result.getJsonParseStatus());
        Assert.assertEquals(null, result.getErrorCause());
        Assert.assertFalse(result.getJsonMessage().contains("1441969739375"));
        Assert.assertFalse(result.getJsonMessage().contains("records"));
    }

    @Test
    public void parseOneLevelDeep() throws JsonProcessingException {
        final List<JsonMask> jsonMasks = new ArrayList<>(Arrays.asList(
                new JsonMask("resultSet", "result")));

        final JsonMaskResponse result = JsonMaskService.mask(JSON_MESSAGE, jsonMasks);

        Assert.assertEquals(JsonParseStatus.PARSED, result.getJsonParseStatus());

        Assert.assertEquals(null, result.getErrorCause());
        Assert.assertFalse(result.getJsonMessage().contains("totalSize"));
        Assert.assertTrue(result.getJsonMessage().contains("messages"));
    }

    @Test
    public void parseTwoLevelsDeep() throws JsonProcessingException {
        final List<JsonMask> jsonMasks = new ArrayList<>(Arrays.asList(
                new JsonMask("resultSet/result", "totalSize"),
                new JsonMask("resultSet/result", "records")));

        final JsonMaskResponse result = JsonMaskService.mask(JSON_MESSAGE, jsonMasks);

        Assert.assertEquals(JsonParseStatus.PARSED, result.getJsonParseStatus());
        Assert.assertEquals(null, result.getErrorCause());
        Assert.assertFalse(result.getJsonMessage().contains("\"totalSize\":\"\"********\""));
        Assert.assertFalse(result.getJsonMessage().contains("attributes"));
        Assert.assertTrue(result.getJsonMessage().contains("endTime"));
    }

    @Test
    public void parseWithNullMaskListReturnsUnparsedStatusResponse() throws JsonProcessingException {

        final JsonMaskResponse result = JsonMaskService.mask(JSON_MESSAGE, null);

        Assert.assertEquals(JsonParseStatus.UNPARSED, result.getJsonParseStatus());
        Assert.assertEquals(JSON_MESSAGE, result.getJsonMessage());
    }
}