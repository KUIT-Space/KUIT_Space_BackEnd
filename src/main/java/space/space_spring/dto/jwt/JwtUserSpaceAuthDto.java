package space.space_spring.dto.jwt;

import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Getter
@ToString
public class JwtUserSpaceAuthDto {

    private Map<Long, String> userSpaceAuthMap = new HashMap<>();

    public void saveUserSpaceAuth(Long spaceId, String userSpaceAuth) {
        userSpaceAuthMap.put(spaceId, userSpaceAuth);
    }
}
