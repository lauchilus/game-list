package com.lauchilus.DTO;

import java.time.LocalDateTime;

import com.lauchilus.entity.Played;

public record AddPlayedResponse(
		Integer id,
		Integer game_Id,
		String image,
		LocalDateTime startDate
		) {

	public AddPlayedResponse(Played played,String image) {
		this(played.getId(),played.getGame_id(),image,played.getStartDate());
	}

}
