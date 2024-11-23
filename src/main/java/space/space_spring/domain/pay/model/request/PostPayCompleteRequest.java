package space.space_spring.domain.pay.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostPayCompleteRequest {

    private Long payRequestTargetId;

}
