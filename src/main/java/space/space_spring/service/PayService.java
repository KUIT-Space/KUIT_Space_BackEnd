package space.space_spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.dao.PayDao;
import space.space_spring.dto.pay.*;
import space.space_spring.entity.PayRequest;
import space.space_spring.entity.PayRequestTarget;
import space.space_spring.entity.Space;
import space.space_spring.entity.User;
import space.space_spring.util.space.SpaceUtils;
import space.space_spring.util.user.UserUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PayService {

    private final PayDao payDao;
    private final UserUtils userUtils;
    private final SpaceUtils spaceUtils;

    @Transactional
    public List<PayRequestInfoDto> getPayRequestInfoForUser(Long userId, Long spaceId, boolean isComplete) {
        // TODO 1. userId에 해당하는 user find
        User userByUserId = userUtils.findUserByUserId(userId);
        
        // TODO 2. spaceId에 해당하는 space find
        Space spaceBySpaceId = spaceUtils.findSpaceBySpaceId(spaceId);

        // TODO 3. 유저가 요청한 정산 리스트 select
        List<PayRequest> payRequestListByUser = payDao.findPayRequestListByUser(userByUserId, spaceBySpaceId, isComplete);

        // TODO 4. return 타입 구성
        // 3-1. 각 payRequest 에 해당하는 모든 payRequestTarget 을 loop로 돌면서 데이터 수집
        List<PayRequestInfoDto> payRequestInfoDtoList = new ArrayList<>();

        for (PayRequest payRequest : payRequestListByUser) {
            // 하나의 정산 요청에 대하여 ,,,
            int totalAmount = payRequest.getTotalAmount();
            int receiveAmount = 0;
            int totalTargetNum = 0;
            int receiveTargetNum = 0;

            List<PayRequestTarget> payRequestTargetList = payDao.findPayRequestTargetListByPayRequest(payRequest);
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

    @Transactional
    public List<PayReceiveInfoDto> getPayReceiveInfoForUser(Long userId, Long spaceId, boolean isComplete) {
        // TODO 1. userId에 해당하는 유저 find
        User userByUserId = userUtils.findUserByUserId(userId);

        // TODO 2. spaceId에 해당하는 space find
        Space spaceBySpaceId = spaceUtils.findSpaceBySpaceId(spaceId);
        
        // TODO 3. 유저가 요청받은 정산 리스트 select
        List<PayRequestTarget> payRequestTargetListByUser = payDao.findPayRequestTargetListByUser(userByUserId, spaceBySpaceId, isComplete);

        // TODO 4. return 타입 구성
        // 3-1. 각 payRequestTarget 에 해당하는 정산 요청자, 정산 요청 금액 을 loop를 돌면서 데이터 수집
        List<PayReceiveInfoDto> payReceiveInfoDtoList = new ArrayList<>();

        for (PayRequestTarget payRequestTarget : payRequestTargetListByUser) {
            String payCreatorName = payRequestTarget.getPayRequest().getPayCreateUser().getUserName();          // 리펙토링 필요
            int requestAmount = payRequestTarget.getRequestAmount();

            PayReceiveInfoDto payReceiveInfoDto = new PayReceiveInfoDto(payCreatorName, requestAmount);

            payReceiveInfoDtoList.add(payReceiveInfoDto);
        }

        return payReceiveInfoDtoList;
    }

    @Transactional
    public GetRecentPayRequestBankInfoResponse getRecentPayRequestBankInfoForUser(Long userId) {
        // TODO 1. userId에 해당하는 유저 find
        User userByUserId = userUtils.findUserByUserId(userId);

        // TODO 2. 유저의 최근 정산받은 은행 계좌 목록 fine
        List<RecentPayRequestBankInfoDto> recentPayRequestBankInfoByUser = payDao.findRecentPayRequestBankInfoByUser(userByUserId);

        return new GetRecentPayRequestBankInfoResponse(recentPayRequestBankInfoByUser);
    }

    @Transactional
    public void createPay(Long userId, Long spaceId, PostPayCreateRequest postPayCreateRequest) {
        // TODO 1. userId로 User find (user : 정산 생성한 유저)
        User payCreateUser = userUtils.findUserByUserId(userId);

        // TODO 2. spaceId로 Space find
        Space spaceBySpaceId = spaceUtils.findSpaceBySpaceId(spaceId);

        // TODO 3. PayRequest 엔티티 생성
        boolean isComplete = false;
        PayRequest payRequest = payDao.createPayRequest(payCreateUser, spaceBySpaceId, postPayCreateRequest.getTotalAmount(), postPayCreateRequest.getBankName(), postPayCreateRequest.getBankAccountNum(), isComplete);

        // TODO 4. PayRequestTarget 엔티티 생성
        for (PostPayCreateRequest.TargetInfo targetInfo : postPayCreateRequest.getTargetInfoList()) {
            payDao.createPayRequestTarget(payRequest, targetInfo.getTargetUserId(), targetInfo.getRequestAmount(), isComplete);
        }
    }


}
