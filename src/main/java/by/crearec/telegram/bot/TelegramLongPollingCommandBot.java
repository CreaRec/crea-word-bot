package by.crearec.telegram.bot;


import by.crearec.telegram.commands.CommandRegistry;
import by.crearec.telegram.commands.IBotCommand;
import by.crearec.telegram.commands.ICommandRegistry;
import by.crearec.telegram.entity.state.StateType;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Collection;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * This class adds command functionality to the TelegramLongPollingBot
 */
public abstract class TelegramLongPollingCommandBot extends TelegramLongPollingBot implements ICommandRegistry {
	private static final Logger LOGGER = LogManager.getLogger(TelegramLongPollingCommandBot.class);

	private final CommandRegistry commandRegistry;

	/**
	 * Creates a TelegramLongPollingCommandBot using default options
	 * Use ICommandRegistry's methods on this bot to register commands
	 */
	public TelegramLongPollingCommandBot() {
		this(ApiContext.getInstance(DefaultBotOptions.class));
	}

	/**
	 * Creates a TelegramLongPollingCommandBot with custom options and allowing commands with
	 * usernames
	 * Use ICommandRegistry's methods on this bot to register commands
	 *
	 * @param options Bot options
	 */
	public TelegramLongPollingCommandBot(DefaultBotOptions options) {
		this(options, true);
	}

	/**
	 * Creates a TelegramLongPollingCommandBot
	 * Use ICommandRegistry's methods on this bot to register commands
	 *
	 * @param options                   Bot options
	 * @param allowCommandsWithUsername true to allow commands with parameters (default),
	 *                                  false otherwise
	 */
	public TelegramLongPollingCommandBot(DefaultBotOptions options, boolean allowCommandsWithUsername) {
		super(options);
		this.commandRegistry = new CommandRegistry(allowCommandsWithUsername, this.getBotUsername());
	}

	@Override
	public final void onUpdateReceived(Update update) {
		if (update.hasMessage()) {
			Message message = update.getMessage();
			if (message.isCommand() && !filter(message)) {
				if (!commandRegistry.executeCommand(this, message)) {
					processInvalidCommandUpdate(update);
				}
				return;
			} if (message.hasDocument()) {
				CreaWordBot.getInstance().stateExecute(this, message, "Для загрузки файла используйте команду /upload и формат файла .xlsx", StateType.UPLOAD);
				return;
			}
		} else if (update.hasCallbackQuery()) {
			CallbackQuery callbackQuery = update.getCallbackQuery();
			String data = callbackQuery.getData();
			if (StringUtils.isNotEmpty(data)) {
				if (!commandRegistry.executeCommand(this, callbackQuery)) {
					processInvalidCommandUpdate(update);
				}
				return;
			}
		}
		processNonCommandUpdate(update);
	}

	/**
	 * This method is called when user sends a not registered command. By default it will just call processNonCommandUpdate(),
	 * override it in your implementation if you want your bot to do other things, such as sending an error message
	 *
	 * @param update Received update from Telegram
	 */
	protected void processInvalidCommandUpdate(Update update) {
		processNonCommandUpdate(update);
	}


	/**
	 * Override this function in your bot implementation to filter messages with commands
	 * <p>
	 * For example, if you want to prevent commands execution incoming from group chat:
	 * #
	 * # return !message.getChat().isGroupChat();
	 * #
	 *
	 * @param message Received message
	 * @return true if the message must be ignored by the command bot and treated as a non command message,
	 * false otherwise
	 * @note Default implementation doesn't filter anything
	 */
	protected boolean filter(Message message) {
		return false;
	}

	@Override
	public final boolean register(IBotCommand botCommand) {
		LOGGER.info("Registering '{}'...", botCommand.getCommandIdentifier());
		return commandRegistry.register(botCommand);
	}

	@Override
	public final Map<IBotCommand, Boolean> registerAll(IBotCommand... botCommands) {
		return commandRegistry.registerAll(botCommands);
	}

	@Override
	public final boolean deregister(IBotCommand botCommand) {
		return commandRegistry.deregister(botCommand);
	}

	@Override
	public final Map<IBotCommand, Boolean> deregisterAll(IBotCommand... botCommands) {
		return commandRegistry.deregisterAll(botCommands);
	}

	@Override
	public final Collection<IBotCommand> getRegisteredCommands() {
		return commandRegistry.getRegisteredCommands();
	}

	@Override
	public void registerDefaultMessageAction(BiConsumer<AbsSender, Message> defaultConsumer) {
		commandRegistry.registerDefaultMessageAction(defaultConsumer);
	}

	@Override
	public void registerDefaultCallbackAction(BiConsumer<AbsSender, CallbackQuery> defaultConsumer) {
		commandRegistry.registerDefaultCallbackAction(defaultConsumer);
	}

	@Override
	public final IBotCommand getRegisteredCommand(String commandIdentifier) {
		return commandRegistry.getRegisteredCommand(commandIdentifier);
	}

	/**
	 * @return Bot username
	 */
	@Override
	public abstract String getBotUsername();

	/**
	 * Process all updates, that are not commands.
	 *
	 * @param update the update
	 * @warning Commands that have valid syntax but are not registered on this bot,
	 * won't be forwarded to this method <b>if a default action is present</b>.
	 */
	public abstract void processNonCommandUpdate(Update update);
}