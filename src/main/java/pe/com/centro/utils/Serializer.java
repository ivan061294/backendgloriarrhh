package pe.com.centro.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

/**
 * Custom Serializer extended from Jackson that
 * manages parsing errors.
 */
@Slf4j
public final class Serializer {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            //.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

    /**
     * Serializes an {@link java.lang.Object} to {@link java.lang.String}
     *
     * @param object the {@link java.lang.Object} to be serialized
     * @return a serialized object
     */
    public static String serialize(Object object) {
        String result = null;
        try {
            result = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Error during serialization", e);
        }
        return result;
    }

    /**
     * Deserializes a {@link java.lang.String} to {@link java.lang.Object}
     *
     * @param string the serialized object to be deserialized
     * @param clazz  the object's class type to be returned
     * @param <T>    a generic that catches the class reference
     * @return a {@link T} object
     */
    public static <T> T deserialize(String string, Class<T> clazz) {
        T result = null;
        try {
            result = objectMapper.readValue(string, clazz);
        } catch (JsonProcessingException e) {
            log.error("Error during deserialization", e);
        }
        return result;
    }

    /**
     * Deserializes a {@link java.lang.String} to {@link java.lang.Object}
     *
     * @param string the serialized object to be deserialized
     * @param type   the type reference (usually used for {@link java.util.List} or {@link java.util.Map} objects)
     * @param <T>    a generic that catches the class reference
     * @return a {@link T} object
     */
    public static <T> T deserialize(String string, TypeReference<T> type) {
        T result = null;
        try {
            result = objectMapper.readValue(string, type);
        } catch (JsonProcessingException e) {
            log.error("Error during deserialization", e);
        }
        return result;
    }
}
