package com.lauchilus.DTO;

import com.lauchilus.entity.Game;

public record GameDTO(
		Integer id,
		Integer game_id
		) {

	public GameDTO(Game game) {
		this(game.getId(),game.getGame_Id());
	}
}
