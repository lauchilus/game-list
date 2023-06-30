package com.lauchilus.DTO;

import jakarta.validation.constraints.NotBlank;

public record UpdateCollectionDto(
		String name,
		String description,
		Byte[] image
		) {

}
