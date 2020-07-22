package by.crearec.telegram.dto;

import lombok.Data;

@Data
public class TelegramResponseDTO<T> {
	private Boolean ok;
	private String description;
	private T result;
}
