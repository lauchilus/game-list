package com.lauchilus.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddPlayingDto(
		@NotNull
		Integer game_Id
		) {

}
