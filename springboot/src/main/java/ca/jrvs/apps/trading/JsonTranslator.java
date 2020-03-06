package ca.jrvs.apps.trading;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;

public class JsonTranslator {
  public static String toJson(Object object, boolean prettyPrint, boolean includeNullValues)
      throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();

    if (!includeNullValues) {
      mapper.setSerializationInclusion(Include.NON_NULL);
    }
    if(prettyPrint){
      mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    return mapper.writeValueAsString(object);
  }

  public static <T> T toObject(String json, Class className, boolean ignoreUnknownFields)
      throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    if(ignoreUnknownFields){
      mapper.configure(
          DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    JavaType type = mapper.getTypeFactory().
        constructType(className);

    return mapper.readValue(json, type);
  }

}
