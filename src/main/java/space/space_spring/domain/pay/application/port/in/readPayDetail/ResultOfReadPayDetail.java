package space.space_spring.domain.pay.application.port.in.readPayDetail;

import lombok.Builder;
import lombok.Getter;
import space.space_spring.domain.pay.domain.Bank;
import space.space_spring.domain.pay.domain.Money;
import space.space_spring.global.util.NaturalNumber;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ResultOfReadPayDetail {

    private Long payRequestId;

    private Money totalAmount;

    private Money receivedAmount;

    private NaturalNumber totalTargetNum;

    private NaturalNumber sendCompleteTargetNum;

    private Bank bank;

    private LocalDateTime createDate;

    private List<InfoOfTargetDetail> infoOfTargetDetails;

    @Builder
    public ResultOfReadPayDetail(Long payRequestId, Money totalAmount, Money receivedAmount, NaturalNumber totalTargetNum, NaturalNumber sendCompleteTargetNum,
                                 Bank bank, LocalDateTime createDate, List<InfoOfTargetDetail> infoOfTargetDetails) {
        this.payRequestId = payRequestId;
        this.totalAmount = totalAmount;
        this.receivedAmount = receivedAmount;
        this.totalTargetNum = totalTargetNum;
        this.sendCompleteTargetNum = sendCompleteTargetNum;
        this.bank = bank;
        this.createDate = createDate;
        this.infoOfTargetDetails = infoOfTargetDetails;
    }
}
