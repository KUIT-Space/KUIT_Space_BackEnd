package space.space_spring.domain.board;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Board {

    @Id
    @GeneratedValue
    private Long id;


}
