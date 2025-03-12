package space.space_spring.domain.discord.domain;

import lombok.Builder;
import space.space_spring.domain.discord.application.port.out.CreateDiscordThreadCommand;
import space.space_spring.domain.pay.domain.Money;
import space.space_spring.domain.pay.domain.PayType;
import space.space_spring.domain.spaceMember.application.port.out.NicknameAndProfileImage;
import space.space_spring.global.exception.CustomException;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
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
        String bankInfo = Bank+"  "+accountNumber;
        String content="";

        content += "["+ creator.getNickname()+"님의 정산]\n\n";

        content += bankInfo+"\n\n";
        content +="총 금액 : " +total.getAmountInInteger() + "원\n";
        content += "총 인원 : "+targetMembers.size()+"명\n\n";
        content +="⭐ 본인 해당 금액을 확인해주세요 ⭐\n\n";

        if(this.payType==PayType.EQUAL_SPLIT){

            content += "\n"+"인당 금액 : "+targetMembers.values().stream().findFirst().get().getAmountInInteger()+"원\n\n";
            String mentionSet=targetMembers.keySet().stream()
                    .map(this::asMention)
                    .collect(Collectors.joining(" "));
            content+=mentionSet;
        }

        if(this.payType==PayType.INDIVIDUAL){

            content += "\n";
//            String mentionSet=targetMembers.entrySet().stream()
//                    .map(entry->asMention(entry.getKey())+":"+entry.getValue().getAmountInInteger()+"원")
//                    .collect(Collectors.joining("\n"));
            String mentionSet = sortByMoney(targetMembers);
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
                .startMessage("")
                .build();
    }

    private String asMention(Long memberDiscordId){
        return "<@" + memberDiscordId.toString() +'>';
    }

    private String sortByMoney(Map<Long,Money> targetMembers){
        String mentionSet = targetMembers.entrySet().stream()
                .collect(Collectors.groupingBy(
                        entry -> entry.getValue().getAmountInInteger(),
                        TreeMap::new, // 금액 오름차순 정렬
                        Collectors.mapping(
                                entry -> asMention(entry.getKey()),
                                Collectors.joining(" ")
                        )
                ))
                .entrySet().stream()
                .map(new Function<Map.Entry<Integer, String>, String>() {
                    int index = 1;
                    @Override
                    public String apply(Map.Entry<Integer, String> entry) {
                        return String.format("(%d) %d원\n%s", index++, entry.getKey(), entry.getValue());
                    }
                })
                .collect(Collectors.joining("\n\n"));

        return mentionSet;
    }
}
