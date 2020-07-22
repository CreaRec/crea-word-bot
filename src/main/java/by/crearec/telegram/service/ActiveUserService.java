package by.crearec.telegram.service;

import by.crearec.telegram.entity.ActiveUser;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

public final class ActiveUserService {

	@Getter
	private final Set<ActiveUser> activeUsers = new HashSet<>();

	public ActiveUser getUser(Integer userId) {
		return activeUsers.stream().filter(a -> a.getUserId().equals(userId)).findFirst().orElse(null);
	}

	public boolean removeUser(Integer userId) {
		return activeUsers.removeIf(a -> a.getUserId().equals(userId));
	}

	public boolean addUser(ActiveUser activeUser) {
		return activeUsers.add(activeUser);
	}

	public boolean hasUser(Integer userId) {
		return activeUsers.stream().anyMatch(a -> a.getUserId().equals(userId));
	}
}