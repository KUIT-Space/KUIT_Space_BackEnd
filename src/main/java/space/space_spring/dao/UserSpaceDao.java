package space.space_spring.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import space.space_spring.dto.user.GetSpaceInfoForUserResponse;
import space.space_spring.dto.user.SpaceChoiceInfo;
import space.space_spring.dto.user.SpaceChoiceViewDto;
import space.space_spring.dto.userSpace.UserProfileImgAndNameDto;
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
        String jpql = "SELECT us.userSpaceId, s.spaceId, s.spaceName, s.spaceProfileImg " +
                "FROM UserSpace us JOIN us.space s " +
                "WHERE us.user = :user AND us.status = 'ACTIVE' " +
                "AND us.userSpaceId > :lastUserSpaceId ORDER BY us.userSpaceId ASC";

        TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
        query.setParameter("user", userByUserId);
        query.setParameter("lastUserSpaceId", lastUserSpaceId);
        query.setMaxResults(size);

        List<Object[]> results = query.getResultList();

        List<SpaceChoiceInfo> spaceChoiceInfoList = mapToSpaceChoiceInfoList(results);

        Long newLastUserSpaceId = determineLastUserSpaceId(results);

        return new SpaceChoiceViewDto(spaceChoiceInfoList, newLastUserSpaceId);
    }

    private Long determineLastUserSpaceId(List<Object[]> results) {
        if (results.isEmpty()) {
            return -1L;             // 더 이상 조회할 데이터가 없음을 표시
        }
        // results가 비어있지 않다면 마지막 userSpaceId를 반환
        return (Long) results.get(results.size() - 1)[0];
    }

    private List<SpaceChoiceInfo> mapToSpaceChoiceInfoList(List<Object[]> results) {
        List<SpaceChoiceInfo> spaceChoiceInfoList = new ArrayList<>();
        for (Object[] result : results) {
            Long spaceId = (Long) result[1];
            String spaceName = (String) result[2];
            String spaceProfileImg = (String) result[3];

            SpaceChoiceInfo spaceChoiceInfo = new SpaceChoiceInfo(spaceId, spaceName, spaceProfileImg);
            spaceChoiceInfoList.add(spaceChoiceInfo);
        }
        return spaceChoiceInfoList;
    }

    public List<UserProfileImgAndNameDto> findUserProfileImgAndName(Space space) {
        String jpql = "SELECT us.userName, us.userProfileImg FROM UserSpace us WHERE us.space = :space";
        TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
        query.setParameter("space", space);

        List<Object[]> results = query.getResultList();
        List<UserProfileImgAndNameDto> responseList = new ArrayList<>();
        for (Object[] result : results) {
            String userName = (String) result[0];
            String profileImg = (String) result[1];
            responseList.add(new UserProfileImgAndNameDto(userName, profileImg));
        }

        return responseList;
    }
}
