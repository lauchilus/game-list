package com.lauchilus.DTO;

import org.springframework.boot.context.properties.bind.DefaultValue;

public record GameResponseDTO(
		Integer id,
		String name,
		@DefaultValue("NO IMAGE")
		String storyline,
		String image,
		@DefaultValue("NO follows")
		Integer follows,
		Integer game_id
		) {

	public GameResponseDTO(GameListData data, String image2,Integer gameId) {
		this(gameId,data.getName(),data.getStoryline(),image2,data.getFollows(),data.getId());
	}

}
