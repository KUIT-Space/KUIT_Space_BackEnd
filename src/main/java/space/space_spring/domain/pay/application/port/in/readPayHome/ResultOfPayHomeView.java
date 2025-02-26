package space.space_spring.domain.pay.application.port.in.readPayHome;

import lombok.Getter;
import space.space_spring.domain.pay.application.port.in.readPayRequestList.InfoOfPayRequest;
import space.space_spring.domain.pay.application.port.in.readRequestedPayList.InfoOfRequestedPay;

import java.util.List;

@Getter
public class ResultOfPayHomeView {

    private List<InfoOfPayRequestInHome> infoOfPayRequestInHomes;
    private List<InfoOfRequestedPayInHome> infoOfRequestedPayInHomes;

    private ResultOfPayHomeView(List<InfoOfPayRequestInHome> infoOfPayRequestInHomes, List<InfoOfRequestedPayInHome> infoOfRequestedPayInHomes) {
        this.infoOfPayRequestInHomes = infoOfPayRequestInHomes;
        this.infoOfRequestedPayInHomes = infoOfRequestedPayInHomes;
    }

    public static ResultOfPayHomeView of(List<InfoOfPayRequestInHome> infoOfPayRequestInHomes, List<InfoOfRequestedPayInHome> infoOfRequestedPayInHomes) {
        return new ResultOfPayHomeView(infoOfPayRequestInHomes, infoOfRequestedPayInHomes);
    }
}
