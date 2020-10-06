package by.crearec.telegram.commands.custom;

import by.crearec.telegram.commands.GeneralCommand;
import by.crearec.telegram.entity.state.StateType;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public final class TranscriptionCommand extends GeneralCommand {
	private static final Logger LOGGER = LogManager.getLogger(TranscriptionCommand.class);

	public TranscriptionCommand() {
		super(CommandType.TRANSCRIPTION.getName(), "print word transcription\n");
	}

	@Override
	public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
		LOGGER.info("Execute message info: userId: [{}], commandIdentifier: [{}]", user.getId(), getCommandIdentifier());
		SendMessage message = new SendMessage();
		message.setChatId(chat.getId().toString());
		if (ArrayUtils.isNotEmpty(strings)) {
			message.setText(String.join(" ", strings));
			execute(absSender, message, user, StateType.ACTIVE, true);
		}
	}
}