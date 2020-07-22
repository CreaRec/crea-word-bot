package by.crearec.telegram.entity.mongo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

@Data
@NoArgsConstructor
public class Word {
	@BsonProperty(value = "_id")
	public ObjectId id;
	@BsonProperty(value = "en")
	public String en;
	@BsonProperty(value = "ru")
	public String ru;
	@BsonProperty(value = "transcription")
	public String transcription;

	public Word(String en, String ru) {
		this.en = en;
		this.ru = ru;
	}

	public Word(String en, String ru, String transcription) {
		this.en = en;
		this.ru = ru;
		this.transcription = transcription;
	}
}
