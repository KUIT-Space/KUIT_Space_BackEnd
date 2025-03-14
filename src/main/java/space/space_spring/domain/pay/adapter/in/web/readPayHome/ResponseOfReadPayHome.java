package space.space_spring.domain.pay.adapter.in.web.readPayHome;

import lombok.Getter;
import space.space_spring.domain.pay.application.port.in.readPayHome.InfoOfPayRequestInHome;
import space.space_spring.domain.pay.application.port.in.readPayHome.InfoOfRequestedPayInHome;
import space.space_spring.domain.pay.application.port.in.readPayHome.ResultOfPayHomeView;

import java.util.List;

@Getter
public class ResponseOfReadPayHome {

    private List<PayRequestInfoInHome> requestInfoInHome;

    private List<RequestedPayInfoInHome> requestedPayInfoInHome;

    private ResponseOfReadPayHome(List<InfoOfPayRequestInHome> infoOfPayRequestInHome, List<InfoOfRequestedPayInHome> infoOfRequestedPayInHome) {
        this.requestInfoInHome = infoOfPayRequestInHome.stream()
                .map(PayRequestInfoInHome::of)
                .toList();
        this.requestedPayInfoInHome = infoOfRequestedPayInHome.stream()
                .map(RequestedPayInfoInHome::of)
                .toList();
    }

    public static ResponseOfReadPayHome of(ResultOfPayHomeView result) {
        return new ResponseOfReadPayHome(
                result.getInfoOfPayRequestInHomes(),
                result.getInfoOfRequestedPayInHomes()
        );
    }
}
