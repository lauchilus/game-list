package com.lauchilus.DTO;

import lombok.Data;

@Data
public class PaginationInfo {

	
	private int totalPages;
    private long totalElements;
    
    public PaginationInfo(int totalPages2, long totalElements2) {
		this.totalPages = totalPages2;
		this.totalElements = totalElements2;
	}
}
