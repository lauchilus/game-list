package com.lauchilus.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddPlayingDto(
		@NotBlank
		String name,
		@NotBlank
		String description,
		@NotNull
		Integer game_id
		) {

}
