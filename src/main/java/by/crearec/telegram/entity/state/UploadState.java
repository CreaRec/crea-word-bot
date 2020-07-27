package by.crearec.telegram.entity.state;

import by.crearec.telegram.entity.mongo.Word;
import by.crearec.telegram.service.ActiveUserService;
import by.crearec.telegram.service.WordService;
import by.crearec.telegram.utils.FileUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;

public class UploadState implements BaseState {

	private final WordService wordService;
	private final ActiveUserService activeUserService;

	public UploadState(ActiveUserService activeUserService, WordService wordService) {
		this.activeUserService = activeUserService;
		this.wordService = wordService;
	}

	@Override
	public StateType getType() {
		return StateType.UPLOAD;
	}

	@Override
	public void onProcess(AbsSender absSender, Message message, String[] strings) {
		User user = message.getFrom();
		Integer userId = user.getId();
		wordService.deleteAllByUserId(userId.toString());
		Document document = message.getDocument();
		if (document.getFileName().endsWith(".xlsx")) {
			List<Word> wordList = FileUtils.readFile(document.getFileId());
			wordService.save(userId.toString(), wordList);
			activeUserService.getUser(userId).setState(new ActiveState());
			SendMessage messageToSend = new SendMessage();
			messageToSend.setChatId(message.getChat().getId().toString());
			messageToSend.setText("Файл успешно загружен. Используйте команду /next для поиска слов");
			execute(absSender, messageToSend, user);
		}
	}
}
