package com.lauchilus.DTO;

import jakarta.validation.constraints.NotNull;

public record AddPlayedDto(
		@NotNull
		Integer game
		) {

}
