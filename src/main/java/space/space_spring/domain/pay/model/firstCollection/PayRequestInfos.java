package space.space_spring.domain.pay.model.firstCollection;

import space.space_spring.domain.pay.model.dto.PayRequestInfoDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PayRequestInfos {

    private final List<PayRequestInfoDto> payRequestInfos = new ArrayList<>();

    public void add(PayRequestInfoDto payRequestInfoDto) {
        payRequestInfos.add(payRequestInfoDto);
    }

    public List<PayRequestInfoDto> getAll() {
        // getAll 메서드가 반환하는 리스트는 '읽기 전용'
        return Collections.unmodifiableList(payRequestInfos);
    }
}
