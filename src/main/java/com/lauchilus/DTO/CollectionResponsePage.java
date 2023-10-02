package com.lauchilus.DTO;

import java.util.List;

import com.lauchilus.entity.Collection;

import lombok.Data;

@Data
public class CollectionResponsePage {

	 List<ResponseCollectionDTO> collections;
     PaginationInfo pagination;
}
