package com.lauchilus.helpers;

import com.lauchilus.entity.User;

import lombok.Data;

@Data
public class GameMapperPlayed {
	private Integer id;
	private User user;
	private Integer game_id;
	private String review;
	private Integer rating;

}
