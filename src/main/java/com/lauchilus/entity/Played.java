package com.lauchilus.entity;

import java.time.LocalDateTime;

import com.lauchilus.DTO.AddPlayedDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "played")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Played {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "user_id")
	private User user;
	private Integer game_id;

	// TODO create variables for image and date started and finish update table in
	// database

	public Played(User user, @Valid AddPlayedDto playedDto) {
		this.user = user;
		this.game_id = playedDto.game();
	}

	public Played(User user, Integer game_id) {
		this.user = user;
		this.game_id = game_id;
	}
	
	
}
