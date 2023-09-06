package com.lauchilus.DTO;

import com.lauchilus.entity.Collection;

public record ResponseCollectionDTO(
		Integer id,
		String name,
		String description,
		String image

		) {

	public ResponseCollectionDTO(Collection collection,String image) {
		this(collection.getId(),collection.getName(),collection.getDescription(),image);
	}
}
