package by.crearec.telegram.commands.custom;

import by.crearec.telegram.commands.GeneralCommand;
import by.crearec.telegram.entity.ActiveUser;
import by.crearec.telegram.entity.mongo.Word;
import by.crearec.telegram.entity.state.StateType;
import by.crearec.telegram.service.ActiveUserService;
import by.crearec.telegram.service.WordService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.List;

public final class NextCommand extends GeneralCommand {
	private static final Logger LOGGER = LogManager.getLogger(NextCommand.class);

	private final ActiveUserService activeUserService;
	private final WordService wordService;

	public NextCommand(ActiveUserService activeUserService, WordService wordService) {
		super(CommandType.NEXT.getName(), "find next word\n");
		this.activeUserService = activeUserService;
		this.wordService = wordService;
	}

	@Override
	public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
		LOGGER.info("Execute message info: userId: [{}], commandIdentifier: [{}]", user.getId(), getCommandIdentifier());
		SendMessage message = new SendMessage();
		message.setChatId(chat.getId().toString());
		if (!activeUserService.hasUser(user.getId())) {
			activeUserService.addUser(new ActiveUser(user.getId()));
		}
		ActiveUser activeUser = activeUserService.getUser(user.getId());
		StateType type = activeUser.getState().getType();
		if (type == StateType.ACTIVE) {
			Word random = wordService.getRandom(user.getId().toString());
			if (random == null) {
				message.setText("У вас не загружен словарь. Загрузите, пожалуйста, при помощи команды /upload");
				execute(absSender, message, user, type, true);
			} else {
				message.setText(random.getEn());
				addTranslateButton(message, random.getRu());
				execute(absSender, message, user, type, false);
			}
		} else {
			message.setText("На данный момент вы находитесь в статусе " + type.name() + ". Для отмены используйте команту /cancel");
			execute(absSender, message, user, type, true);
		}
	}

	private static void addTranslateButton(SendMessage sendMessage, String translateWord) {
		if (StringUtils.isNotEmpty(translateWord)) {
			InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
			inlineKeyboardButton1.setText("Translate");
			inlineKeyboardButton1.setCallbackData(CommandType.TRANSLATE.getCommand() + " " + translateWord);
			List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
			keyboardButtonsRow1.add(inlineKeyboardButton1);
			List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
			rowList.add(keyboardButtonsRow1);
			InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup().setKeyboard(rowList);
			sendMessage.setReplyMarkup(inlineKeyboardMarkup);
		}
	}
}