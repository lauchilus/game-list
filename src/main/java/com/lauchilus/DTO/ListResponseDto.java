package com.lauchilus.DTO;

import org.springframework.boot.context.properties.bind.DefaultValue;

public record ListResponseDto(
		Integer id,
		String name,
		@DefaultValue("NO IMAGE")
		String storyline,
		String image,
		@DefaultValue("NO follows")
		Integer follows
		) {

	public ListResponseDto(GameListData data, String image2) {
		this(data.getId(),data.getName(),data.getStoryline(),image2,data.getFollows());
	}

}
