package space.space_spring.domain.pay.application.service;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import space.space_spring.domain.pay.application.port.out.LoadPayRequestPort;
import space.space_spring.domain.pay.domain.Bank;
import space.space_spring.domain.pay.domain.Money;
import space.space_spring.domain.pay.domain.PayRequest;
import space.space_spring.domain.pay.domain.PayType;
import space.space_spring.global.common.entity.BaseInfo;
import space.space_spring.global.util.NaturalNumber;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.when;

class ReadBankInfoServiceTest {

    private LoadPayRequestPort loadPayRequestPort;
    private ReadBankInfoService readBankInfoService;

    private Long seongjunId;

    @BeforeEach
    void setUp() {
        loadPayRequestPort = Mockito.mock(LoadPayRequestPort.class);
        readBankInfoService = new ReadBankInfoService(loadPayRequestPort);

        seongjunId = 1L;
    }

    @Test
    @DisplayName("스페이스 멤버가 과거 생성한 정산들의 은행정보를 중복을 제거하여 반환한다.")
    void readBankInfo1() throws Exception {
        //given
        PayRequest payRequest1 = PayRequest.create(1L, seongjunId, 1L, Money.of(10000), NaturalNumber.of(3), Bank.of("우리은행", "111-111"), PayType.EQUAL_SPLIT, BaseInfo.ofEmpty());
        PayRequest payRequest2 = PayRequest.create(2L, seongjunId, 2L, Money.of(20000), NaturalNumber.of(2), Bank.of("우리은행", "222-222"), PayType.EQUAL_SPLIT, BaseInfo.ofEmpty());
        PayRequest payRequest3 = PayRequest.create(3L, seongjunId, 3L, Money.of(10000), NaturalNumber.of(5), Bank.of("국민은행", "333-333"), PayType.INDIVIDUAL, BaseInfo.ofEmpty());
        PayRequest payRequest4 = PayRequest.create(4L, seongjunId, 4L, Money.of(10000), NaturalNumber.of(4), Bank.of("국민은행", "333-333"), PayType.INDIVIDUAL, BaseInfo.ofEmpty());

        when(loadPayRequestPort.loadByPayCreatorId(seongjunId)).thenReturn(List.of(payRequest1, payRequest2, payRequest3, payRequest4));

        //when
        Set<Bank> banks = readBankInfoService.readBankInfo(seongjunId);

        //then
        assertThat(banks).hasSize(3);
        assertThat(banks.stream())
                .extracting("name", "accountNumber")
                .containsExactlyInAnyOrder(
                        tuple("우리은행", "111-111"),
                        tuple("우리은행", "222-222"),
                        tuple("국민은행", "333-333")
                );
    }

    @Test
    @DisplayName("스페이스 멤버가 과거 생성한 정산이 없는 경우 빈 Set을 반환한다.")
    void readBankInfo2() throws Exception {
        //given
        when(loadPayRequestPort.loadByPayCreatorId(seongjunId)).thenReturn(List.of());          // 빈 리스트 (seongjun이 생성한 정산은 없다)

        //when
        Set<Bank> banks = readBankInfoService.readBankInfo(seongjunId);

        //then
        assertThat(banks).isEmpty();
    }
}