package com.maumjido.generate.mybatis.source.util;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

public class JsonUtil {
  public static final String[] EXCEPT_WORD_IN_JSON = { "password", "pwd", "userPwd", "currentPwd", "versionNum" };
  private static ObjectMapper mapper = new ObjectMapper();
  static {
    mapper.setSerializationInclusion(Include.NON_NULL);
    mapper.setSerializationInclusion(Include.NON_EMPTY);

    SimpleFilterProvider simpleFilterProvider = new SimpleFilterProvider();
    FilterProvider filters = simpleFilterProvider//
        .addFilter("myMixIn", SimpleBeanPropertyFilter.serializeAllExcept(EXCEPT_WORD_IN_JSON));
    mapper.addMixIn(Object.class, MyMixIn.class);
    mapper.setFilterProvider(filters);
  }

  @JsonFilter("myMixIn")
  public class MyMixIn {
    //
  }

  public static String toJson(Object object) {
    if (object == null) {
      return null;
    }
    try {
      return mapper.writer().writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public static <T> T toObject(String string, Class<T> valueType) {
    return toObject(string, valueType, null);
  }

  public static <T> T toObject(String string, Class<T> valueType, PropertyNamingStrategy pns) {
    if (string == null) {
      return null;
    }
    try {
      ObjectMapper mapper = new ObjectMapper();
      if (pns != null) {
        mapper.setPropertyNamingStrategy(pns);
      }
      return mapper.readValue(string, valueType);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static <T> T toObject(String string, TypeReference<?> typeReference) {
    if (string == null) {
      return null;
    }
    try {
      ObjectMapper mapper = new ObjectMapper();
      return mapper.readValue(string, typeReference);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
