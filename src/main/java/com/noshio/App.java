package com.noshio;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.noshio.api.JsonMask;
import com.noshio.api.JsonMaskResponse;
import com.noshio.masker.JsonMaskService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Simple main application
 * just to get an idea of processing speed.
 */
public class App {
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

    public static void main(String[] args) throws JsonProcessingException {
        //warm up JVM
        final JsonMask jsonMask = new JsonMask("", "startedTime");
        final List<JsonMask> jsonMasks = new ArrayList<>(Arrays.asList(jsonMask));
        long startTime0 = System.currentTimeMillis();
        for (int i = 0; i < 5000; i++) {
            final JsonMaskResponse result = JsonMaskService.mask(JSON_MESSAGE, jsonMasks);
        }
        final long stopTime0 = System.currentTimeMillis();
        final long elapsedTime0 = stopTime0 - startTime0;
        System.out.println("WARM UP JVM -> Masking 5000 times a root element took  " + elapsedTime0 + "milisecods");
        // now let's go
        for (int x = 0; x < 20; x++) {
            System.out.println("RUN NUMBER :" + (x + 1));

            // test 1
            final long startTime1 = System.currentTimeMillis();
            final List<JsonMask> jsonMasks1 = new ArrayList<>(Arrays.asList(
                    new JsonMask("resultSet/result", "totalSize"),
                    new JsonMask("resultSet/result", "records")));
            for (int i = 0; i < 5000; i++) {
                final JsonMaskResponse result = JsonMaskService.mask(JSON_MESSAGE, jsonMasks1);
            }
            final long stopTime1 = System.currentTimeMillis();
            final long elapsedTime1 = stopTime1 - startTime1;
            System.out.println("Test 1 -> Masking 5000 times 2 keys on second level element took -> " + elapsedTime1 + "milisecods");

            // test 2

            final List<JsonMask> jsonMasks2 = new ArrayList<>(Arrays.asList(new JsonMask("", "endTime")));
            final long startTime2 = System.currentTimeMillis();
            for (int i = 0; i < 5000; i++) {
                final JsonMaskResponse result = JsonMaskService.mask(JSON_MESSAGE, jsonMasks2);
            }
            final long stopTime2 = System.currentTimeMillis();
            final long elapsedTime2 = stopTime2 - startTime2;
            System.out.println("Test 2 -> Masking 5000 times one root element took -> " + elapsedTime2 + "milisecods");
        }
    }
}
