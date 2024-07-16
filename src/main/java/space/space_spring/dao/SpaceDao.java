package space.space_spring.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.Space;
import space.space_spring.dto.space.PostSpaceCreateRequest;

@Repository
public class SpaceDao {

    @PersistenceContext
    private EntityManager em;

    public Long saveSpace(PostSpaceCreateRequest postSpaceCreateRequest) {
        Space space = new Space();
        space.saveSpace(postSpaceCreateRequest.getSpaceName(), postSpaceCreateRequest.getSpaceProfileImg());

        em.persist(space);
        return space.getSpaceId();
    }
}
