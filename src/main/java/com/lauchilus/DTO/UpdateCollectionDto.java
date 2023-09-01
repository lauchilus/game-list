package com.lauchilus.DTO;

public record UpdateCollectionDto(
		String name,
		String description,
		Byte[] image
		) {

}
