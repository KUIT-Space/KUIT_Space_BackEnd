package space.space_spring.domain.pay.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import space.space_spring.domain.pay.application.port.in.loadCurrentPayRequestState.CurrentPayRequestState;
import space.space_spring.domain.pay.application.port.in.loadCurrentPayRequestState.LoadCurrentPayRequestStateUseCase;
import space.space_spring.domain.pay.application.port.in.readPayDetail.ResultOfReadPayDetail;
import space.space_spring.domain.pay.application.port.out.LoadPayRequestPort;
import space.space_spring.domain.pay.application.port.out.LoadPayRequestTargetPort;
import space.space_spring.domain.pay.domain.*;
import space.space_spring.domain.spaceMember.application.port.out.LoadSpaceMemberInfoPort;
import space.space_spring.domain.spaceMember.application.port.out.NicknameAndProfileImage;
import space.space_spring.global.common.entity.BaseInfo;
import space.space_spring.global.exception.CustomException;
import space.space_spring.global.util.NaturalNumber;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.INVALID_PAY_REQUEST_ID;

class ReadPayDetailServiceTest {

    private LoadPayRequestPort loadPayRequestPort;
    private LoadPayRequestTargetPort loadPayRequestTargetPort;
    private LoadCurrentPayRequestStateUseCase loadCurrentPayRequestStateUseCase;
    private LoadSpaceMemberInfoPort loadSpaceMemberInfoPort;
    private ReadPayDetailService readPayDetailService;

    private Long seongjunId;
    private Long sangjunId;
    private Long seohyunId;
    private Long kyungminId;

    @BeforeEach
    void setUp() {
        loadPayRequestPort = Mockito.mock(LoadPayRequestPort.class);
        loadPayRequestTargetPort = Mockito.mock(LoadPayRequestTargetPort.class);
        loadCurrentPayRequestStateUseCase = Mockito.mock(LoadCurrentPayRequestStateUseCase.class);
        loadSpaceMemberInfoPort = Mockito.mock(LoadSpaceMemberInfoPort.class);
        readPayDetailService = new ReadPayDetailService(loadPayRequestPort, loadPayRequestTargetPort, loadCurrentPayRequestStateUseCase, loadSpaceMemberInfoPort);

        seongjunId = 1L;
        sangjunId = 2L;
        seohyunId = 3L;
        kyungminId = 4L;
    }

    @Test
    @DisplayName("스페이스 멤버는 본인이 생성한 정산요청의 상세 정보를 조회할 수 있다.")
    void readPayDetail1() throws Exception {
        //given
        Long spaceMemberId = seongjunId;     // 요청 보낸 스페이스 멤버
        Long payRequestId = 1L;

        PayRequest payRequest = createPayRequest(payRequestId, spaceMemberId);
        List<PayRequestTarget> targets = createPayRequestTargets(payRequestId);
        CurrentPayRequestState currentState = createCurrentState();

        stubLoadPayRequest(payRequestId, payRequest);
        stubLoadPayRequestTargets(payRequestId, targets);
        stubLoadCurrentState(payRequestId, currentState);
        stubLoadSpaceMemberInfo();

        //when
        ResultOfReadPayDetail result = readPayDetailService.readPayDetail(spaceMemberId, payRequestId);

        //then
        assertThat(result)
                .extracting(
                "totalAmount", "receivedAmount", "totalTargetNum", "sendCompleteTargetNum"
                )
                .containsExactly(
                        Money.of(10000), Money.of(6666), NaturalNumber.of(3), NaturalNumber.of(2)
                );
        assertThat(result.getInfoOfTargetDetails()).hasSize(3)
                .extracting("targetMemberId", "targetMemberName", "targetMemberProfileImageUrl", "requestedAmount")
                .containsExactlyInAnyOrder(
                        tuple(sangjunId, "개구리비안", "상준 프로필 이미지", Money.of(3333)),
                        tuple(seohyunId, "정서현", "서현 프로필 이미지", Money.of(3333)),
                        tuple(kyungminId, "김경민", "경민 프로필 이미지", Money.of(3333))
                );
    }

    @Test
    @DisplayName("자신이 생성하지 않은 정산의 상세 정보 조회를 요청할 경우, 에러를 발생시킨다.")
    void readPayDetail2() throws Exception {
        //given
        Long spaceMemberId = seongjunId;     // 요청 보낸 스페이스 멤버
        Long payRequestId = 1L;

        PayRequest payRequest = createPayRequest(payRequestId, kyungminId);         // 정산 생성자는 kyungmin
        List<PayRequestTarget> targets = createPayRequestTargets(payRequestId);
        CurrentPayRequestState currentState = createCurrentState();

        stubLoadPayRequest(payRequestId, payRequest);
        stubLoadPayRequestTargets(payRequestId, targets);
        stubLoadCurrentState(payRequestId, currentState);
        stubLoadSpaceMemberInfo();

        //when //then
        assertThatThrownBy(() -> readPayDetailService.readPayDetail(spaceMemberId, payRequestId))
                .isInstanceOf(CustomException.class)
                .hasMessage(INVALID_PAY_REQUEST_ID.getMessage());
    }

    private PayRequest createPayRequest(Long payRequestId, Long payCreatorId) {
        return PayRequest.create(
                payRequestId,
                payCreatorId,
                1L,
                Money.of(10000),
                NaturalNumber.of(3),
                Bank.of("우리은행", "111-111"),
                PayType.EQUAL_SPLIT,
                BaseInfo.ofEmpty()
        );
    }

    private List<PayRequestTarget> createPayRequestTargets(Long payRequestId) {
        PayRequestTarget target1 = PayRequestTarget.create(
                1L, sangjunId, payRequestId, Money.of(3333), BaseInfo.ofEmpty()
        );
        PayRequestTarget target2 = PayRequestTarget.create(
                2L, seohyunId, payRequestId, Money.of(3333), BaseInfo.ofEmpty()
        );
        PayRequestTarget target3 = PayRequestTarget.create(
                3L, kyungminId, payRequestId, Money.of(3333), BaseInfo.ofEmpty()
        );
        return List.of(target1, target2, target3);
    }

    private CurrentPayRequestState createCurrentState() {
        // 3명의 정산 대상들 중 2명만 정산 완료한 상황을 가정
        return CurrentPayRequestState.of(Money.of(6666), NaturalNumber.of(2));
    }

    private void stubLoadPayRequest(Long payRequestId, PayRequest payRequest) {
        when(loadPayRequestPort.loadById(payRequestId)).thenReturn(payRequest);
    }

    private void stubLoadPayRequestTargets(Long payRequestId, List<PayRequestTarget> targets) {
        when(loadPayRequestTargetPort.loadByPayRequestId(payRequestId)).thenReturn(targets);
    }

    private void stubLoadCurrentState(Long payRequestId, CurrentPayRequestState currentState) {
        when(loadCurrentPayRequestStateUseCase.loadCurrentPayRequestState(payRequestId))
                .thenReturn(currentState);
    }

    private void stubLoadSpaceMemberInfo() {
        when(loadSpaceMemberInfoPort.loadNicknameAndProfileImageById(sangjunId))
                .thenReturn(NicknameAndProfileImage.of("개구리비안", "상준 프로필 이미지"));
        when(loadSpaceMemberInfoPort.loadNicknameAndProfileImageById(seohyunId))
                .thenReturn(NicknameAndProfileImage.of("정서현", "서현 프로필 이미지"));
        when(loadSpaceMemberInfoPort.loadNicknameAndProfileImageById(kyungminId))
                .thenReturn(NicknameAndProfileImage.of("김경민", "경민 프로필 이미지"));
    }
}