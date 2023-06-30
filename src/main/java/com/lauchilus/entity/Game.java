package com.lauchilus.entity;

import com.lauchilus.DTO.AddGameDto;

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
@Table(name="games")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="id")
public class Game {

	

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	private Integer game_Id;

    @ManyToOne
    @JoinColumn(name = "collection_id")
    private Collection collection;
    
    public Game(@Valid AddGameDto addGamedto, Collection collection) {
		this.game_Id = addGamedto.game_Id();
		this.collection = collection;
	}
    
}
