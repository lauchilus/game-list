package com.lauchilus.igdb;

import com.lauchilus.DTO.CategoryEnum;
import com.lauchilus.DTO.ScreenshotsDataGame;

import lombok.Data;

@Data
public class GameData {
	Integer id;
	private Double aggregated_rating;
	private CategoryEnum category;
	private String collection;
	private ScreenshotsDataGame[] screenshots;
	private String name;
	private String summary;

}
