package com.lauchilus.DTO;

import java.util.List;

import com.lauchilus.entity.Collection;

import lombok.Data;

@Data
public class PlayedResponsePage {

	 List<PlayedResponseDto> collections;
     PaginationInfo pagination;
}
