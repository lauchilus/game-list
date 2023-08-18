package com.lauchilus.DTO;

public record ListResponseDto(
		Integer id,
		String name,
		String storyline,
		byte[] image,
		Integer follows
		) {

	public ListResponseDto(GameListData data, byte[] image2) {
		this(data.getId(),data.getName(),data.getStoryline(),image2,data.getFollows());
	}

}
