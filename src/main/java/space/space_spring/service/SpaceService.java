package space.space_spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.dao.SpaceDao;
import space.space_spring.dao.UserDao;
import space.space_spring.dto.jwt.JwtPayloadDto;
import space.space_spring.dto.jwt.JwtUserSpaceAuthDto;
import space.space_spring.dto.space.SpaceCreateDto;
import space.space_spring.entity.Space;
import space.space_spring.entity.User;
import space.space_spring.dto.space.PostSpaceCreateRequest;
import space.space_spring.dto.space.PostSpaceCreateResponse;
import space.space_spring.entity.UserSpace;
import space.space_spring.jwt.JwtUserSpaceProvider;

@Service
@RequiredArgsConstructor
public class SpaceService {

    private final SpaceDao spaceDao;
    private final UserDao userDao;
    private final JwtUserSpaceProvider jwtUserSpaceProvider;

    @Transactional
    public SpaceCreateDto createSpace(Long userId, PostSpaceCreateRequest postSpaceCreateRequest) {

        // TODO 1. 스페이스 생성 정보 db insert
        Space saveSpace = spaceDao.saveSpace(postSpaceCreateRequest);

        // TODO 2. 유저_스페이스 매핑 정보 db insert
        User manager = userDao.findUserByUserId(userId);
        UserSpace userSpace = spaceDao.createUserSpace(manager, saveSpace);

        // TODO 3. jwt에 space 정보 추가
        JwtUserSpaceAuthDto jwtUserSpaceAuthDto = new JwtUserSpaceAuthDto();
        jwtUserSpaceAuthDto.saveUserSpaceAuth(userSpace.getSpace().getSpaceId(), userSpace.getUserSpaceAuth());

        JwtPayloadDto jwtPayloadDto = new JwtPayloadDto();
        jwtPayloadDto.addJwtUserSpaceAuth(jwtUserSpaceAuthDto);

        String jwtUserSpace = jwtUserSpaceProvider.generateToken(jwtPayloadDto);

        return new SpaceCreateDto(saveSpace.getSpaceId(), jwtUserSpace);
    }
}
