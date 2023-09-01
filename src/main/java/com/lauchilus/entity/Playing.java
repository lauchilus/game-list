package com.lauchilus.entity;

import java.time.LocalDateTime;

import com.lauchilus.DTO.AddPlayingDto;
import com.lauchilus.DTO.CoverGame;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="playing")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="id")
public class Playing {


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private User user;
	private byte[] image;

    private String name;
    private String description;

    private LocalDateTime startDate = LocalDateTime.now();
    private LocalDateTime finishDate = null;
    private Integer game_id;

    public Playing(@Valid AddPlayingDto playingDto, User user) {
		this.user = user;
		this.name = playingDto.name();
		this.description = playingDto.description();
		this.game_id = playingDto.game_id();
	}

	public void updateFinishDate() {
		this.finishDate = LocalDateTime.now();
	}


}
