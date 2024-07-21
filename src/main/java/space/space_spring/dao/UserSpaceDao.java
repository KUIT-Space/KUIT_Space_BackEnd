package space.space_spring.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import space.space_spring.dto.user.GetSpaceInfoForUserResponse;
import space.space_spring.entity.Space;
import space.space_spring.entity.User;
import space.space_spring.entity.UserSpace;

import java.util.ArrayList;
import java.util.List;
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


    public List<GetSpaceInfoForUserResponse> getSpaceNameAndProfileImgList(User userByUserId, int size, Long lastUserSpaceId) {
        String jpql = "SELECT s.spaceName, s.spaceProfileImg " +
                "FROM UserSpace us JOIN us.space s " +
                "WHERE us.user = :user";
        TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
        query.setParameter("user", userByUserId);

        List<Object[]> results = query.getResultList();
        List<GetSpaceInfoForUserResponse> responseList = new ArrayList<>();

        for (Object[] result : results) {
            String spaceName = (String) result[0];
            String spaceImgUrl = (String) result[1];
            responseList.add(new GetSpaceInfoForUserResponse(spaceName, spaceImgUrl));
        }

        return responseList;
    }

}
