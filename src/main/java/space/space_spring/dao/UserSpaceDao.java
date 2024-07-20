package space.space_spring.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import space.space_spring.entity.Space;
import space.space_spring.entity.User;
import space.space_spring.entity.UserSpace;

import java.util.Optional;

import static space.space_spring.entity.enumStatus.UserSpaceAuth.MANAGER;

@Repository
public class UserSpaceDao {

    @PersistenceContext
    private EntityManager em;

    public UserSpace createUserSpace(User manager, Space saveSpace) {
        UserSpace userSpace = new UserSpace();
        userSpace.createUserSpace(manager, saveSpace, MANAGER);

        em.persist(userSpace);
        return userSpace;
    }

    public Optional<UserSpace> findUserSpaceByUserAndSpace(User user, Space space) {
        TypedQuery<UserSpace> query = em.createQuery(
                "SELECT us FROM UserSpace us WHERE us.user = :user AND us.space = :space", UserSpace.class);
        query.setParameter("user", user);
        query.setParameter("space", space);

        return query.getResultList().stream().findFirst();
    }
}
