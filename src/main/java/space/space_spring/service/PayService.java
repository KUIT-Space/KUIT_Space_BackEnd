package space.space_spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.dao.PayDao;
import space.space_spring.dao.UserDao;
import space.space_spring.dao.UserSpaceDao;
import space.space_spring.dto.pay.dto.*;
import space.space_spring.dto.pay.request.PostPayCreateRequest;
import space.space_spring.dto.pay.response.GetRecentPayRequestBankInfoResponse;
import space.space_spring.dto.pay.response.PostPayCompleteResponse;
import space.space_spring.entity.*;
import space.space_spring.exception.UserSpaceException;
import space.space_spring.util.space.SpaceUtils;
import space.space_spring.util.user.UserUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static space.space_spring.response.status.BaseExceptionResponseStatus.USER_IS_NOT_IN_SPACE;

@Service
@RequiredArgsConstructor
public class PayService {

    private final PayDao payDao;
    private final UserUtils userUtils;
    private final SpaceUtils spaceUtils;
    private final UserDao userDao;
    private final UserSpaceDao userSpaceDao;

    @Transactional
    public List<PayRequestInfoDto> getPayRequestInfoForUser(Long userId, Long spaceId, boolean isComplete) {
        // TODO 1. userId에 해당하는 user find
        User userByUserId = userUtils.findUserByUserId(userId);
        
        // TODO 2. spaceId에 해당하는 space find
        Space spaceBySpaceId = spaceUtils.findSpaceBySpaceId(spaceId);

        // TODO 3. 유저가 요청한 정산 리스트 select
        List<PayRequest> payRequestListByUser = payDao.findPayRequestListByUser(userByUserId, spaceBySpaceId, isComplete);

        // TODO 4. return 타입 구성
        // <PayRequest의 receiveAmount 값을 저장할 수 있도록 다오단에 코드를 추가해야할 것 같음>

        // 3-1. 각 payRequest 에 해당하는 모든 payRequestTarget 을 loop로 돌면서 데이터 수집
        List<PayRequestInfoDto> payRequestInfoDtoList = new ArrayList<>();

        for (PayRequest payRequest : payRequestListByUser) {
            PayRequestInfoDto payRequestInfoDto = createPayRequestInfoDto(payRequest);
            payRequestInfoDtoList.add(payRequestInfoDto);
        }

        return payRequestInfoDtoList;
    }

    private PayRequestInfoDto createPayRequestInfoDto(PayRequest payRequest) {
        Long payRequestId = payRequest.getPayRequestId();
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

        return new PayRequestInfoDto(
                payRequestId, totalAmount, receiveAmount, totalTargetNum, receiveTargetNum
        );
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
            PayReceiveInfoDto payReceiveInfoDto = createPayReceiveInfoDto(payRequestTarget);
            payReceiveInfoDtoList.add(payReceiveInfoDto);
        }

