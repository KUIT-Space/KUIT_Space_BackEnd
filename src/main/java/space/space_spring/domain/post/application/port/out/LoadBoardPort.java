package space.space_spring.domain.post.application.port.out;

import space.space_spring.domain.post.domain.Board;


import java.util.Optional;

import java.util.List;


public interface LoadBoardPort {
    Optional<Board> load(Long boardId);

    Board loadById(Long id);


    List<Board> findAll();

}
