package space.space_spring.domain.post.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataBoardRepository extends JpaRepository<BoardJpaEntity, Long> {
}
