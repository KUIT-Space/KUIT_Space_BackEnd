package space.space_spring.domain.pay.application.port.in.readRequestedPayList;

import lombok.Getter;

import java.util.List;

@Getter
public class ResultOfReadRequestedPayList {

    private List<InfoOfRequestedPay> completeRequestedPayList;

    private List<InfoOfRequestedPay> inCompleteRequestedPayList;

    private ResultOfReadRequestedPayList(List<InfoOfRequestedPay> completeRequestedPayList, List<InfoOfRequestedPay> inCompleteRequestedPayList) {
        this.completeRequestedPayList = completeRequestedPayList;
        this.inCompleteRequestedPayList = inCompleteRequestedPayList;
    }

    public static ResultOfReadRequestedPayList of(List<InfoOfRequestedPay> completeRequestedPayList, List<InfoOfRequestedPay> inCompleteRequestedPayList) {
        return new ResultOfReadRequestedPayList(completeRequestedPayList, inCompleteRequestedPayList);
    }
}
