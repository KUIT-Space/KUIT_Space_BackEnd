package space.space_spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.dao.SpaceDao;
import space.space_spring.dao.UserDao;
import space.space_spring.dao.UserSpaceDao;
import space.space_spring.dto.space.response.GetUserInfoBySpaceResponse;
import space.space_spring.entity.Space;
import space.space_spring.entity.User;
import space.space_spring.entity.UserSpace;
import space.space_spring.util.space.SpaceUtils;


@Service
@RequiredArgsConstructor
public class SpaceService {

    private final SpaceDao spaceDao;
    private final UserDao userDao;
    private final UserSpaceDao userSpaceDao;
    private final SpaceUtils spaceUtils;

    @Transactional
    public Long createSpace(Long userId, String spaceName, String spaceImgUrl) {

        // TODO 1. 스페이스 생성 정보 db insert
        Space saveSpace = spaceDao.saveSpace(spaceName, spaceImgUrl);

        // TODO 2. 유저_스페이스 매핑 정보 db insert
        User manager = userDao.findUserByUserId(userId);
        UserSpace userSpace = userSpaceDao.createUserSpace(manager, saveSpace);

        return saveSpace.getSpaceId();
    }

    @Transactional
    public GetUserInfoBySpaceResponse findUserInfoBySpace(Long spaceId) {
        // TODO 1. spaceId로 Space 찾기
        Space spaceBySpaceId = spaceUtils.findSpaceBySpaceId(spaceId);

        // TODO 2. 스페이스의 모든 유저 정보 return
        return new GetUserInfoBySpaceResponse(userSpaceDao.findUserInfoInSpace(spaceBySpaceId));
    }
}
