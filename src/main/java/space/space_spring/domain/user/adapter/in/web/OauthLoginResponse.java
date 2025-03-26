package space.space_spring.domain.user.adapter.in.web;

import java.util.List;
import lombok.Getter;

@Getter
public class OauthLoginResponse {

    private Boolean isSuccess;

    private List<SpaceInfo> spaceInfos;

    public OauthLoginResponse(boolean isSuccess, List<SpaceInfo> spaceInfos) {
        this.isSuccess = isSuccess;
        this.spaceInfos = spaceInfos;
    }
}
