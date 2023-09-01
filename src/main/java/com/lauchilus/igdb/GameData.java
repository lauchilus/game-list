package com.lauchilus.igdb;

import com.lauchilus.DTO.CategoryEnum;
import com.lauchilus.DTO.CoverGame;

import lombok.Data;

@Data
public class GameData {
	Integer id;
	private Double aggregated_rating;
	private CategoryEnum category;
	private String collection;
	private CoverGame cover;
	private String name;
	private String summary;

}
