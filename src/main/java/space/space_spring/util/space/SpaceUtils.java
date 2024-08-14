package space.space_spring.util.space;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.dao.SpaceDao;
import space.space_spring.entity.Space;
import space.space_spring.exception.CustomException;

import static space.space_spring.response.status.BaseExceptionResponseStatus.SPACE_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class SpaceUtils {

    private final SpaceDao spaceDao;

    @Transactional
    public Space findSpaceBySpaceId(Long spaceId) {
        Space spaceBySpaceId = spaceDao.findSpaceBySpaceId(spaceId);
        if (spaceBySpaceId == null) {
            throw new CustomException(SPACE_NOT_FOUND);
        }
        return spaceBySpaceId;
    }

}
