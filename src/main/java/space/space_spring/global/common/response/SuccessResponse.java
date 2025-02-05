package space.space_spring.global.common.response;

import lombok.Getter;

@Getter
public class SuccessResponse {

    private final boolean isSuccess;

    public SuccessResponse(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }
}
