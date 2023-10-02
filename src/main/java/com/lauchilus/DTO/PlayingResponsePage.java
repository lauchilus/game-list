package com.lauchilus.DTO;

import java.util.List;

import com.lauchilus.entity.Collection;

import lombok.Data;

@Data
public class PlayingResponsePage {

	 List<PlayingResponseDto> collections;
     PaginationInfo pagination;
}
