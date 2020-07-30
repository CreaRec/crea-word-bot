package by.crearec.telegram.utils;

import by.crearec.telegram.bot.CreaWordBot;
import by.crearec.telegram.commands.custom.CommandType;
import by.crearec.telegram.entity.state.StateType;
import lombok.experimental.UtilityClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class MessageUtils {
	private final Logger LOGGER = LogManager.getLogger(CreaWordBot.class);

	public void sendUnknownCommandMessage(AbsSender absSender, User from, Long chatId, String text) {
		LOGGER.warn("User {} is trying to execute unknown command '{}'.", from.getId(), text);
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(chatId);
		sendMessage.setText(text + " command not found!");
		try {
			absSender.execute(sendMessage);
		} catch (TelegramApiException e) {
			LOGGER.error("Error while replying unknown command to user {}.", from, e);
		}
	}

	public void addBottomButtons(SendMessage sendMessage, StateType stateType) {
		// Создаем клавиуатуру
		ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
		sendMessage.setReplyMarkup(replyKeyboardMarkup);
		replyKeyboardMarkup.setSelective(true);
		replyKeyboardMarkup.setResizeKeyboard(true);
		replyKeyboardMarkup.setOneTimeKeyboard(false);

		// Создаем список строк клавиатуры
		List<KeyboardRow> keyboard = new ArrayList<>();

		// Первая строчка клавиатуры
		KeyboardRow keyboardFirstRow = new KeyboardRow();
		// Вторая строчка клавиатуры
		KeyboardRow keyboardSecondRow = new KeyboardRow();

		// Добавляем кнопки в первую строчку клавиатуры
		if (stateType == StateType.ACTIVE) {
			keyboardFirstRow.add(new KeyboardButton(CommandType.NEXT.getCommand()));
			keyboardSecondRow.add(new KeyboardButton(CommandType.UPLOAD.getCommand()));
		} else {
			keyboardFirstRow.add(new KeyboardButton(CommandType.CANCEL.getCommand()));
		}

		// Добавляем кнопки во вторую строчку клавиатуры
//		keyboardSecondRow.add(new KeyboardButton(CommandType.HELP.getCommand()));

		// Добавляем все строчки клавиатуры в список
		keyboard.add(keyboardFirstRow);
		keyboard.add(keyboardSecondRow);
		// и устанваливаем этот список клавиатуре
		replyKeyboardMarkup.setKeyboard(keyboard);
	}
}
