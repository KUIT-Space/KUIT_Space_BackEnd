package space.space_spring.domain.pay.application.port.out;

import java.util.List;

public interface DeletePayRequestTargetPort {

    void deleteAllPayRequestTarget(List<Long> payRequestTargetIds);
}
