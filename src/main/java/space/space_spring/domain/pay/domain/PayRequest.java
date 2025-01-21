package space.space_spring.domain.pay.domain;

import space.space_spring.domain.spaceMember.SpaceMember;

public class PayRequest {

    private SpaceMember payCreator;            // JPA 엔티티가 아니라, 도메인 엔티티로 변경

    private Money totalAmount;

    private Bank bank;          // 정산


}
