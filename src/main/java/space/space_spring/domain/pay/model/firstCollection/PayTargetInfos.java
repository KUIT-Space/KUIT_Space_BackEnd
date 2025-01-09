package space.space_spring.domain.pay.model.firstCollection;

import space.space_spring.domain.pay.model.dto.PayTargetInfoDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PayTargetInfos {

    private final List<PayTargetInfoDto> payTargetInfoDtos = new ArrayList<>();

    public void add(PayTargetInfoDto payTargetInfoDto) {
        payTargetInfoDtos.add(payTargetInfoDto);
    }

    public List<PayTargetInfoDto> getAll() {
        // getAll 메서드가 반환하는 리스트는 '읽기 전용'
        return Collections.unmodifiableList(payTargetInfoDtos);
    }
}
