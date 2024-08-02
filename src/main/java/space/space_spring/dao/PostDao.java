package space.space_spring.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import space.space_spring.entity.Post;
import space.space_spring.entity.Space;

import java.util.List;

@Repository
public interface PostDao extends JpaRepository<Post, Long> {

    List<Post> findBySpace(Space space);

}
