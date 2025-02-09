package space.space_spring.domain.pay.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PayRequests {

    private final List<PayRequest> payRequests;

    private PayRequests(List<PayRequest> payRequests) {
        this.payRequests = payRequests;
    }

    public static PayRequests create(List<PayRequest> payRequests) {
        return new PayRequests(payRequests);
    }
}
