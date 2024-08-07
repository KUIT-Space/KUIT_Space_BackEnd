package space.space_spring.dto.pay.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostPayCreateRequest {

    /**
     * PayRequest 엔티티 생성 시 필요한 정보
     */
    private int totalAmount;

    private String bankName;

    private String bankAccountNum;

    /**
     * PayRequestTarget 엔티티 생성 시 필요한 정보
     * <targetUserId requestAmount> 쌍
     */
    private List<TargetInfo> targetInfoList = new ArrayList<>();

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TargetInfo {
        private Long targetUserId;
        private int requestAmount;
    }
}
