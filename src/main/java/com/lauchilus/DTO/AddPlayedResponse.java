package com.lauchilus.DTO;

import com.lauchilus.entity.Played;

public record AddPlayedResponse(
		Integer id,
		Integer game_Id,
		Integer user_Id
		) {

	public AddPlayedResponse(Played played) {
		this(played.getId(),played.getGame_id(),played.getUser().getId());
	}

}
