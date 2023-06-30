package com.lauchilus.DTO;

import jakarta.validation.constraints.NotNull;

public record AddGameDto(
		@NotNull
		Integer game_Id
		) {

}
