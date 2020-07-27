package by.crearec.telegram.entity.state;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface BaseState {
	Logger LOGGER = LogManager.getLogger(BaseState.class);

	StateType getType();
	void onProcess(AbsSender absSender, Message message, String[] strings);
	default void execute(AbsSender sender, SendMessage message, User user) {
		try {
			sender.execute(message);
			LOGGER.info("Execute message info: userId: [{}], stateIdentifier: [{}]", user.getId(), getType());
		} catch (TelegramApiException e) {
			LOGGER.error("Execute message error: userId: [{}], stateIdentifier: [{}]", user.getId(), getType());
		}
	}
}
