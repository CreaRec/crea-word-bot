package by.crearec.telegram.commands;

import by.crearec.telegram.entity.state.StateType;
import by.crearec.telegram.utils.MessageUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public abstract class GeneralCommand extends BotCommand {
	private static final Logger LOGGER = LogManager.getLogger(GeneralCommand.class);

	public GeneralCommand(String commandIdentifier, String description) {
		super(commandIdentifier, description);
	}

	public void execute(AbsSender sender, SendMessage message, User user, StateType stateType, boolean addDefaultButtons) {
		try {
			if (addDefaultButtons) {
				MessageUtils.addBottomButtons(message, stateType);
			}
			sender.execute(message);
			LOGGER.info("Execute message info: userId: [{}], commandIdentifier: [{}]", user.getId(), getCommandIdentifier());
		} catch (TelegramApiException e) {
			LOGGER.error("Execute message error: userId: [{}], commandIdentifier: [{}]", user.getId(), getCommandIdentifier());
		}
	}
}