package space.space_spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.dao.SpaceDao;
import space.space_spring.dao.UserDao;
import space.space_spring.dao.UserSpaceDao;
import space.space_spring.dto.space.GetSpaceJoinDto;
import space.space_spring.dto.space.response.GetUserInfoBySpaceResponse;
import space.space_spring.entity.Space;
import space.space_spring.entity.User;
import space.space_spring.entity.UserSpace;
import space.space_spring.util.space.SpaceUtils;

import java.time.format.DateTimeFormatter;


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
}
