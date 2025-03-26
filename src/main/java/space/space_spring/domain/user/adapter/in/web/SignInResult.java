package space.space_spring.domain.user.adapter.in.web;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignInResult {

    private TokenPair tokenPair;

    private Boolean isSuccess;

    private List<SpaceInfo> spaceInfos;

    public static SignInResult createSuccess(TokenPair tokenPair, List<SpaceInfo> spaceInfos) {
        return new SignInResult(tokenPair, true, spaceInfos);
    }

    public static SignInResult createFail() {
        return new SignInResult(null, false, List.of());
    }

    public boolean isSignInFail() {
        return this.tokenPair == null;
    }

}
