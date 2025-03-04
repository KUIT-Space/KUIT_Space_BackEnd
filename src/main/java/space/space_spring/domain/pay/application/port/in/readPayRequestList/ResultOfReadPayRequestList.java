package space.space_spring.domain.pay.application.port.in.readPayRequestList;

import lombok.Getter;

import java.util.List;

@Getter
public class ResultOfReadPayRequestList {

    private List<InfoOfPayRequest> completePayRequestList;

    private List<InfoOfPayRequest> inCompletePayRequestList;

    private ResultOfReadPayRequestList(List<InfoOfPayRequest> completePayRequestList, List<InfoOfPayRequest> inCompletePayRequestList) {
        this.completePayRequestList = completePayRequestList;
        this.inCompletePayRequestList = inCompletePayRequestList;
    }

    public static ResultOfReadPayRequestList of(List<InfoOfPayRequest> completePayRequestList, List<InfoOfPayRequest> inCompletePayRequestList) {
        return new ResultOfReadPayRequestList(completePayRequestList, inCompletePayRequestList);
    }
}
