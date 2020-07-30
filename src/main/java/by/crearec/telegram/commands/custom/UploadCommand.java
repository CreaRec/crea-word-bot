package by.crearec.telegram.commands.custom;

import by.crearec.telegram.commands.GeneralCommand;
import by.crearec.telegram.entity.state.StateType;
import by.crearec.telegram.entity.state.UploadState;
import by.crearec.telegram.service.ActiveUserService;
import by.crearec.telegram.service.WordService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public final class UploadCommand extends GeneralCommand {
	private static final Logger LOGGER = LogManager.getLogger(UploadCommand.class);

	private final ActiveUserService activeUserService;
	private final WordService wordService;

	public UploadCommand(ActiveUserService activeUserService, WordService wordService) {
		super(CommandType.UPLOAD.getName(), "upload xlsx file\n");
		this.activeUserService = activeUserService;
		this.wordService = wordService;
	}

	@Override
	public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
		LOGGER.info("Execute message info: userId: [{}], commandIdentifier: [{}]", user.getId(), getCommandIdentifier());
		SendMessage message = new SendMessage();
		message.setChatId(chat.getId().toString());
		if (activeUserService.hasUser(user.getId())) {
			activeUserService.getUser(user.getId()).setState(new UploadState(activeUserService, wordService));
			message.setText("Загрузите файл Excel (подробнее о загрузке файлов в разделе /help )");
			execute(absSender, message, user, StateType.UPLOAD, true);
		} else {
			message.setText("Для начала используйте команду /start");
			execute(absSender, message, user, StateType.ACTIVE, true);
		}
	}
}