package space.space_spring.domain.discord;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import space.space_spring.domain.discord.domain.DiscordPayMessage;
import space.space_spring.domain.pay.domain.Bank;
import space.space_spring.domain.pay.domain.Money;
import space.space_spring.domain.pay.domain.PayType;
import space.space_spring.domain.spaceMember.application.port.out.NicknameAndProfileImage;

import java.util.Map;

public class PayMessageGenerate {

    @Test
    @DisplayName("정산 타겟 멤버는 본인이 요청받은 특정 정산 요청에 대해 완료처리를 할 수 있다.")
    void createINDIVIDUALPayMessage(){
        Map<Long, Money> targetMembers = Map.of(
                1L, Money.of(10000),
                2L, Money.of(10000),
                3L, Money.of(10000),
                4L, Money.of(5000),
                5L, Money.of(5000),
                6L, Money.of(5000),
                7L, Money.of(5000),
                8L, Money.of(20000),
                9L, Money.of(20000),
                10L, Money.of(10000)
        );

        DiscordPayMessage command = DiscordPayMessage.builder()
                .total(Money.of(100000))
                .payType(PayType.INDIVIDUAL)
                .creator(NicknameAndProfileImage.of("김상준","asdf"))
                .Bank("국민")
                .payTitle("정산을 시작합니다")
                .targetMembers(targetMembers)
                .accountNumber("12321313")
                .channelWebhook("Asdfdsasfd")
                .guildDiscordId(123123L)
                        .build();
        System.out.println(command.getPayMessageCommand().getContentMessage());
    }

    @Test
    @DisplayName("정산 타겟 멤버는 본인이 요청받은 특정 정산 요청에 대해 완료처리를 할 수 있다.")
    void createPayEQUAL_SPLITMessage(){
        Map<Long, Money> targetMembers = Map.of(
                1L, Money.of(5000),
                2L, Money.of(5000),
                3L, Money.of(5000),
                4L, Money.of(5000),
                5L, Money.of(5000),
                6L, Money.of(5000),
                7L, Money.of(5000),
                8L, Money.of(5000),
                9L, Money.of(5000),
                10L, Money.of(5000)
        );

        DiscordPayMessage command = DiscordPayMessage.builder()
                .total(Money.of(500000))
                .payType(PayType.EQUAL_SPLIT)
                .creator(NicknameAndProfileImage.of("김상준","asdf"))
                .Bank("국민")
                .payTitle("정산을 시작합니다")
                .targetMembers(targetMembers)
                .accountNumber("12321313")
                .channelWebhook("Asdfdsasfd")
                .guildDiscordId(123123L)
                .build();
        System.out.println(command.getPayMessageCommand().getContentMessage());
    }
}
