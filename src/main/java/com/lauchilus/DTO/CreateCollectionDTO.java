package com.lauchilus.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCollectionDTO(
		@NotNull
		String user,
		@NotBlank
		String name,
		@NotBlank
		String description,
		String image
		) {

}
