package space.space_spring.domain.user.adapter.in.web;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import space.space_spring.domain.space.domain.Space;

@Getter
public class OauthLoginResponse {

    private boolean isSuccess;

    private List<SpaceInfo> spaceInfos;

    public OauthLoginResponse(boolean isSuccess, List<Space> spaces) {
        this.isSuccess = isSuccess;
        this.spaceInfos = new ArrayList<>();
        for (Space space : spaces) {
            spaceInfos.add(SpaceInfo.create(space));
        }
    }
}
