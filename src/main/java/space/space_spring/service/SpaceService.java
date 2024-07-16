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


    }
}
