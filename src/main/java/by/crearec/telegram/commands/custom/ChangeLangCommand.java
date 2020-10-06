package by.crearec.telegram.commands.custom;

import by.crearec.telegram.bot.CreaWordBot;
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

public final class ChangeLangCommand extends GeneralCommand {
	private static final Logger LOGGER = LogManager.getLogger(ChangeLangCommand.class);

	private final ActiveUserService activeUserService;

	public ChangeLangCommand(ActiveUserService activeUserService) {
		super(CommandType.CHANGE_LANG.getName(), "change language\n");
		this.activeUserService = activeUserService;
	}

	@Override
	public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
		Integer userId = user.getId();
		LOGGER.info("Execute message info: userId: [{}], commandIdentifier: [{}]", userId, getCommandIdentifier());
		SendMessage message = new SendMessage();
		message.setChatId(chat.getId().toString());
		if (activeUserService.hasUser(userId)) {
			ActiveUser activeUser = activeUserService.getUser(userId);
			activeUser.setIsEnglish(!activeUser.getIsEnglish());
			message.setText("Язык перевода успешно изменен");
			execute(absSender, message, user, activeUser.getState().getType(), true);
		} else {
			CreaWordBot.getInstance().getRegisteredCommand(CommandType.NEXT.getName()).execute(absSender, user, chat, null);
		}
	}
}