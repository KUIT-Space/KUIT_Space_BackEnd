package space.space_spring.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.user.model.dto.SpaceChoiceInfo;
import space.space_spring.domain.user.model.dto.SpaceChoiceViewDto;
import space.space_spring.dto.userSpace.UserInfoInSpace;
import space.space_spring.entity.Space;
import space.space_spring.domain.user.model.entity.User;
import space.space_spring.entity.UserSpace;
import space.space_spring.entity.enumStatus.UserSpaceAuth;

import java.util.*;

@Repository
public class UserSpaceDao {

    @PersistenceContext
    private EntityManager em;

    public UserSpace createUserSpace(User manager, Space saveSpace, UserSpaceAuth userSpaceAuth) {
        UserSpace userSpace = new UserSpace();
        userSpace.createUserSpace(manager, saveSpace, userSpaceAuth);

        em.persist(userSpace);
        return userSpace;
    }

    public Optional<UserSpace> findUserSpaceByUserAndSpace(User user, Space space) {
        TypedQuery<UserSpace> query = em.createQuery(
                "SELECT us FROM UserSpace us WHERE us.user = :user AND us.space = :space AND us.status = 'ACTIVE'", UserSpace.class);
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

    public List<UserInfoInSpace> findUserInfoInSpace(Space space) {
        String jpql = "SELECT us.user.userId, us.userName, us.userProfileImg, us.userSpaceAuth " +
                "FROM UserSpace us WHERE us.space = :space";
        TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
        query.setParameter("space", space);

        List<Object[]> results = query.getResultList();

        return mapToUserInfoInSpace(results);
    }

    private List<UserInfoInSpace> mapToUserInfoInSpace(List<Object[]> results) {
        List<UserInfoInSpace> userInfoInSpaceList = new ArrayList<>();

        for (Object[] result : results) {
            Long userId = (Long) result[0];
            String userName = (String) result[1];
            String profileImgUrl = (String) result[2];
            String userAuth = (String) result[3];

            UserInfoInSpace userInfoInSpace = new UserInfoInSpace(userId, userName, profileImgUrl, userAuth);
            userInfoInSpaceList.add(userInfoInSpace);
        }

        return userInfoInSpaceList;
    }

    public int calculateSpaceMemberNum(Space space) {
        String jpql = "SELECT COUNT(us) FROM UserSpace us WHERE us.space = :space AND us.status = 'ACTIVE'";
        TypedQuery<Long> query = em.createQuery(jpql, Long.class);
        query.setParameter("space", space);

        return query.getSingleResult().intValue();
    }

    public List<UserSpace> findUserSpaceListByUser(User user) {
        String jpql = "SELECT us FROM UserSpace us WHERE us.user = :user AND us.status = 'ACTIVE'";
        TypedQuery<UserSpace> query = em.createQuery(jpql, UserSpace.class);
        query.setParameter("user", user);

        return query.getResultList();
    }

    @Transactional(readOnly = true)
    public String findUserSpaceAuthById(Long userSpaceId) {
        String jpql = "SELECT us.userSpaceAuth FROM UserSpace us WHERE us.userSpaceId = :userSpaceId";
        return em.createQuery(jpql, String.class)
                .setParameter("userSpaceId", userSpaceId)
                .getSingleResult();

    }

    public Optional<String> findProfileImageById(Long userSpaceId){
        String jpql = "SELECT us.userProfileImg FROM UserSpace us WHERE us.userSpaceId = :userSpaceId AND us.status = 'ACTIVE'";
        List<String> result = em.createQuery(jpql, String.class)
                .setParameter("userSpaceId",userSpaceId)
                .getResultList();
        return result.isEmpty() ? Optional.empty() : Optional.ofNullable(result.get(0));
    }

    public String findUserNameById(Long userSpaceId){
        String jpql = "SELECT us.userName FROM UserSpace us WHERE us.userSpaceId = :userSpaceId AND us.status = 'ACTIVE'";
        return em.createQuery(jpql, String.class)
                .setParameter("userSpaceId", userSpaceId)
                .getSingleResult();

    }
}
