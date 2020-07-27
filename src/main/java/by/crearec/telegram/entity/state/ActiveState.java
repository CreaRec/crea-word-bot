package by.crearec.telegram.entity.state;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class ActiveState implements BaseState {
	@Override
	public StateType getType() {
		return StateType.ACTIVE;
	}

	@Override
	public void onProcess(AbsSender absSender, Message message, String[] strings) {
		// do nothing
	}
}
