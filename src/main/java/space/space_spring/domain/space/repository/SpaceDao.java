package space.space_spring.domain.space.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.space.model.entity.Space;

@Repository
public class SpaceDao {

    @PersistenceContext
    private EntityManager em;

    public Space saveSpace(String spaceName, String spaceImgUrl) {
        Space space = Space.create(spaceName, spaceImgUrl);

        em.persist(space);
        return space;
    }

    public Space findSpaceBySpaceId(Long spaceId) {
        return em.find(Space.class, spaceId);
    }
}