        return payReceiveInfoDtoList;
    }

    private PayReceiveInfoDto createPayReceiveInfoDto(PayRequestTarget payRequestTarget) {
        String payCreatorName = payRequestTarget.getPayRequest().getPayCreateUser().getUserName();          // 리펙토링 필요
        int requestAmount = payRequestTarget.getRequestAmount();

        return new PayReceiveInfoDto(payRequestTarget.getPayRequestTargetId(), payCreatorName, requestAmount);
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
    public List<PayRequestTarget> createPay(Long userId, Long spaceId, PostPayCreateRequest postPayCreateRequest) {
        // TODO 1. userId로 User find (user : 정산 생성한 유저)
        User payCreateUser = userUtils.findUserByUserId(userId);

        // TODO 2. spaceId로 Space find
        Space spaceBySpaceId = spaceUtils.findSpaceBySpaceId(spaceId);

        // TODO 3. PayRequest 엔티티 생성
        boolean isComplete = false;
        PayRequest payRequest = payDao.createPayRequest(payCreateUser, spaceBySpaceId, postPayCreateRequest.getTotalAmount(), postPayCreateRequest.getBankName(), postPayCreateRequest.getBankAccountNum(), isComplete);

        // TODO 4. PayRequestTarget 엔티티 생성
        List<PayRequestTarget> resultList = new ArrayList<>();
        for (PostPayCreateRequest.TargetInfo targetInfo : postPayCreateRequest.getTargetInfoList()) {
            PayRequestTarget payRequestTarget = payDao.createPayRequestTarget(payRequest, targetInfo.getTargetUserId(), targetInfo.getRequestAmount(), isComplete);
            resultList.add(payRequestTarget);
        }

        return resultList;
    }

    /**
     * 하나의 정산에 대한 상세정보 조회
     */
    @Transactional
    public TotalPayInfoDto getTotalPayInfo(Long spaceId, Long payRequestId) {
        // TODO 1. spaceId로 Space 엔티티 find
        Space spaceBySpaceId = spaceUtils.findSpaceBySpaceId(spaceId);

        // TODO 2. payRequestId 로 PayRequest 엔티티 find
        PayRequest payRequestById = payDao.findPayRequestById(payRequestId);

        // TODO 3. PayRequest로 해당 정산의 정보 get
        PayRequestInfoDto payRequestInfoDto = createPayRequestInfoDto(payRequestById);

        // TODO 4. PayRequest의 PayRequestTarget find
        List<PayRequestTarget> payRequestTargetListByPayRequest = payDao.findPayRequestTargetListByPayRequest(payRequestById);

        // TODO 5. 정산 타겟 유저 정보 get
        List<PayTargetInfoDto> payTargetInfoDtoList = new ArrayList<>();
        for (PayRequestTarget payRequestTarget : payRequestTargetListByPayRequest) {
            PayTargetInfoDto payTargetInfoDto = createPayTargetInfoDto(payRequestTarget, spaceBySpaceId);
            payTargetInfoDtoList.add(payTargetInfoDto);
        }

        // TODO 6. return 타입 구성
        return new TotalPayInfoDto(
                payRequestId,
                payRequestById.getBankName(),
                payRequestById.getBankAccountNum(),
                payRequestById.getTotalAmount(),
                payRequestInfoDto.getReceiveAmount(),
                payRequestInfoDto.getTotalTargetNum(),
                payRequestInfoDto.getReceiveTargetNum(),
                payTargetInfoDtoList,
                payRequestById.isComplete()
        );
    }

    private PayTargetInfoDto createPayTargetInfoDto(PayRequestTarget payRequestTarget, Space space) {
        Long targetUserId = payRequestTarget.getTargetUserId();
        User userByUserId = userDao.findUserByUserId(targetUserId);

        UserSpace userSpace = userSpaceDao.findUserSpaceByUserAndSpace(userByUserId, space)
                .orElseThrow(() -> new UserSpaceException(USER_IS_NOT_IN_SPACE));

        String userName = userSpace.getUserName();
        String userProfileImg = userSpace.getUserProfileImg();

        return new PayTargetInfoDto(
                payRequestTarget.getTargetUserId(),
                userName,
                userProfileImg,
                payRequestTarget.getRequestAmount(),
                payRequestTarget.isComplete()
        );
    }

    /**
     * 정산 타겟 유저의 정산 완료 처리
     */
    @Transactional
    public PostPayCompleteResponse setPayRequestTargetToComplete(Long userId, Long spaceId, Long payRequestTargetId) {
        // TODO 1. userId 로 User find
        User userByUserId = userUtils.findUserByUserId(userId);

        // TODO 2. spaceId로 Space find
        Space spaceBySpaceId = spaceUtils.findSpaceBySpaceId(spaceId);

        // TODO 3. payRequestTargetId로 PayRequestTarget find
        PayRequestTarget payRequestTargetById = payDao.findPayRequestTargetById(payRequestTargetId);

        // TODO 4. 해당 PayRequestTarget 정산 완료 처리
        payRequestTargetById.changeCompleteStatus(true);

        // TODO 5. PayRequest의 완료 여부 파악
        PayRequest payRequest = payRequestTargetById.getPayRequest();
        List<PayRequestTarget> payRequestTargetListByPayRequest = payDao.findPayRequestTargetListByPayRequest(payRequest);

        boolean payRequestCompleteStatus = true;
        for (PayRequestTarget payRequestTarget : payRequestTargetListByPayRequest) {
            if (!payRequestTarget.isComplete()) {
                payRequestCompleteStatus = false;
                break;              // 더이상 확인할 필요가 없으므로 break
            }
        }

        if (payRequestCompleteStatus) {
            payRequest.changeCompleteStatus(true);
        }

        // TODO 6. return 타입 구성
        return new PostPayCompleteResponse(
                payRequest.getPayRequestId(),
                payRequestCompleteStatus
        );
    }
}
