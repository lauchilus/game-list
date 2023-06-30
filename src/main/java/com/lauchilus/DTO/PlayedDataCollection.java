package com.lauchilus.DTO;

import com.lauchilus.entity.Played;
import com.lauchilus.entity.User;

public record PlayedDataCollection(
		Integer id,
		User user,
		Integer game_id
		) {

	public PlayedDataCollection(Played played) {
		this(played.getId(),played.getUser(),played.getGame_id());
	}
	
}
