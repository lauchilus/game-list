package com.lauchilus.DTO;

import com.lauchilus.entity.Playing;

public record PlayingDataCollection(
		Integer id,
		String name,
		Byte[] image,
		Integer game_id
		) {

//	public PlayingDataCollection(Playing playing){
//		this(playing.getId(),playing.getName(),playing.getImage(),playing.getGame_id());
//	}


}
