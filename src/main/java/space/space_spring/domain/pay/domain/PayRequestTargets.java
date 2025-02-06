package space.space_spring.domain.pay.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PayRequestTargets {

    private List<PayRequestTarget> payRequestTargets;

    private PayRequestTargets(List<PayRequestTarget> payRequestTargets) {
        this.payRequestTargets = payRequestTargets;
    }

    public static PayRequestTargets create(List<PayRequestTarget> payRequestTargets) {
        return new PayRequestTargets(payRequestTargets);
    }

    public List<PayRequestTarget> getCompletePayRequestTargetList() {
        List<PayRequestTarget> completePayRequestTargetList = new ArrayList<>();
        for (PayRequestTarget payRequestTarget : payRequestTargets) {
            if (payRequestTarget.isComplete()) {
                completePayRequestTargetList.add(payRequestTarget);
            }
        }

        return Collections.unmodifiableList(completePayRequestTargetList);
    }

    public List<PayRequestTarget> getInCompletePayRequestTargetList() {
        List<PayRequestTarget> inCompletePayRequestTargetList = new ArrayList<>();
        for (PayRequestTarget payRequestTarget : payRequestTargets) {
            if (!payRequestTarget.isComplete()) {
                inCompletePayRequestTargetList.add(payRequestTarget);
            }
        }

        return Collections.unmodifiableList(inCompletePayRequestTargetList);
    }
}
