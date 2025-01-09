package space.space_spring.util.pay;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.pay.repository.PayDao;
import space.space_spring.domain.pay.model.entity.PayRequest;
import space.space_spring.domain.pay.model.entity.PayRequestTarget;
import space.space_spring.exception.CustomException;

import static space.space_spring.response.status.BaseExceptionResponseStatus.PAY_REQUEST_NOT_FOUND;
import static space.space_spring.response.status.BaseExceptionResponseStatus.PAY_REQUEST_TARGET_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class PayUtils {

    private final PayDao payDao;

    @Transactional
    public PayRequest findPayRequestById(Long id) {
        PayRequest payRequestById = payDao.findPayRequestById(id);
        if (payRequestById == null) {
            throw new CustomException(PAY_REQUEST_NOT_FOUND);
        }
        return payRequestById;
    }

    @Transactional
    public PayRequestTarget findPayRequestTargetById(Long id) {
        PayRequestTarget payRequestTargetById = payDao.findPayRequestTargetById(id);
        if (payRequestTargetById == null) {
            throw new CustomException(PAY_REQUEST_TARGET_NOT_FOUND);
        }
        return payRequestTargetById;
    }
}
