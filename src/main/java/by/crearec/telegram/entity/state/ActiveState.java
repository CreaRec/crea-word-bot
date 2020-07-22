package by.crearec.telegram.entity.state;

import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class ActiveState implements BaseState {
	@Override
	public StateType getType() {
		return StateType.ACTIVE;
	}

	@Override
	public void onProcess(AbsSender absSender, User user, Chat chat, String[] strings) {
		// do nothing
	}
}
