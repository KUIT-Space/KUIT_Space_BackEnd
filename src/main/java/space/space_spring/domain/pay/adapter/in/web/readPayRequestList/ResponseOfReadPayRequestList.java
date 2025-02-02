package space.space_spring.domain.pay.adapter.in.web.readPayRequestList;

import space.space_spring.domain.pay.application.port.in.readPayRequestList.InfoOfPayRequest;
import space.space_spring.domain.pay.application.port.in.readPayRequestList.ResultOfReadPayRequestList;

import java.util.List;
import java.util.stream.Collectors;

public class ResponseOfReadPayRequestList {

    private List<ResponseOfPayRequestInfo> completePayRequestList;

    private List<ResponseOfPayRequestInfo> inCompletePayRequestList;

    private ResponseOfReadPayRequestList(List<InfoOfPayRequest> completePayRequestList, List<InfoOfPayRequest> inCompletePayRequestList) {
        this.completePayRequestList = completePayRequestList.stream()
                .map(ResponseOfPayRequestInfo::of)
                .collect(Collectors.toList());

        this.inCompletePayRequestList = inCompletePayRequestList.stream()
                .map(ResponseOfPayRequestInfo::of)
                .collect(Collectors.toList());
    }

    public static ResponseOfReadPayRequestList of(ResultOfReadPayRequestList result) {
        return new ResponseOfReadPayRequestList(
                result.getCompletePayRequestList(),
                result.getInCompletePayRequestList()
        );
    }
}
