package space.space_spring.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import space.space_spring.entity.Space;
import space.space_spring.entity.User;
import space.space_spring.entity.UserSpace;
import space.space_spring.dto.space.PostSpaceCreateRequest;

import static space.space_spring.entity.enumStatus.UserSpaceAuth.MANAGER;

@Repository
public class SpaceDao {

    @PersistenceContext
    private EntityManager em;

    public Space saveSpace(PostSpaceCreateRequest postSpaceCreateRequest) {
        Space space = new Space();
        space.saveSpace(postSpaceCreateRequest.getSpaceName(), postSpaceCreateRequest.getSpaceProfileImg());

        em.persist(space);
        return space;
    }

    public Space findSpaceBySpaceId(Long spaceId) {
        return em.find(Space.class, spaceId);
    }

}
