package by.crearec.telegram.entity;

import by.crearec.telegram.entity.state.ActiveState;
import by.crearec.telegram.entity.state.BaseState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class ActiveUser {

	private Integer userId;
	private Boolean isEnglish;
	private BaseState state;

	public ActiveUser(Integer userId) {
		this.userId = userId;
		this.state = new ActiveState();
		this.isEnglish = true;
	}
}
