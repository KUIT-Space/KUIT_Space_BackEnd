package space.space_spring.domain.space.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import space.space_spring.domain.pay.model.dto.PayTargetInfoDto;
import space.space_spring.domain.pay.model.dto.PayRequestInfoDto;
import java.util.List;

public class GetSpaceHomeDto {

    @Getter
    @AllArgsConstructor
    public static class Response {

        private String spaceName;

        private String spaceProfileImg;

        private List<PayRequestInfoDto> payRequestInfoDtoList;

        private List<PayTargetInfoDto> payReceiveInfoDtoList;

        private List<SpaceHomeNotice> noticeList;

        private int memberNum;

        private String userAuth;
    }

    @Getter
    @AllArgsConstructor
    public static class SpaceHomeNotice {

        private Long postId;

        private String title;
    }

    @Getter
    @AllArgsConstructor
    public static class SpaceInfoForHome {

        private String spaceName;

        private String spaceProfileImg;

        private int memberNum;
    }
}
