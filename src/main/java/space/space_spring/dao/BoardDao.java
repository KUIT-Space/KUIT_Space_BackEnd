package space.space_spring.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import space.space_spring.entity.Board;
import space.space_spring.entity.Space;
import space.space_spring.entity.User;

import java.util.List;

@Repository
public interface BoardDao extends JpaRepository<Board, Long> {

    List<Board> findBySpace(Space space);

}
