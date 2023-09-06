package com.lauchilus.DTO;

import com.lauchilus.entity.Collection;

public record CollectionDataResponse(
		String name,
		String description,
		byte[] image
		) {

	public CollectionDataResponse(Collection collection) {
		this(collection.getName(),collection.getDescription(),collection.getImage());
	}

}
