package com.lauchilus.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRegisterDTO(
		@NotNull
		String username,
		@NotNull
		String password
		
		) {

}
