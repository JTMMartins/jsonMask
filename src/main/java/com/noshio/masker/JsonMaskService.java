package com.noshio.masker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.noshio.api.JsonMask;
import com.noshio.api.JsonMaskResponse;
import com.noshio.api.JsonParseStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class JsonMaskService {

    private static final Logger LOG = LogManager.getLogger(JsonMaskService.class);
    private static final ObjectMapper mapper = new ObjectMapper();


    public static JsonMaskResponse mask(
            final String jsonMessage,
            final List<JsonMask> jsonMasks
    ) throws JsonProcessingException {

        if (!isValid(jsonMessage, jsonMasks)) {
            return new JsonMaskResponse(
                    JsonParseStatus.UNPARSED,
                    jsonMessage,
                    "Either jsonMessage or JsonMask have null Values"
            );
        }
        JsonNode originalNode;
        try {
            originalNode = mapper.readTree(jsonMessage);
        } catch (IOException ex) {
            LOG.error(ex.getMessage());
            return new JsonMaskResponse(JsonParseStatus.UNPARSED, jsonMessage, ex.getMessage());
        }
        final MaskCombiner maskCombiner = new MaskCombiner();
        final Map<String, List<MaskingElement>> masksDictionary = maskCombiner.combine(jsonMasks);
        nodeLookup(originalNode, masksDictionary);
        return buildJsonParseResponse(originalNode);
    }

    private static JsonMaskResponse buildJsonParseResponse(final JsonNode jsonNode) throws JsonProcessingException {
        return new JsonMaskResponse(
                JsonParseStatus.PARSED,
                mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode), null);
    }

    private static void nodeLookup(final JsonNode node, final Map<String, List<MaskingElement>> masksDictionary) {
        for (Map.Entry<String, List<MaskingElement>> dictionaryEntry : masksDictionary.entrySet()) {
            final List<MaskingElement> jsonMasks = dictionaryEntry.getValue();
            final String[] paths = dictionaryEntry.getKey().split(Constants.PATH_DIVIDER, 5);
            JsonNode currentNode = node;
            if (paths.length >= 1 && !"".equals(paths[0])) {
                for (String path : paths) {
                    currentNode = currentNode.get(path);
                }
            }
            processCurrentNode(currentNode, jsonMasks);
        }
    }


    private static void processCurrentNode(final JsonNode jsonNode, final List<MaskingElement> jsonMasks) {
        final JsonNodeType nodeType = jsonNode.getNodeType();
        switch (nodeType) {
            case ARRAY:
                jsonMasks.forEach(mask -> maskArray(jsonNode, mask));
                break;
            case OBJECT:
                jsonMasks.forEach(mask -> maskObject(jsonNode, mask));
                break;
            case NUMBER:
            case STRING:
            case POJO:
            case BINARY:
            case BOOLEAN:
            case MISSING:
            case NULL:
                throw new UnsupportedOperationException("Trying to mask a not yet implemented case");
        }
    }

    private static boolean isValid(final String jsonMessage, final List<JsonMask> jsonMasks) {
        return jsonMessage != null && jsonMasks != null;
    }

    private static ArrayNode maskArray(final JsonNode jsonNode, final MaskingElement jsonMask) {

        final ArrayNode arrayNode = (ArrayNode) jsonNode;

        for (int i = 0; i < arrayNode.size(); i++) {
            final JsonNode currentNode = arrayNode.get(i);
            arrayNode.remove(i);
            arrayNode.insert(i, maskObject(currentNode, jsonMask));
        }
        return arrayNode;
    }

    private static JsonNode maskObject(final JsonNode jsonNode, final MaskingElement jsonMask) {
        return ((ObjectNode) jsonNode).put(jsonMask.getJsonKey(), jsonMask.getMask());
    }
}


