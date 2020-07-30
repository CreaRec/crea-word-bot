package by.crearec.telegram.commands.custom;

import by.crearec.telegram.commands.GeneralCommand;
import by.crearec.telegram.entity.ActiveUser;
import by.crearec.telegram.entity.state.ActiveState;
import by.crearec.telegram.entity.state.StateType;
import by.crearec.telegram.service.ActiveUserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public final class CancelCommand extends GeneralCommand {
	private static final Logger LOGGER = LogManager.getLogger(CancelCommand.class);

	private final ActiveUserService activeUserService;

	public CancelCommand(ActiveUserService activeUserService) {
		super(CommandType.CANCEL.getName(), "return to active state\n");
		this.activeUserService = activeUserService;
	}

	@Override
	public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
		LOGGER.info("Execute message info: userId: [{}], commandIdentifier: [{}]", user.getId(), getCommandIdentifier());
		SendMessage message = new SendMessage();
		message.setChatId(chat.getId().toString());
		if (activeUserService.hasUser(user.getId())) {
			ActiveUser activeUser = activeUserService.getUser(user.getId());
			activeUser.setState(new ActiveState());
		}
		message.setText("Возврат к обычному состоянию. Используйте /next для поиска следующего слова.");
		execute(absSender, message, user, StateType.ACTIVE, true);
	}
}