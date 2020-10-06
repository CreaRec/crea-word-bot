package by.crearec.telegram.commands.custom;

import java.util.Arrays;

public enum CommandType {
	START("start", "/start"),
	NEXT("next", "/next"),
	UPLOAD("upload", "/upload"),
//	ADD("add", "/add"),
//	DELETE("delete", "/delete"),
	CANCEL("cancel", "/cancel"),

	TRANSLATE("translate", "/translate"),
	TRANSCRIPTION("transcription", "/transcription"),

	CHANGE_LANG("changeLang", "/changeLang"),

	HELP("help", "/help");

	private final String name;
	private final String command;

	CommandType(String name, String command) {
		this.name = name;
		this.command = command;
	}

	public String getName() {
		return name;
	}

	public String getCommand() {
		return command;
	}

	public static CommandType getByCommand(String command) {
		return Arrays.stream(CommandType.values()).filter(item -> item.getCommand().equalsIgnoreCase(command)).findAny().orElse(null);
	}
}
