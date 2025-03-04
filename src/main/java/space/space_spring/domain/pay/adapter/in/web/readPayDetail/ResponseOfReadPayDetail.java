package space.space_spring.domain.pay.adapter.in.web.readPayDetail;

import lombok.Builder;
import space.space_spring.domain.pay.application.port.in.readPayDetail.ResultOfReadPayDetail;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ResponseOfReadPayDetail {

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private Long payRequestId;

    private int totalAmount;

    private int receivedAmount;

    private int totalTargetNum;

    private int sendCompleteTargetNum;

    private String bankName;

    private String bankAccountNum;

    private String createdDate;

    private List<ResponseOfTargetDetail> responseOfTargetDetails;

    @Builder
    private ResponseOfReadPayDetail(Long payRequestId, int totalAmount, int receivedAmount, int totalTargetNum, int sendCompleteTargetNum,
                                    String bankName, String bankAccountNum, String createdDate,
                                    List<ResponseOfTargetDetail> responseOfTargetDetails) {
        this.payRequestId = payRequestId;
        this.totalAmount = totalAmount;
        this.receivedAmount = receivedAmount;
        this.totalTargetNum = totalTargetNum;
        this.sendCompleteTargetNum = sendCompleteTargetNum;
        this.bankName = bankName;
        this.bankAccountNum = bankAccountNum;
        this.createdDate = createdDate;
        this.responseOfTargetDetails = responseOfTargetDetails;
    }

    public static ResponseOfReadPayDetail of(ResultOfReadPayDetail result) {
        return ResponseOfReadPayDetail.builder()
                .payRequestId(result.getPayRequestId())
                .totalAmount(result.getTotalAmount().getAmountInInteger())
                .receivedAmount(result.getReceivedAmount().getAmountInInteger())
                .totalTargetNum(result.getTotalTargetNum().getNumber())
                .sendCompleteTargetNum(result.getSendCompleteTargetNum().getNumber())
                .bankName(result.getBank().getName())
                .bankAccountNum(result.getBank().getAccountNumber())
                .createdDate(result.getCreateDate().format(DateTimeFormatter.ofPattern(DATE_FORMAT)))
                .responseOfTargetDetails(
                        result.getInfoOfTargetDetails().stream()
                            .map(ResponseOfTargetDetail::of)
                            .toList())
                .build();
    }
}
