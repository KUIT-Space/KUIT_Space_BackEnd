package space.space_spring.global.common.response;

import lombok.Getter;

@Getter
public class SuccessResponse {

    private final Boolean isSuccess;

    public SuccessResponse(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }
}
