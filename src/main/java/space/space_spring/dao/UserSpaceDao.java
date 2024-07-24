package space.space_spring.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import space.space_spring.dto.user.GetSpaceInfoForUserResponse;
import space.space_spring.dto.user.SpaceChoiceViewDto;
import space.space_spring.entity.Space;
import space.space_spring.entity.User;
import space.space_spring.entity.UserSpace;

import java.util.*;

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


    public SpaceChoiceViewDto getSpaceChoiceView(User userByUserId, int size, Long lastUserSpaceId) {

        // 유저가 현재 속해있는 스페이스의 정보만을 return하기 위해 status가 active인 것만 select
        String jpql = "SELECT us.userSpaceId, s.spaceName, s.spaceProfileImg " +
                "FROM UserSpace us JOIN us.space s " +
                "WHERE us.user = :user AND us.status = 'ACTIVE' " +
                "AND us.userSpaceId > :lastUserSpaceId ORDER BY us.userSpaceId ASC";

        TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
        query.setParameter("user", userByUserId);
        query.setParameter("lastUserSpaceId", lastUserSpaceId);
        query.setMaxResults(size);

        List<Object[]> results = query.getResultList();
        List<Map<String, String>> responseList = new ArrayList<>();
        Long newLastUserSpaceId = null;

        for (Object[] result : results) {
            Long userSpaceId = (Long) result[0];
            String spaceName = (String) result[1];
            String spaceProfileImg = (String) result[2];
            Map<String, String> spaceNameAndProfileImgMap = new HashMap<>();
            spaceNameAndProfileImgMap.put("spaceName", spaceName);
            spaceNameAndProfileImgMap.put("spaceProfileImg", spaceProfileImg);
            responseList.add(spaceNameAndProfileImgMap);
            newLastUserSpaceId = userSpaceId;
        }

        // 데이터가 마지막임을 알리기 위해 -1 설정
        if (results.isEmpty()) {
            newLastUserSpaceId = -1L;
        }

        return new SpaceChoiceViewDto(responseList, newLastUserSpaceId);
    }

}
