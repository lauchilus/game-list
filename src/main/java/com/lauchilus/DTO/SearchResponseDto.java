package com.lauchilus.DTO;

import com.lauchilus.igdb.GameData;

public record SearchResponseDto(
		Integer id,
		Double rating,
		CategoryEnum category,
		String collection,
		byte[] cover,
		String name,
		String summary
		
		
		) {



	public SearchResponseDto(GameData data,byte[] cover,String coll) {
		this(data.getId()
		,data.getAggregated_rating()
		,data.getCategory()
		,coll
		,cover
		,data.getName()
		,data.getSummary());
	}

}
