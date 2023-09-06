package com.lauchilus.DTO;

import java.time.LocalDateTime;

import com.lauchilus.entity.Played;

public record PlayedResponseDto(
		Integer id,
		String name,
		String image,
		LocalDateTime startDate,
		Integer game_id
		) {

	public PlayedResponseDto(Played data,String name,String image) {
		this(data.getId(),name,image,LocalDateTime.now(),data.getGame_id());
	}

}
