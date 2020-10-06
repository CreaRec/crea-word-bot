package by.crearec.telegram.commands;

import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * Representation of a command, which can be executed
 */
public abstract class BotCommand implements IBotCommand {
	public final static String COMMAND_INIT_CHARACTER = "/";
	public static final String COMMAND_PARAMETER_SEPARATOR_REGEXP = "\\s+";
	private final static int COMMAND_MAX_LENGTH = 32;

	private final String commandIdentifier;
	private final String description;

	/**
	 * Construct a command
	 *
	 * @param commandIdentifier the unique identifier of this command (e.g. the command string to
	 *                          enter into chat)
	 * @param description       the description of this command
	 */
	public BotCommand(String commandIdentifier, String description) {

		if (StringUtils.isEmpty(commandIdentifier)) {
			throw new IllegalArgumentException("commandIdentifier for command cannot be null or empty");
		}

		if (commandIdentifier.startsWith(COMMAND_INIT_CHARACTER)) {
			commandIdentifier = commandIdentifier.substring(1);
		}

		if (commandIdentifier.length() + 1 > COMMAND_MAX_LENGTH) {
			throw new IllegalArgumentException("commandIdentifier cannot be longer than " + COMMAND_MAX_LENGTH + " (including " + COMMAND_INIT_CHARACTER + ")");
		}

		this.commandIdentifier = commandIdentifier;
		this.description = description;
	}

	/**
	 * Get the identifier of this command
	 *
	 * @return the identifier
	 */
	@Override
	public final String getCommandIdentifier() {
		return commandIdentifier;
	}

	/**
	 * Get the description of this command
	 *
	 * @return the description as String
	 */
	@Override
	public final String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return "<b>" + COMMAND_INIT_CHARACTER + getCommandIdentifier() + "</b>\n" + getDescription();
	}

	/**
	 * Process the message and execute the command
	 *
	 * @param absSender absSender to send messages over
	 * @param message   the message to process
	 * @param arguments passed arguments
	 */
	@Override
	public void processMessage(AbsSender absSender, Message message, String[] arguments) {
		execute(absSender, message.getFrom(), message.getChat(), arguments);
	}

	@Override
	public void processCallback(AbsSender absSender, CallbackQuery callbackQuery, String[] arguments) {
		execute(absSender, callbackQuery.getFrom(), callbackQuery.getMessage().getChat(), arguments);
	}
}