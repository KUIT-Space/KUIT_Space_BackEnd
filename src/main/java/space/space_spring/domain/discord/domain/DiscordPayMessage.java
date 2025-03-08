package space.space_spring.domain.discord.domain;

import lombok.Builder;
import space.space_spring.domain.discord.application.port.out.CreateDiscordThreadCommand;
import space.space_spring.domain.pay.domain.Money;
import space.space_spring.domain.pay.domain.PayType;
import space.space_spring.domain.spaceMember.application.port.out.NicknameAndProfileImage;
import space.space_spring.global.exception.CustomException;

import java.util.Map;
import java.util.stream.Collectors;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.INVALID_PAY_CREATE;

@Builder
public class DiscordPayMessage {
    private NicknameAndProfileImage creator;
    private Map<Long, Money> targetMembers;
    private PayType payType;

    private String Bank;
    private String accountNumber;

    private Money total;

    private String payTitle;

    private Long guildDiscordId;
    private Long channelDiscordId;
    private String channelWebhook;


    public CreateDiscordThreadCommand getPayMessageCommand(){
        String BankInfo = Bank+"  "+accountNumber;
        String content="";
        if(this.payType==PayType.EQUAL_SPLIT){
            content +="총 금액 " +total.toString()
                    + "\n";
            String mentionSet=targetMembers.keySet().stream()
                    .map(this::asMention)
                    .collect(Collectors.joining(" "));
            content+=mentionSet;
        }
        if(this.payType==PayType.INDIVIDUAL){
            content +="총 금액 " +total.toString()
                    + "\n";
            String mentionSet=targetMembers.entrySet().stream()
                    .map(entry->asMention(entry.getKey())+":"+entry.getValue().toString()+"원")
                    .collect(Collectors.joining("\n"));
            content+=mentionSet;
        }
        if(this.payType==null){
            throw new CustomException(INVALID_PAY_CREATE,"in discord : payType is null");
        }

        return CreateDiscordThreadCommand.builder()
                .channelDiscordId(this.channelDiscordId)
                .guildDiscordId(this.guildDiscordId)
                .webHookUrl(this.channelWebhook)
                .avatarUrl(this.creator.getProfileImageUrl())
                .userName(this.creator.getNickname())
                .threadName(this.payTitle)
                .contentMessage(content)
                .build();
    }

    private String asMention(Long memberDiscordId){
        return "<@" + memberDiscordId.toString() +'>';
    }
}
