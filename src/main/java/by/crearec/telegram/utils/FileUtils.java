package by.crearec.telegram.utils;

import by.crearec.telegram.bot.CreaWordBot;
import by.crearec.telegram.configuration.Configurator;
import by.crearec.telegram.dto.TelegramGetFileDTO;
import by.crearec.telegram.dto.TelegramResponseDTO;
import by.crearec.telegram.entity.mongo.Word;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
	private static final Logger LOGGER = LogManager.getLogger(FileUtils.class);

	public static List<Word> readFile(String fileId) {
		final Client client = Configurator.getInstance().getClient();
		List<Word> wordList = new ArrayList<>();
		Response response = client.target("https://api.telegram.org/bot" + CreaWordBot.BOT_TOKEN + "/getFile?file_id=" + fileId).request(MediaType.APPLICATION_JSON_TYPE)
		                          .get();
		TelegramResponseDTO<TelegramGetFileDTO> getFileResponse = response.readEntity(new GenericType<TelegramResponseDTO<TelegramGetFileDTO>>() {});
		if (getFileResponse.getOk()) {
			TelegramGetFileDTO result = getFileResponse.getResult();
			String fileUrl = "https://api.telegram.org/file/bot" + CreaWordBot.BOT_TOKEN + "/" + result.getFilePath();
			try (Workbook workbook = new XSSFWorkbook(new BufferedInputStream(new URL(fileUrl).openStream()))) {
				Sheet sheet = workbook.getSheetAt(0);
				DataFormatter formatter = new DataFormatter();
				for (Row row : sheet) {
					Word word = new Word(formatter.formatCellValue(row.getCell(0)), formatter.formatCellValue(row.getCell(1)), formatter.formatCellValue(row.getCell(2)));
					wordList.add(word);
				}
			} catch (IOException e) {
				LOGGER.error("File downloading and processing fail", e);
			}
		} else {
			LOGGER.warn("Telegram API: getFile: error message : [{}]", getFileResponse.getDescription());
		}
		return wordList;
	}
}
