package com.lauchilus.DTO;

import com.lauchilus.entity.Game;

public record AddGameResponse(
		Integer id,
		Integer game_id,
		Integer collection_id
		) {

	public AddGameResponse(Game game) {
		this(game.getId(),game.getGame_Id(),game.getCollection().getId());
	}
}
