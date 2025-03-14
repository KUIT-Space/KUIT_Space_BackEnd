package space.space_spring.domain.pay.domain;

import space.space_spring.global.util.NaturalNumber;

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

    public boolean areAllTargetsComplete() {
        for (PayRequestTarget payRequestTarget : payRequestTargets) {
            if (!payRequestTarget.isComplete()) {
                return false;
            }
        }

        return true;
    }

    public NaturalNumber calculateNumberOfSendCompleteTarget() {
        NaturalNumber count = NaturalNumber.of(0);
        for (PayRequestTarget payRequestTarget : payRequestTargets) {
            if (payRequestTarget.isComplete()) {
                count = count.add(NaturalNumber.of(1));
            }
        }

        return count;
    }

    public Money calculateMoneyOfSendComplete() {
        Money money = Money.of(0);
        for (PayRequestTarget payRequestTarget : payRequestTargets) {
            if (payRequestTarget.isComplete()) {
                money = money.add(payRequestTarget.getRequestedAmount());
            }
        }

        return money;
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

    public List<Long> getAllTargetIds() {
        return payRequestTargets.stream()
                .map(PayRequestTarget::getId)
                .toList();
    }
}
