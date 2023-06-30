package com.lauchilus.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import com.lauchilus.DTO.CreateCollectionDTO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="collections")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="id")
public class Collection {

	

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String name;
    private String description;
    @Lob
    private Byte[] image;
    
    @OneToMany(mappedBy = "collection")
    private List<Game> games = new ArrayList<>();
    
    public Collection(@Valid CreateCollectionDTO createCollectionDTO, User user) {
		this.user = user;
		this.name = createCollectionDTO.name();
		this.description = createCollectionDTO.description();
		if(!createCollectionDTO.image().equals(null)) {
		this.image = createCollectionDTO.image();
		}
	}
    
    public void addGame(Game game) {
    	this.games.add(game);
    }
}
