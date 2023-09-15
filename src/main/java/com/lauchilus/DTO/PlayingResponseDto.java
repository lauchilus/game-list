package com.lauchilus.DTO;

import java.time.LocalDateTime;

import com.lauchilus.entity.Playing;

public record PlayingResponseDto(
		Integer id,
		String name,
		String image,
		LocalDateTime startDate,
		LocalDateTime finishDate,
		Integer game_id
		) {

	public PlayingResponseDto(Playing data,String image, String name) {
		this(data.getId(),name,image,data.getStartDate(),data.getFinishDate(),data.getGame_id());
	}

}
