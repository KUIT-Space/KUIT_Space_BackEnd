package space.space_spring.domain.pay.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import space.space_spring.domain.pay.application.port.in.readRequestedPayList.ResultOfReadRequestedPayList;
import space.space_spring.domain.pay.application.port.out.LoadPayRequestPort;
import space.space_spring.domain.pay.application.port.out.LoadPayRequestTargetPort;
import space.space_spring.domain.pay.domain.*;
import space.space_spring.domain.spaceMember.LoadSpaceMemberInfoPort;
import space.space_spring.domain.spaceMember.NicknameAndProfileImage;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ReadRequestedPayListServiceTest {

    private LoadPayRequestTargetPort loadPayRequestTargetPort;
    private LoadPayRequestPort loadPayRequestPort;
    private LoadSpaceMemberInfoPort spaceMemberInfoPort;
    private ReadRequestedPayListService readRequestedPayListService;

    private Long seongjunId;
    private Long sangjunId;
    private Long seohyunId;
    private Long kyungminId;

    @BeforeEach
    void setUp() {
        loadPayRequestTargetPort = Mockito.mock(LoadPayRequestTargetPort.class);
        loadPayRequestPort = Mockito.mock(LoadPayRequestPort.class);
        spaceMemberInfoPort = Mockito.mock(LoadSpaceMemberInfoPort.class);
        readRequestedPayListService = new ReadRequestedPayListService(loadPayRequestTargetPort, loadPayRequestPort, spaceMemberInfoPort);

        seongjunId = 1L;
        sangjunId = 2L;
        seohyunId = 3L;
        kyungminId = 4L;
    }

    @Test
    @DisplayName("특정 유저가 요청받은 모든 정산의 [payRequestTargetId, 정산 생성자 닉네임, 정산 생성자 프로필 이미지 url, 요청받은 금액, 송금할 은행] 정보를 송금완료한 정산, 아직 송금하지 않은 정산으로 구분해서 반환한다.")
    void readRequestedPayList1() throws Exception {
        //given
        PayRequestTarget payRequestTarget1 = PayRequestTarget.create(1L, seongjunId, 1L, Money.of(3333));
        PayRequestTarget payRequestTarget2 = PayRequestTarget.of(2L, seongjunId, 2L, Money.of(10000), true);

        Mockito.when(loadPayRequestTargetPort.loadByTargetMemberId(seongjunId)).thenReturn(List.of(payRequestTarget1, payRequestTarget2));

        stubLoadPayRequest(1L, seohyunId, Bank.of("우리은행", "111-111"));
        stubLoadPayRequest(2L, kyungminId, Bank.of("신한은행", "222-222"));

        stubLoadNicknameAndProfileImage(seohyunId, "정서현", "정서현_프로필_이미지");
        stubLoadNicknameAndProfileImage(kyungminId, "김경민", "김경민_프로필_이미지");

        //when
        ResultOfReadRequestedPayList result = readRequestedPayListService.readRequestedPayList(seongjunId);

        //then
        assertThat(result.getCompleteRequestedPayList()).hasSize(1)
                .extracting(
                        "payRequestTargetId", "payCreatorNickname", "payCreatorProfileImageUrl", "requestedAmount", "bank"
                )
                .containsExactlyInAnyOrder(
                        tuple(2L, "김경민", "김경민_프로필_이미지", Money.of(10000), Bank.of("신한은행", "222-222"))
                );

        assertThat(result.getInCompleteRequestedPayList()).hasSize(1)
                .extracting(
                        "payRequestTargetId", "payCreatorNickname", "payCreatorProfileImageUrl", "requestedAmount", "bank"
                )
                .containsExactlyInAnyOrder(
                        tuple(1L, "정서현", "정서현_프로필_이미지", Money.of(3333), Bank.of("우리은행", "111-111"))
                );
    }

    @Test
    @DisplayName("특정 유저가 요청받은 정산 중 [송금완료한 정산] 이 없을 경우, 송금완료한 정산은 빈 ArrayList를 반환한다.")
    void readRequestedPayList2() throws Exception {
        //given
        PayRequestTarget payRequestTarget1 = PayRequestTarget.create(1L, seongjunId, 1L, Money.of(3333));
        PayRequestTarget payRequestTarget2 = PayRequestTarget.create(2L, seongjunId, 2L, Money.of(10000));

        Mockito.when(loadPayRequestTargetPort.loadByTargetMemberId(seongjunId)).thenReturn(List.of(payRequestTarget1, payRequestTarget2));

        stubLoadPayRequest(1L, seohyunId, Bank.of("우리은행", "111-111"));
        stubLoadPayRequest(2L, kyungminId, Bank.of("신한은행", "222-222"));

        stubLoadNicknameAndProfileImage(seohyunId, "정서현", "정서현_프로필_이미지");
        stubLoadNicknameAndProfileImage(kyungminId, "김경민", "김경민_프로필_이미지");

        //when
        ResultOfReadRequestedPayList result = readRequestedPayListService.readRequestedPayList(seongjunId);

        //then
        assertThat(result.getCompleteRequestedPayList()).isNotNull().isEmpty();

        assertThat(result.getInCompleteRequestedPayList()).hasSize(2)
                .extracting(
                        "payRequestTargetId", "payCreatorNickname", "payCreatorProfileImageUrl", "requestedAmount", "bank"
                )
                .containsExactlyInAnyOrder(
                        tuple(1L, "정서현", "정서현_프로필_이미지", Money.of(3333), Bank.of("우리은행", "111-111")),
                        tuple(2L, "김경민", "김경민_프로필_이미지", Money.of(10000), Bank.of("신한은행", "222-222"))
                );
    }

    @Test
    @DisplayName("특정 유저가 요청받은 정산 중 [송금완료하지 않은 정산] 이 없을 경우, 송금완료하지 않은 정산은 빈 ArrayList를 반환한다.")
    void readRequestedPayList3() throws Exception {
        //given
        PayRequestTarget payRequestTarget1 = PayRequestTarget.of(1L, seongjunId, 1L, Money.of(3333), true);
        PayRequestTarget payRequestTarget2 = PayRequestTarget.of(2L, seongjunId, 2L, Money.of(10000), true);

        Mockito.when(loadPayRequestTargetPort.loadByTargetMemberId(seongjunId)).thenReturn(List.of(payRequestTarget1, payRequestTarget2));

        stubLoadPayRequest(1L, seohyunId, Bank.of("우리은행", "111-111"));
        stubLoadPayRequest(2L, kyungminId, Bank.of("신한은행", "222-222"));

        stubLoadNicknameAndProfileImage(seohyunId, "정서현", "정서현_프로필_이미지");
        stubLoadNicknameAndProfileImage(kyungminId, "김경민", "김경민_프로필_이미지");

        //when
        ResultOfReadRequestedPayList result = readRequestedPayListService.readRequestedPayList(seongjunId);

        //then
        assertThat(result.getCompleteRequestedPayList()).hasSize(2)
                .extracting(
                        "payRequestTargetId", "payCreatorNickname", "payCreatorProfileImageUrl", "requestedAmount", "bank"
                )
                .containsExactlyInAnyOrder(
                        tuple(1L, "정서현", "정서현_프로필_이미지", Money.of(3333), Bank.of("우리은행", "111-111")),
                        tuple(2L, "김경민", "김경민_프로필_이미지", Money.of(10000), Bank.of("신한은행", "222-222"))
                );

        assertThat(result.getInCompleteRequestedPayList()).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("특정 멤버가 요청받은 정산이 하나도 없을 경우, 양쪽 리스트 모두 빈 리스트를 반환한다.")
    void readRequestedPayList_emptyList() throws Exception {
        //given
        Mockito.when(loadPayRequestTargetPort.loadByTargetMemberId(seongjunId)).thenReturn(List.of());

        //when
        ResultOfReadRequestedPayList result = readRequestedPayListService.readRequestedPayList(seongjunId);

        //then
        assertThat(result.getCompleteRequestedPayList()).isEmpty();
        assertThat(result.getInCompleteRequestedPayList()).isEmpty();
    }



    private void stubLoadPayRequest(Long payRequestId, Long payCreatorId, Bank bank) {
        PayRequest payRequest = Mockito.mock(PayRequest.class);
        Mockito.when(payRequest.getPayCreatorId()).thenReturn(payCreatorId);
        Mockito.when(payRequest.getBank()).thenReturn(bank);
        Mockito.when(loadPayRequestPort.loadById(payRequestId)).thenReturn(payRequest);
    }

    private void stubLoadNicknameAndProfileImage(Long payCreatorId, String nickname, String profileImageUrl) {
        Mockito.when(spaceMemberInfoPort.loadNicknameAndProfileImageById(payCreatorId))
                .thenReturn(NicknameAndProfileImage.of(nickname, profileImageUrl));
    }
}