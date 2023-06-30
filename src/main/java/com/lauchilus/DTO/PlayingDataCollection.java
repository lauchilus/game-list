package com.lauchilus.DTO;

import com.lauchilus.entity.Playing;
import com.lauchilus.entity.User;

public record PlayingDataCollection(
		Integer id,
		Integer user,
		String name,
		String Description,
		Byte[] image,
		Integer game_id
		) {

	public PlayingDataCollection(Playing playing) {
		this(playing.getId(), playing.getUser().getId(), playing.getName(), playing.getDescription(), playing.getImage(), playing.getGame_id());
	}
}
