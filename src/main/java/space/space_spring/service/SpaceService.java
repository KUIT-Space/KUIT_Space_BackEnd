package space.space_spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.dao.SpaceDao;
import space.space_spring.dao.UserDao;
import space.space_spring.dao.UserSpaceDao;
import space.space_spring.dto.space.GetSpaceJoinDto;
import space.space_spring.dto.space.response.GetUserInfoBySpaceResponse;
import space.space_spring.dto.userSpace.GetUserProfileInSpaceDto;
import space.space_spring.dto.userSpace.PutUserProfileInSpaceDto;
import space.space_spring.entity.Space;
import space.space_spring.entity.User;
import space.space_spring.entity.UserSpace;
import space.space_spring.exception.UserSpaceException;
import space.space_spring.util.space.SpaceUtils;
import space.space_spring.util.userSpace.UserSpaceUtils;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static space.space_spring.response.status.BaseExceptionResponseStatus.USER_IS_NOT_IN_SPACE;


@Service
@RequiredArgsConstructor
public class SpaceService {

    private final SpaceDao spaceDao;
    private final UserDao userDao;
    private final UserSpaceDao userSpaceDao;
    private final SpaceUtils spaceUtils;
    private final UserSpaceUtils userSpaceUtils;

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

    @Transactional
    public GetSpaceJoinDto.Response findSpaceJoin(Long spaceId) {
        // TODO 1. spaceId로 Space find
        Space spaceBySpaceId = spaceUtils.findSpaceBySpaceId(spaceId);

        // TODO 2. Space 엔티티에서 썸네일 이미지, 이름, 생성일 데이터 get
        GetSpaceJoinDto getSpaceJoinDto = new GetSpaceJoinDto(
                spaceBySpaceId.getSpaceProfileImg(),
                spaceBySpaceId.getSpaceName(),
                spaceBySpaceId.getCreatedAt()
        );

        // TODO 3. 해당 스페이스의 멤버 수 get
        int memberNum = userSpaceDao.calculateSpaceMemberNum(spaceBySpaceId);

        // TODO 4. 스페이스 생성일 형식 'yyyy년 mm월 dd일' 로 변경
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
        String spaceCreatedDate = getSpaceJoinDto.getCreatedAt().format(formatter);

        // TODO 5. return
        return new GetSpaceJoinDto.Response(
                getSpaceJoinDto.getSpaceProfileImg(),
                getSpaceJoinDto.getSpaceName(),
                spaceCreatedDate,
                memberNum
        );
    }

    @Transactional
    public GetUserProfileInSpaceDto.Response getUserProfileInSpace(Long userId, Long spaceId) {
        // TODO 1. userId, spaceId로 UserSpace find
        Optional<UserSpace> userInSpace = userSpaceUtils.isUserInSpace(userId, spaceId);

        // TODO 2. user의 프로필 정보 return
        if (userInSpace.isPresent()) {
            UserSpace userSpace = userInSpace.get();

            return new GetUserProfileInSpaceDto.Response(
                    userSpace.getUserProfileImg(),
                    userSpace.getUserName(),
                    userSpace.getUserSpaceAuth(),
                    userSpace.getUserProfileMsg()
            );
        }

        // userSpaceUtils.isUserInSpace 메서드에서도 해당 에러를 던지기는 하지만
        // 컴파일 에러의 방지를 위해 일단 이중으로 예외를 던지도록 구현했습니다
        throw new UserSpaceException(USER_IS_NOT_IN_SPACE);
    }

    @Transactional
    public void changeUserProfileInSpace(UserSpace userSpace, PutUserProfileInSpaceDto.Request request) {

        // TODO 1. UserSpace의 필드값 수정
        userSpace.
    }
}
