package com.noshio;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noshio.api.JsonMask;
import com.noshio.api.JsonMaskResponse;
import com.noshio.masker.JsonMaskService;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonMaskServiceArrayTest {

    private static final String JSON_MESSAGE = "{\n" +
            "  \"data\": [\n" +
            "    {\n" +
            "      \"_id\": \"5d3a34bc864b6e86d7f36635\",\n" +
            "      \"index\": 0,\n" +
            "      \"guid\": \"c3f11967-dea6-4609-8d80-7c354235355a\",\n" +
            "      \"isActive\": true,\n" +
            "      \"balance\": \"$3,082.04\",\n" +
            "      \"picture\": \"http://placehold.it/32x32\",\n" +
            "      \"age\": 31,\n" +
            "      \"eyeColor\": \"green\",\n" +
            "      \"name\": \"Luella Hinton\",\n" +
            "      \"gender\": \"female\",\n" +
            "      \"company\": \"PASTURIA\",\n" +
            "      \"email\": \"luellahinton@pasturia.com\",\n" +
            "      \"phone\": \"+1 (948) 572-2003\",\n" +
            "      \"address\": \"422 Hausman Street, Gambrills, Utah, 2804\",\n" +
            "      \"about\": \"Adipisicing ex quis eiusmod aute et Lorem in mollit eu. Est veniam anim labore minim tempor excepteur ad veniam minim ea in. Elit cupidatat nisi qui ea reprehenderit tempor ullamco enim ad id. Dolore amet occaecat et anim labore elit occaecat. Consequat ex mollit aute ea sint exercitation occaecat et deserunt ut dolore culpa Lorem. Aliqua ullamco reprehenderit mollit excepteur anim.\\r\\n\",\n" +
            "      \"registered\": \"2018-02-19T04:11:52 -00:00\",\n" +
            "      \"latitude\": -3.288264,\n" +
            "      \"longitude\": 86.687213,\n" +
            "      \"tags\": [\n" +
            "        \"labore\",\n" +
            "        \"proident\",\n" +
            "        \"aute\",\n" +
            "        \"esse\",\n" +
            "        \"amet\",\n" +
            "        \"cillum\",\n" +
            "        \"voluptate\"\n" +
            "      ],\n" +
            "      \"friends\": [\n" +
            "        {\n" +
            "          \"id\": 0,\n" +
            "          \"name\": \"Mclaughlin Potter\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 1,\n" +
            "          \"name\": \"Alford Burgess\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 2,\n" +
            "          \"name\": \"Pearl Mcdonald\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"greeting\": \"Hello, Luella Hinton! You have 6 unread messages.\",\n" +
            "      \"favoriteFruit\": \"apple\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"_id\": \"5d3a34bc4eb4669f36d39e38\",\n" +
            "      \"index\": 1,\n" +
            "      \"guid\": \"be253fbc-d5cc-4b43-8e7a-b69d10817fa0\",\n" +
            "      \"isActive\": false,\n" +
            "      \"balance\": \"$2,203.51\",\n" +
            "      \"picture\": \"http://placehold.it/32x32\",\n" +
            "      \"age\": 32,\n" +
            "      \"eyeColor\": \"brown\",\n" +
            "      \"name\": \"Nellie Cohen\",\n" +
            "      \"gender\": \"female\",\n" +
            "      \"company\": \"ETERNIS\",\n" +
            "      \"email\": \"nelliecohen@eternis.com\",\n" +
            "      \"phone\": \"+1 (978) 487-2684\",\n" +
            "      \"address\": \"921 Dekoven Court, Worton, Kansas, 1962\",\n" +
            "      \"about\": \"Lorem amet aliquip labore sunt dolor duis duis excepteur. Esse adipisicing eiusmod consectetur veniam cupidatat reprehenderit consequat proident eu cillum. Elit proident nisi mollit id nulla velit et sunt aliqua excepteur excepteur. Laborum irure laboris nisi quis.\\r\\n\",\n" +
            "      \"registered\": \"2014-01-16T04:40:15 -00:00\",\n" +
            "      \"latitude\": -34.896257,\n" +
            "      \"longitude\": -95.958992,\n" +
            "      \"tags\": [\n" +
            "        \"commodo\",\n" +
            "        \"laborum\",\n" +
            "        \"anim\",\n" +
            "        \"veniam\",\n" +
            "        \"duis\",\n" +
            "        \"enim\",\n" +
            "        \"elit\"\n" +
            "      ],\n" +
            "      \"friends\": [\n" +
            "        {\n" +
            "          \"id\": 0,\n" +
            "          \"name\": \"Rosario Riggs\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 1,\n" +
            "          \"name\": \"Fowler Mcintosh\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 2,\n" +
            "          \"name\": \"Watts Greene\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"greeting\": \"Hello, Nellie Cohen! You have 2 unread messages.\",\n" +
            "      \"favoriteFruit\": \"strawberry\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    @Test
    public void maskJsonArrayMasksGivenKey() throws JsonProcessingException {
        final JsonMask jsonMask = new JsonMask("data", "balance");
        final List<JsonMask> jsonMasks = new ArrayList<>(Arrays.asList(jsonMask));

        final JsonMaskResponse result = JsonMaskService.mask(JSON_MESSAGE, jsonMasks);
        Assert.assertFalse(result.getJsonMessage().contains("$3,082.04"));
        Assert.assertFalse(result.getJsonMessage().contains("$2,203.51"));
    }

    /**
     * tags is a list inside the data array.
     * We want to completely mask the list values.
     *
     * @throws JsonProcessingException
     */
    @Test
    public void maskJsonListInsideJsonArray() throws IOException {
        final JsonMask jsonMask = new JsonMask("data", "tags");
        final List<JsonMask> jsonMasks = new ArrayList<>(Arrays.asList(jsonMask));
        final JsonMaskResponse result = JsonMaskService.mask(JSON_MESSAGE, jsonMasks);
        final ObjectMapper mapper = new ObjectMapper();
        final ArrayTestObject resultAsObject = mapper.readValue(result.getJsonMessage(), ArrayTestObject.class);
        final List<ArrayObjectData> resultData = resultAsObject.getData();

        resultData.forEach(arrayObjectData ->
                Assert.assertTrue(arrayObjectData.getTags().equals(jsonMask.getMask())));
    }
}
