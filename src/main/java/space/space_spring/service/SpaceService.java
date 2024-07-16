package space.space_spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.space_spring.dao.SpaceDao;
import space.space_spring.dto.space.PostSpaceCreateRequest;
import space.space_spring.dto.space.PostSpaceCreateResponse;

@Service
@RequiredArgsConstructor
public class SpaceService {

    private final SpaceDao spaceDao;

    public PostSpaceCreateResponse createSpace(Long userId, PostSpaceCreateRequest postSpaceCreateRequest) {

        // TODO 1. 스페이스 생성 정보 db insert
        Long spaceId = spaceDao.saveSpace(postSpaceCreateRequest);

        // TODO 2. 유저_스페이스 매핑 정보 db insert


        // TODO 3. jwt에 space 정보 추가

        return new PostSpaceCreateResponse(spaceId);
    }
}
