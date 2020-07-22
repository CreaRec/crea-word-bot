package by.crearec.telegram.entity.state;

import by.crearec.telegram.repository.WordRepository;
import by.crearec.telegram.utils.FileUtils;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class UploadState implements BaseState {

	private final WordRepository wordRepository;

	public UploadState(WordRepository wordRepository) {
		this.wordRepository = wordRepository;
	}

	@Override
	public StateType getType() {
		return StateType.UPLOAD;
	}

	@Override
	public void onProcess(AbsSender absSender, User user, Chat chat, String[] strings) {
		wordRepository.dropCollection(user.getId().toString());
//		FileUtils.readFile()
	}
}
