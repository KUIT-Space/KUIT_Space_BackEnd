package space.space_spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.dao.SpaceDao;
import space.space_spring.dao.UserDao;
import space.space_spring.dao.UserSpaceDao;
import space.space_spring.dto.space.PostSpaceCreateResponse;
import space.space_spring.entity.Space;
import space.space_spring.entity.User;
import space.space_spring.dto.space.PostSpaceCreateRequest;
import space.space_spring.entity.UserSpace;

@Service
@RequiredArgsConstructor
public class SpaceService {

    private final SpaceDao spaceDao;
    private final UserDao userDao;
    private final UserSpaceDao userSpaceDao;

    @Transactional
    public PostSpaceCreateResponse createSpace(Long userId, String spaceName, String spaceImgUrl) {

        // TODO 1. 스페이스 생성 정보 db insert
        Space saveSpace = spaceDao.saveSpace(spaceName, spaceImgUrl);

        // TODO 2. 유저_스페이스 매핑 정보 db insert
        User manager = userDao.findUserByUserId(userId);
        UserSpace userSpace = userSpaceDao.createUserSpace(manager, saveSpace);

        return new PostSpaceCreateResponse(saveSpace.getSpaceId(), spaceImgUrl);
    }
}
