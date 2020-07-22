package by.crearec.telegram.configuration;

import by.crearec.telegram.MainAWS;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.ext.ContextResolver;
import java.io.IOException;

public class ObjectMapperProvider implements ContextResolver<ObjectMapper> {
	private static final Logger LOGGER = LogManager.getLogger(MainAWS.class);
	private static final ObjectMapper OBJECT_MAPPER;

	static {
		OBJECT_MAPPER = new ObjectMapper();
		OBJECT_MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
		OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		OBJECT_MAPPER.registerModule(new JavaTimeModule());
		OBJECT_MAPPER.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
	}

	@Override
	public ObjectMapper getContext(final Class<?> type) {
		return OBJECT_MAPPER;
	}

	public static String toJSON(Object object) {
		String json = null;
		try {
			json = OBJECT_MAPPER.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			LOGGER.error("Error on convert to JSON: {}", object);
		}
		return json;
	}

	public static <T> T readValue(String json, Class<T> type) {
		T result = null;
		try {
			result = OBJECT_MAPPER.readValue(json, type);
		} catch (IOException e) {
			LOGGER.error("Error on reading from JSON : {}", json);
		}

		return result;
	}

	public static <T> T readValue(String json, TypeReference<T> valueTypeRef) {
		T result = null;
		try {
			result = OBJECT_MAPPER.readValue(json, valueTypeRef);
		} catch (IOException e) {
			LOGGER.error("Error on reading from JSON : {}", json);
		}
		return result;
	}

	public static <T> T readValue(Object object, TypeReference<T> valueTypeRef) {
		T result = null;
		if (object != null) {
			String strJson = toJSON(object);
			result = StringUtils.isNotEmpty(strJson) ? readValue(strJson, valueTypeRef) : null;
		}
		return result;
	}

	public static <T> T readValue(Object object, Class<T> type) {
		T result = null;
		if (object != null) {
			String strJson = toJSON(object);
			result = StringUtils.isNotEmpty(strJson) ? readValue(strJson, type) : null;
		}
		return result;
	}
}
