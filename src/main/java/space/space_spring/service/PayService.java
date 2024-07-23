package space.space_spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.space_spring.dao.PayDao;
import space.space_spring.dto.pay.PayReceiveInfoDto;
import space.space_spring.dto.pay.PayRequestInfoDto;
import space.space_spring.entity.PayRequest;
import space.space_spring.entity.PayRequestTarget;
import space.space_spring.entity.User;
import space.space_spring.util.user.UserUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PayService {

    private final PayDao payDao;
    private final UserUtils userUtils;

    public List<PayRequestInfoDto> getPayRequestInfoForUser(Long userId, Long spaceId) {
        // TODO 1. userId에 해당하는 user find
        User userByUserId = userUtils.findUserByUserId(userId);

        // TODO 2. 유저가 요청한 정산 중 진행 중인 정산 리스트 select
        List<PayRequest> payRequestListByUser = payDao.findPayRequestListByUser(userByUserId);

        // TODO 3. return 타입 구성
        // 3-1. 각 payRequest 에 해당하는 모든 payRequestTarget 을 loop로 돌면서 데이터 수집
        List<PayRequestInfoDto> payRequestInfoDtoList = new ArrayList<>();

        for (PayRequest payRequest : payRequestListByUser) {
            // 하나의 정산 요청에 대하여 ,,,
            int totalAmount = payRequest.getTotalAmount();
            int receiveAmount = 0;
            int totalTargetNum = 0;
            int receiveTargetNum = 0;

            List<PayRequestTarget> payRequestTargetList = payDao.findPayRequestTargetList(payRequest);
            for (PayRequestTarget payRequestTarget : payRequestTargetList) {
                if (payRequestTarget.isComplete()) {
                    // 해당 타겟이 돈을 낸 경우
                    receiveAmount += payRequestTarget.getRequestAmount();
                    receiveTargetNum++;
                }

                totalTargetNum++;
            }

            PayRequestInfoDto payRequestInfoDto = new PayRequestInfoDto(totalAmount, receiveAmount, totalTargetNum, receiveTargetNum);
            payRequestInfoDtoList.add(payRequestInfoDto);
        }

        return payRequestInfoDtoList;
    }

    public List<PayReceiveInfoDto> getPayReceiveInfoForUser(Long userId, Long spaceId) {


    }
}
