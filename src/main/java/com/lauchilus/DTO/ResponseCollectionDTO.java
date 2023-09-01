package com.lauchilus.DTO;

import com.lauchilus.entity.Collection;

public record ResponseCollectionDTO(
		Integer id,
		String name,
		String description,
		Byte[] image

		) {

	public ResponseCollectionDTO(Collection collection) {
		this(collection.getId(),collection.getName(),collection.getDescription(),collection.getImage());
	}
}
