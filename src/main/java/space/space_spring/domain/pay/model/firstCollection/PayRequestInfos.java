package space.space_spring.domain.pay.model.firstCollection;

import space.space_spring.domain.pay.model.dto.PayRequestInfoDto;

import java.util.ArrayList;
import java.util.List;

public class PayRequestInfos {

    private final List<PayRequestInfoDto> payRequestInfos = new ArrayList<>();

    public void add(PayRequestInfoDto payRequestInfoDto) {
        payRequestInfos.add(payRequestInfoDto);
    }

    public List<PayRequestInfoDto> getAll() {
        return payRequestInfos;
    }
}
