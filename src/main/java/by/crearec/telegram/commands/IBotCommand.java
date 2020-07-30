package by.crearec.telegram.commands;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * This Interface represents the a Command that can be executed
 */
public interface IBotCommand {
	/**
	 * Get the identifier of this command
	 *
	 * @return the identifier
	 */
	String getCommandIdentifier();

	/**
	 * Get the description of this command
	 *
	 * @return the description as String
	 */
	String getDescription();

	/**
	 * Process the message and execute the command
	 *
	 * @param absSender absSender to send messages over
	 * @param message   the message to process
	 */
	void processMessage(AbsSender absSender, Message message, String[] arguments);

	void processCallback(AbsSender absSender, CallbackQuery callbackQuery, String[] arguments);

	void execute(AbsSender absSender, User user, Chat chat, String[] arguments);
}