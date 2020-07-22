package by.crearec.telegram.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
public class TelegramGetFileDTO {
	private String fileId;
	private String fileUniqueId;
	private Integer fileSize;
	private String filePath;
}
