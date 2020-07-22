package by.crearec.telegram.commands.custom;

import by.crearec.telegram.commands.GeneralCommand;
import by.crearec.telegram.entity.ActiveUser;
import by.crearec.telegram.entity.mongo.Word;
import by.crearec.telegram.repository.MongoRepository;
import by.crearec.telegram.repository.WordRepository;
import by.crearec.telegram.service.ActiveUserService;
import by.crearec.telegram.service.WordService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public final class StartCommand extends GeneralCommand {
	private static final Logger LOGGER = LogManager.getLogger(StartCommand.class);

	private final ActiveUserService activeUserService;
	private final WordService wordService;

	public StartCommand(ActiveUserService activeUserService, WordService wordService) {
		super("start", "start using bot\n");
		this.activeUserService = activeUserService;
		this.wordService = wordService;
	}

	@Override
	public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
		LOGGER.info("Execute message info: userId: [{}], commandIdentifier: [{}]", user.getId(), getCommandIdentifier());
		SendMessage message = new SendMessage();
		message.setChatId(chat.getId().toString());
		if (activeUserService.hasUser(user.getId())) {
			message.setText("Вы уже активны!");
			execute(absSender, message, user);
		} else {
			activeUserService.addUser(new ActiveUser(user.getId()));
			message.setText("Пытаемся найти Ваш словарь...");
			execute(absSender, message, user);
			Word random = wordService.getRandom(user.getId().toString());
			if (random == null) {
				message.setText("У вас не загружен словарь. Загрузите, пожалуйста, при помощи команды /upload");
				execute(absSender, message, user);
			} else {
				message.setText("Словарь найден. Для поиска следующего слова используйте /next. Ваше первое слово:");
				execute(absSender, message, user);
				message.setText(random.getEn());
				execute(absSender, message, user);
			}
		}
	}
}