package space.space_spring.domain.pay.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.pay.model.firstCollection.PayRequestInfos;
import space.space_spring.domain.pay.model.firstCollection.PayRequestTargets;
import space.space_spring.domain.pay.model.firstCollection.PayRequests;
import space.space_spring.domain.pay.model.firstCollection.PayTargetInfos;
import space.space_spring.domain.pay.model.mapper.PayMapper;
import space.space_spring.domain.userSpace.model.entity.UserSpace;
import space.space_spring.domain.userSpace.repository.UserSpaceRepository;
import space.space_spring.domain.pay.model.entity.PayRequest;
import space.space_spring.domain.pay.model.dto.*;
import space.space_spring.domain.pay.model.entity.PayRequestTarget;
import space.space_spring.domain.pay.model.response.PayHomeViewResponse;
import space.space_spring.domain.pay.repository.PayRequestRepository;
import space.space_spring.domain.pay.repository.PayRequestTargetRepository;
import space.space_spring.exception.CustomException;

import java.util.ArrayList;
import java.util.List;

import static space.space_spring.response.status.BaseExceptionResponseStatus.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PayService {

//    private final PayDao payDao;
//    private final UserUtils userUtils;
//    private final SpaceUtils spaceUtils;
//    private final UserDao userDao;
//    private final UserSpaceDao userSpaceDao;

    private final UserSpaceRepository userSpaceRepository;
    private final PayRequestRepository payRequestRepository;
    private final PayRequestTargetRepository payRequestTargetRepository;
    private final PayMapper payMapper;

    private final boolean INCOMPLETE_PAY = false;
    private final boolean COMPLETE_PAY = true;

    @Transactional          // 지연로딩을 이용하기 위한 Transaction 설정
    public PayHomeViewResponse getPayHomeInfos(Long userId, Long spaceId) {
        // 유저가 스페이스에 속하는 지 검증하고,
        UserSpace userSpace = validateUserInSpace(userId, spaceId);

        // 유저가 요청한 정산들 중, 현재 진행중인 정산 찾고
        List<PayRequest> allPayRequests = payRequestRepository.findAllByUserAndSpace(userSpace.getUser(), userSpace.getSpace(), INCOMPLETE_PAY);
        PayRequests payRequests = PayRequests.create(allPayRequests);
        PayRequestInfos payRequestInfos = payRequests.getPayRequestInfos();

        // 유저가 요청받은 정산들 중, 현재 진행중인 정산 찾아서
        List<PayRequestTarget> allPayRequestTargets = payRequestTargetRepository.findAllByUserAndSpace(userId, userSpace.getSpace(), INCOMPLETE_PAY);
        PayRequestTargets payRequestTargets = PayRequestTargets.create(allPayRequestTargets);
        PayTargetInfos payTargetInfos = payRequestTargets.getPayTargetInfos();

        // return
        return payMapper.createPayHomeViewResponse(payRequestInfos, payTargetInfos);
    }

    private UserSpace validateUserInSpace(Long userId, Long spaceId) {
        return userSpaceRepository.findUserSpaceByUserAndSpace(userId, spaceId).orElseThrow(() -> new CustomException(USER_IS_NOT_IN_SPACE));
    }



//    @Transactional
//    public List<PayRequestInfoDto> getPayRequestInfoForUser(Long userId, Long spaceId, boolean isComplete) {
//        // TODO 1. userId에 해당하는 user find
//        User userByUserId = userUtils.findUserByUserId(userId);
//
//        // TODO 2. spaceId에 해당하는 space find
//        Space spaceBySpaceId = spaceUtils.findSpaceBySpaceId(spaceId);
//
//        // TODO 3. 유저가 요청한 정산 리스트 select
//        List<PayRequest> payRequestListByUser = payDao.findPayRequestListByUser(userByUserId, spaceBySpaceId, isComplete);
//
//        // TODO 4. return 타입 구성
//        // <PayRequest의 receiveAmount 값을 저장할 수 있도록 다오단에 코드를 추가해야할 것 같음>
//
//        // 3-1. 각 payRequest 에 해당하는 모든 payRequestTarget 을 loop로 돌면서 데이터 수집
//        List<PayRequestInfoDto> payRequestInfoDtoList = new ArrayList<>();
//
//        for (PayRequest payRequest : payRequestListByUser) {
//            PayRequestInfoDto payRequestInfoDto = payRequest.createPayRequestInfo();
//            payRequestInfoDtoList.add(payRequestInfoDto);
//        }
//
//        return payRequestInfoDtoList;
//    }
//
//    @Transactional
//    public List<PayTargetInfoDto> getPayReceiveInfoForUser(Long userId, Long spaceId, boolean isComplete) {
//        // TODO 1. userId에 해당하는 유저 find
//        User userByUserId = userUtils.findUserByUserId(userId);
//
//        // TODO 2. spaceId에 해당하는 space find
//        Space spaceBySpaceId = spaceUtils.findSpaceBySpaceId(spaceId);
//
//        // TODO 3. 유저가 요청받은 정산 리스트 select
//        List<PayRequestTarget> payRequestTargetListByUser = payDao.findPayRequestTargetListByUser(userByUserId, spaceBySpaceId, isComplete);
//
//        // TODO 4. return 타입 구성
//        // 3-1. 각 payRequestTarget 에 해당하는 정산 요청자, 정산 요청 금액 을 loop를 돌면서 데이터 수집
//        List<PayTargetInfoDto> payReceiveInfoDtoList = new ArrayList<>();
//
//        for (PayRequestTarget payRequestTarget : payRequestTargetListByUser) {
//            PayTargetInfoDto payReceiveInfoDto = payRequestTarget.createPayReceiveInfo();
//            payReceiveInfoDtoList.add(payReceiveInfoDto);
//        }
//
//        return payReceiveInfoDtoList;
//    }
//
//    @Transactional
//    public GetRecentPayRequestBankInfoResponse getRecentPayRequestBankInfoForUser(Long userId) {
//        // TODO 1. userId에 해당하는 유저 find
//        User userByUserId = userUtils.findUserByUserId(userId);
//
//        // TODO 2. 유저의 최근 정산받은 은행 계좌 목록 fine
//        List<RecentPayRequestBankInfoDto> recentPayRequestBankInfoByUser = payDao.findRecentPayRequestBankInfoByUser(userByUserId);
//
//        return new GetRecentPayRequestBankInfoResponse(recentPayRequestBankInfoByUser);
//    }
//
//    @Transactional
//    public List<PayRequestTarget> createPay(Long userId, Long spaceId, PostPayCreateRequest postPayCreateRequest, int unRequestedAmount) {
//        // TODO 1. userId로 User find (user : 정산 생성한 유저)
//        User payCreateUser = userUtils.findUserByUserId(userId);
//
//        // TODO 2. spaceId로 Space find
//        Space spaceBySpaceId = spaceUtils.findSpaceBySpaceId(spaceId);
//
//        // TODO 3. PayRequest 엔티티 생성
//        boolean isComplete = false;
//        PayRequest payRequest = payDao.createPayRequest(payCreateUser, spaceBySpaceId, postPayCreateRequest.getTotalAmount(), postPayCreateRequest.getBankName(), postPayCreateRequest.getBankAccountNum(), unRequestedAmount, isComplete);
//
//        // TODO 4. PayRequestTarget 엔티티 생성
//        List<PayRequestTarget> resultList = new ArrayList<>();
//        for (PostPayCreateRequest.TargetInfo targetInfo : postPayCreateRequest.getTargetInfoList()) {
//            PayRequestTarget payRequestTarget = payDao.createPayRequestTarget(payRequest, targetInfo.getTargetUserId(), targetInfo.getRequestAmount(), isComplete);
//            resultList.add(payRequestTarget);
//        }
//
//        return resultList;
//    }
//
//    /**
//     * 하나의 정산에 대한 상세정보 조회
//     */
//    @Transactional
//    public TotalPayInfoDto getTotalPayInfo(Long spaceId, Long payRequestId) {
//        // TODO 1. spaceId로 Space 엔티티 find
//        Space spaceBySpaceId = spaceUtils.findSpaceBySpaceId(spaceId);
//
//        // TODO 2. payRequestId 로 PayRequest 엔티티 find
//        PayRequest payRequestById = payDao.findPayRequestById(payRequestId);
//
//        // TODO 3. PayRequest로 해당 정산의 정보 get
//        PayRequestInfoDto payRequestInfoDto = payRequestById.createPayRequestInfo();
//
//        // TODO 4. PayRequest의 PayRequestTarget find
//        List<PayRequestTarget> payRequestTargetListByPayRequest = payDao.findPayRequestTargetListByPayRequest(payRequestById);
//
//        // TODO 5. 정산 타겟 유저 정보 get
//        List<PayTargetInfoDto> payTargetInfoDtoList = new ArrayList<>();
//        for (PayRequestTarget payRequestTarget : payRequestTargetListByPayRequest) {
//            PayTargetInfoDto payTargetInfoDto = createPayTargetInfoDto(payRequestTarget, spaceBySpaceId);
//            payTargetInfoDtoList.add(payTargetInfoDto);
//        }
//
//        // TODO 6. return 타입 구성
//        return new TotalPayInfoDto(
//                payRequestId,
//                payRequestById.getBankName(),
//                payRequestById.getBankAccountNum(),
//                payRequestById.getTotalAmount(),
//                payRequestInfoDto.getReceiveAmount(),
//                payRequestInfoDto.getTotalTargetNum(),
//                payRequestInfoDto.getReceiveTargetNum(),
//                payTargetInfoDtoList,
//                payRequestById.isComplete(),
//                payRequestById.getCreatedAt().atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime()
//        );
//    }
//
//    private PayTargetInfoDto createPayTargetInfoDto(PayRequestTarget payRequestTarget, Space space) {
//        Long targetUserId = payRequestTarget.getTargetUserId();
//        User userByUserId = userDao.findUserByUserId(targetUserId);
//
//        UserSpace userSpace = userSpaceDao.findUserSpaceByUserAndSpace(userByUserId, space)
//                .orElseThrow(() -> new CustomException(USER_IS_NOT_IN_SPACE));
//
//        String userName = userSpace.getUserName();
//        String userProfileImg = userSpace.getUserProfileImg();
//
//        return new PayTargetInfoDto(
//                payRequestTarget.getTargetUserId(),
//                userName,
//                userProfileImg,
//                payRequestTarget.getRequestAmount(),
//                payRequestTarget.isComplete()
//        );
//    }
//
//    /**
//     * 정산 타겟 유저의 정산 완료 처리
//     */
//    @Transactional
//    public PostPayCompleteResponse setPayRequestTargetToComplete(Long payRequestTargetId) {
//
//        // TODO 1. payRequestTargetId로 PayRequestTarget find
//        PayRequestTarget payRequestTargetById = payDao.findPayRequestTargetById(payRequestTargetId);
//
//        // TODO 2. 해당 PayRequestTarget 정산 완료 처리
//        payRequestTargetById.changeCompleteStatus(true);
//
//        // TODO 3. PayRequest의 완료 여부 파악
//        PayRequest payRequest = payRequestTargetById.getPayRequest();
//        boolean payRequestCompleteStatus = isPayRequestComplete(payRequest);
//        if (payRequestCompleteStatus) {
//            payRequest.changeCompleteStatus(true);
//        }
//
//        // TODO 4. return 타입 구성
//        return new PostPayCompleteResponse(
//                payRequest.getPayRequestId(),
//                payRequestCompleteStatus
//        );
//    }
//
//    private boolean isPayRequestComplete(PayRequest payRequest) {
//        List<PayRequestTarget> payRequestTargetListByPayRequest = payDao.findPayRequestTargetListByPayRequest(payRequest);
//        for (PayRequestTarget payRequestTarget : payRequestTargetListByPayRequest) {
//            if (!payRequestTarget.isComplete()) {
//                return false;
//            }
//        }
//
//        return true;
//    }
}
