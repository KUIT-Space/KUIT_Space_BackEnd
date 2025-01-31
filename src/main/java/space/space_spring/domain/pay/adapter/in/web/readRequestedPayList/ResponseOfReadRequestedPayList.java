package space.space_spring.domain.pay.adapter.in.web.readRequestedPayList;

import space.space_spring.domain.pay.application.port.in.readRequestedPayList.InfoOfRequestedPay;
import space.space_spring.domain.pay.application.port.in.readRequestedPayList.ResultOfReadRequestedPayList;

import java.util.List;
import java.util.stream.Collectors;

public class ResponseOfReadRequestedPayList {

    private List<ResponseOfRequestedPayInfo> completeRequestedPayList;

    private List<ResponseOfRequestedPayInfo> inCompleteRequestedPayList;

    private ResponseOfReadRequestedPayList(List<InfoOfRequestedPay> completeRequestedPayList, List<InfoOfRequestedPay> inCompleteRequestedPayList) {
        this.completeRequestedPayList = completeRequestedPayList.stream()
                .map(ResponseOfRequestedPayInfo::of)
                .collect(Collectors.toList());

        this.inCompleteRequestedPayList = inCompleteRequestedPayList.stream()
                .map(ResponseOfRequestedPayInfo::of)
                .collect(Collectors.toList());
    }

    public static ResponseOfReadRequestedPayList of(ResultOfReadRequestedPayList result) {
        return new ResponseOfReadRequestedPayList(
                result.getCompleteRequestedPayList(),
                result.getInCompleteRequestedPayList()
        );
    }
}
