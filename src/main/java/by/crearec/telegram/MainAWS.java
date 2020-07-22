package by.crearec.telegram;

import by.crearec.telegram.bot.CreaWordBot;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainAWS implements RequestStreamHandler {
	private static final Logger LOGGER = LogManager.getLogger(MainAWS.class);

	private static final ObjectMapper MAPPER = new ObjectMapper();

	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		Update update;
		try {
			update = MAPPER.readValue(input, Update.class);
			LOGGER.info("Update: [{}]", update);
		} catch (Exception e) {
			LOGGER.error("Failed to parse update: ", e);
			throw new RuntimeException("Failed to parse update!", e);
		}
		CreaWordBot.getInstance().onUpdateReceived(update);
	}
}
