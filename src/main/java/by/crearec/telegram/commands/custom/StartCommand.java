package by.crearec.telegram.commands.custom;

import by.crearec.telegram.bot.CreaWordBot;
import by.crearec.telegram.commands.GeneralCommand;
import by.crearec.telegram.entity.ActiveUser;
import by.crearec.telegram.entity.mongo.Word;
import by.crearec.telegram.entity.state.StateType;
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
		super(CommandType.START.getName(), "start using bot\n");
		this.activeUserService = activeUserService;
		this.wordService = wordService;
	}

	@Override
	public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
		LOGGER.info("Execute message info: userId: [{}], commandIdentifier: [{}]", user.getId(), getCommandIdentifier());
		SendMessage message = new SendMessage();
		message.setChatId(chat.getId().toString());
		ActiveUser activeUser = activeUserService.getUser(user.getId());
		if (activeUser == null) {
			activeUserService.addUser(new ActiveUser(user.getId()));
			message.setText("Пытаемся найти Ваш словарь...");
			execute(absSender, message, user, StateType.ACTIVE, true);
			Word random = wordService.getRandom(user.getId().toString());
			if (random == null) {
				message.setText("У вас не загружен словарь. Загрузите, пожалуйста, при помощи команды /upload");
				execute(absSender, message, user, StateType.ACTIVE, true);
			} else {
				message.setText("Словарь найден. Для поиска следующего слова используйте /next. Ваше первое слово:");
				execute(absSender, message, user, StateType.ACTIVE, true);
				CreaWordBot.getInstance().getRegisteredCommand(CommandType.NEXT.getName()).execute(absSender, user, chat, null);
			}
		} else {
			message.setText("Вы уже активны! Используйте команду /next");
			execute(absSender, message, user, activeUser.getState().getType(), true);
		}
	}
}