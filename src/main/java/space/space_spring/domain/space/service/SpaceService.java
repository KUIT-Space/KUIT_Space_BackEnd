package space.space_spring.domain.space.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.space.repository.SpaceDao;
import space.space_spring.domain.user.repository.UserDao;
import space.space_spring.domain.userSpace.repository.UserSpaceDao;
import space.space_spring.domain.space.model.dto.GetSpaceHomeDto;
import space.space_spring.domain.space.model.dto.GetSpaceJoinDto;
import space.space_spring.domain.space.model.dto.PostSpaceJoinDto;
import space.space_spring.domain.space.model.response.GetUserInfoBySpaceResponse;
import space.space_spring.domain.userSpace.model.GetUserProfileInSpaceDto;
import space.space_spring.domain.userSpace.model.PutUserProfileInSpaceDto;
import space.space_spring.domain.space.model.entity.Space;
import space.space_spring.domain.user.model.entity.User;
import space.space_spring.domain.userSpace.model.entity.UserSpace;
import space.space_spring.global.common.enumStatus.UserSpaceAuth;
import space.space_spring.global.exception.CustomException;
import space.space_spring.global.util.space.SpaceUtils;
import space.space_spring.global.util.user.UserUtils;
import space.space_spring.global.util.userSpace.UserSpaceUtils;

import java.time.ZoneId;
import java.util.Optional;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.USER_IS_NOT_IN_SPACE;


@Service
@RequiredArgsConstructor
public class SpaceService {

    private final SpaceDao spaceDao;
    private final UserDao userDao;
    private final UserSpaceDao userSpaceDao;
    private final SpaceUtils spaceUtils;
    private final UserSpaceUtils userSpaceUtils;
    private final UserUtils userUtils;

    @Transactional
    public Space createSpace(Long userId, String spaceName, String spaceImgUrl) {

        // TODO 1. 스페이스 생성 정보 db insert
        Space saveSpace = spaceDao.saveSpace(spaceName, spaceImgUrl);

        // TODO 2. 유저_스페이스 매핑 정보 db insert
        User manager = userDao.findUserByUserId(userId);
        UserSpace userSpace = userSpaceDao.createUserSpace(manager, saveSpace, UserSpaceAuth.MANAGER);

        return saveSpace;
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

        // TODO 4. return
        return new GetSpaceJoinDto.Response(
                getSpaceJoinDto.getSpaceProfileImg(),
                getSpaceJoinDto.getSpaceName(),
                spaceBySpaceId.getCreatedAt().atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime(),
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
        throw new CustomException(USER_IS_NOT_IN_SPACE);
    }

    @Transactional
    public PutUserProfileInSpaceDto.Response changeUserProfileInSpace(Long userId, Long spaceId, PutUserProfileInSpaceDto putUserProfileInSpaceDto) {

        // TODO 1. userId, spaceId로 UserSpace find
        Optional<UserSpace> userInSpace = userSpaceUtils.isUserInSpace(userId, spaceId);

        if (userInSpace.isPresent()) {
            UserSpace userSpace = userInSpace.get();

            // TODO 2. UserSpace의 필드값 수정
            userSpace.changeUserName(putUserProfileInSpaceDto.getUserName());
            userSpace.changeUserProfileImg(putUserProfileInSpaceDto.getUserProfileImg());
            userSpace.changeUserProfileMsg(putUserProfileInSpaceDto.getUserProfileMsg());

            // TODO 3. return
            return new PutUserProfileInSpaceDto.Response(
                    userSpace.getUserProfileImg(),
                    userSpace.getUserSpaceAuth(),
                    userSpace.getUserName(),
                    userSpace.getUserProfileMsg()
            );
        }

        // userSpaceUtils.isUserInSpace 메서드에서도 해당 에러를 던지기는 하지만
        // 컴파일 에러의 방지를 위해 일단 이중으로 예외를 던지도록 구현했습니다
        throw new CustomException(USER_IS_NOT_IN_SPACE);

    }

    @Transactional
    public void createUserSpace(Long userId, Long spaceId, PostSpaceJoinDto postSpaceJoinDto) {

        // TODO 1. userId에 해당하는 User find
        User userByUserId = userUtils.findUserByUserId(userId);

        // TODO 2. spaceId에 해당하는 Space find
        Space spaceBySpaceId = spaceUtils.findSpaceBySpaceId(spaceId);

        // TODO 3. 유저_스페이스 매핑 정보 db insert
        UserSpace userSpace = userSpaceDao.createUserSpace(userByUserId, spaceBySpaceId, UserSpaceAuth.NORMAL);

        userSpace.changeUserProfileImg(postSpaceJoinDto.getUserProfileImg());
        userSpace.changeUserName(postSpaceJoinDto.getUserName());
        userSpace.changeUserProfileMsg(postSpaceJoinDto.getUserProfileMsg());
    }

    public GetSpaceHomeDto.SpaceInfoForHome getSpaceInfoForHome(Long spaceId) {

        // TODO 1. spaceId로 Space find
        Space spaceBySpaceId = spaceUtils.findSpaceBySpaceId(spaceId);

        // TODO 2. Space의 이름, 프로필 이미지, 멤버 수 get
        String spaceName = spaceBySpaceId.getSpaceName();
        String spaceProfileImg = spaceBySpaceId.getSpaceProfileImg();
        int memberNum = userSpaceDao.calculateSpaceMemberNum(spaceBySpaceId);

        // TODO 3. return
        return new GetSpaceHomeDto.SpaceInfoForHome(
                spaceName,
                spaceProfileImg,
                memberNum
        );
    }

}
