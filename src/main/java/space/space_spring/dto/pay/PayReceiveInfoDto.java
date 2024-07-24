package space.space_spring.dto.pay;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PayReceiveInfoDto {

    private String payCreatorName;

    private int requestAmount;
}
