package space.space_spring.dto.jwt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@ToString
public class JwtPayloadDto {

    private Long userId;

    private List<JwtUserSpaceAuthDto> userSpaceList = new ArrayList<>();

    public void saveUserIdToJwt(Long userId) {
        this.userId = userId;
    }

    public void saveUserSpaceList(List<JwtUserSpaceAuthDto> userSpaceList) {
        this.userSpaceList = userSpaceList;
    }

    public void addJwtUserSpaceAuth(JwtUserSpaceAuthDto jwtUserSpaceAuthDto) {
        this.userSpaceList.add(jwtUserSpaceAuthDto);
    }
}
