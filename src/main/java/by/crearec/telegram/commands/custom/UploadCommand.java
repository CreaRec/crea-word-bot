package by.crearec.telegram.commands.custom;

import by.crearec.telegram.commands.GeneralCommand;
import by.crearec.telegram.entity.mongo.Word;
import by.crearec.telegram.entity.state.UploadState;
import by.crearec.telegram.repository.WordRepository;
import by.crearec.telegram.service.ActiveUserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public final class UploadCommand extends GeneralCommand {
	private static final Logger LOGGER = LogManager.getLogger(UploadCommand.class);

	private final ActiveUserService activeUserService;
	private final WordRepository wordRepository;

	public UploadCommand(ActiveUserService activeUserService, WordRepository wordRepository) {
		super("start", "start using bot\n");
		this.activeUserService = activeUserService;
		this.wordRepository = wordRepository;
	}

	@Override
	public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
		LOGGER.info("Execute message info: userId: [{}], commandIdentifier: [{}]", user.getId(), getCommandIdentifier());
		SendMessage message = new SendMessage();
		message.setChatId(chat.getId().toString());
		if (activeUserService.hasUser(user.getId())) {
			activeUserService.getUser(user.getId()).setState(new UploadState(wordRepository));
			message.setText("Загрузите файл Excel (подробнее о загрузке файлов в разделе /help )");
			execute(absSender, message, user);
		} else {
			message.setText("Для начала используйте команду /start");
			execute(absSender, message, user);
		}
	}
}