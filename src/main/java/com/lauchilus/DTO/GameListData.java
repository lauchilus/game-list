package com.lauchilus.DTO;

import lombok.Data;

@Data
public class GameListData {

	private Integer id;
	private String name;
	private String storyline;
	private CoverGame cover;
	private Integer follows;
}
