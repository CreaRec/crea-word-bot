package by.crearec.telegram.bot;

import by.crearec.telegram.commands.custom.CancelCommand;
import by.crearec.telegram.commands.custom.NextCommand;
import by.crearec.telegram.commands.custom.StartCommand;
import by.crearec.telegram.commands.custom.UploadCommand;
import by.crearec.telegram.entity.state.BaseState;
import by.crearec.telegram.entity.state.StateType;
import by.crearec.telegram.repository.WordRepository;
import by.crearec.telegram.service.ActiveUserService;
import by.crearec.telegram.service.WordService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;

public final class CreaWordBot extends TelegramLongPollingCommandBot {
	private static final Logger LOGGER = LogManager.getLogger(CreaWordBot.class);

	private static final String BOT_NAME = "CreaWordBot";
	public static final String BOT_TOKEN = "656020815:AAE5dUwFDw4WnpE-_NEPbXFVGW849041V3g";

	private final ActiveUserService activeUserService = new ActiveUserService();
	private final WordService wordService = new WordService(new WordRepository());

	public static class CreaWordBotHolder {
		public static final CreaWordBot HOLDER_INSTANCE = new CreaWordBot();
	}

	public static CreaWordBot getInstance() {
		return CreaWordBotHolder.HOLDER_INSTANCE;
	}

	private CreaWordBot() {
		LOGGER.info("Initializing Anonymizer Bot...");

		LOGGER.info("Registering commands...");
		register(new StartCommand(activeUserService, wordService));
		register(new UploadCommand(activeUserService, wordService));
		register(new NextCommand(activeUserService, wordService));
		register(new CancelCommand(activeUserService));

		LOGGER.info("Registering default action'...");
		registerDefaultAction(((absSender, message) -> {
			LOGGER.warn("User {} is trying to execute unknown command '{}'.", message.getFrom().getId(), message.getText());

			SendMessage text = new SendMessage();
			text.setChatId(message.getChatId());
			text.setText(message.getText() + " command not found!");

			try {
				absSender.execute(text);
			} catch (TelegramApiException e) {
				LOGGER.error("Error while replying unknown command to user {}.", message.getFrom(), e);
			}

//            helpCommand.execute(absSender, message.getFrom(), message.getChat(), new String[] {});
		}));
	}

	@Override
	public String getBotUsername() {
		return BOT_NAME;
	}

	@Override
	public String getBotToken() {
		return BOT_TOKEN;
	}

	@Override
	public void processNonCommandUpdate(Update update) {
		LOGGER.info("Processing non-command update...");

		if (!update.hasMessage()) {
			LOGGER.error("Update doesn't have a body!");
			throw new IllegalStateException("Update doesn't have a body!");
		}

		Message msg = update.getMessage();
		User user = msg.getFrom();

		LOGGER.warn("Processing unknown message for user [{}]", user.getId());
	}

	public void stateExecute(AbsSender absSender, Message message, String wrongStateMessage, StateType... expectedStateTypes) {
		try {
			User user = message.getFrom();
			SendMessage messageToSend = new SendMessage();
			messageToSend.setChatId(message.getChat().getId().toString());
			if (activeUserService.hasUser(user.getId())) {
				BaseState state = activeUserService.getUser(user.getId()).getState();
				StateType currentType = state.getType();
				boolean result = false;
				for (StateType expectedStateType : expectedStateTypes) {
					if (expectedStateType == currentType) {
						state.onProcess(absSender, message, null);
						result = true;
						break;
					}
				}
				if (!result) {
					messageToSend.setText(wrongStateMessage);
					absSender.execute(messageToSend);
				}
			} else {
				messageToSend.setText("Для начала используйте команду /start");
				absSender.execute(messageToSend);
			}
		} catch (TelegramApiException e) {
			LOGGER.error("Unexpected error", e);
		}
	}
}