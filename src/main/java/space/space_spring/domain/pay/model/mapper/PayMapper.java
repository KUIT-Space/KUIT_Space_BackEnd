package space.space_spring.domain.pay.model.mapper;

import org.springframework.stereotype.Component;
import space.space_spring.domain.pay.model.dto.PayRequestInfoDto;
import space.space_spring.domain.pay.model.firstCollection.PayRequestInfos;
import space.space_spring.domain.pay.model.firstCollection.PayRequests;
import space.space_spring.domain.pay.model.firstCollection.PayTargetInfos;
import space.space_spring.domain.pay.model.response.PayHomeViewResponse;

import java.util.List;

@Component
public class PayMapper {


    public PayHomeViewResponse createPayHomeViewResponse(PayRequestInfos payRequestInfos, PayTargetInfos payTargetInfos) {
        return PayHomeViewResponse.builder()
                .payRequestInfoDtos(payRequestInfos.getAll())
                .payTargetInfoDtos(payTargetInfos.getAll())
                .build();
    }

}
