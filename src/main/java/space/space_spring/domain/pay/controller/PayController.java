package space.space_spring.domain.pay.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import space.space_spring.argumentResolver.jwtLogin.JwtLoginAuth;
import space.space_spring.domain.pay.model.request.PayCreateRequest;
import space.space_spring.domain.pay.model.response.*;
import space.space_spring.exception.CustomException;
import space.space_spring.response.BaseResponse;
import space.space_spring.domain.pay.service.PayService;
import space.space_spring.util.pay.PayUtils;
import space.space_spring.util.user.UserUtils;
import space.space_spring.util.userSpace.UserSpaceUtils;

import static space.space_spring.response.status.BaseExceptionResponseStatus.*;
import static space.space_spring.util.bindingResult.BindingResultUtils.getErrorMessage;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PayController {

    private final PayService payService;
    private final UserSpaceUtils userSpaceUtils;
    private final UserUtils userUtils;
    private final PayUtils payUtils;

    /**
     * 정산 홈 view
     */
    @GetMapping("/space/{spaceId}/pay")
    public BaseResponse<PayHomeViewResponse> showPayHome(@JwtLoginAuth Long userId, @PathVariable Long spaceId) {

        return new BaseResponse<>(payService.getPayHomeInfos(userId, spaceId));
    }

//    private void validateIsUserInSpace(Long userId, Long spaceId) {
//        // 유저가 스페이스에 속할 경우 exception이 터지지 않을 것임
//        // 그렇지 않을 경우, USER_IS_NOT_IN_SPACE 예외가 터질 것임 -> 추후 exception handling 과정 필요
//        userSpaceUtils.isUserInSpace(userId, spaceId);
//    }
//
//    /**
//     * 내가 요청한 정산 조회
//     */
//    @GetMapping("/space/{spaceId}/pay/request")
//    public BaseResponse<GetRequestPayViewResponse> showRequestPayListForUser(@JwtLoginAuth Long userId, @PathVariable Long spaceId) {
//        // TODO 1. 유저가 스페이스에 속하는 지 검증 -> 추후에 인터셉터에서 처리하게끔 리펙토링 필요
//        validateIsUserInSpace(userId, spaceId);
//
//        // TODO 2. 유저가 요청한 정산 중 현재 진행중인 정산 리스트 get -> 아직 완료되지 않은 정산 : isComplete = false
//        List<PayRequestInfoDto> payRequestInfoDtoListInComplete = payService.getPayRequestInfoForUser(userId, spaceId, false);
//
//        // TODO 3. 유저가 요청한 정산 중 완료한 정산 리스트 get -> 완료된 정산 : isComplete = true
//        List<PayRequestInfoDto> payRequestInfoDtoListComplete = payService.getPayRequestInfoForUser(userId, spaceId, true);
//
//        return new BaseResponse<>(new GetRequestPayViewResponse(payRequestInfoDtoListInComplete, payRequestInfoDtoListComplete));
//    }
//
//    /**
//     * 내가 요청받은 정산 조회
//     */
//    @GetMapping("/space/{spaceId}/pay/receive")
//    public BaseResponse<GetReceivePayViewResponse> showReceivePayListForUser(@JwtLoginAuth Long userId, @PathVariable Long spaceId) {
//        // TODO 1. 유저가 스페이스에 속하는 지 검증 -> 추후에 인터셉터에서 처리하게끔 리펙토링 필요
//        validateIsUserInSpace(userId, spaceId);
//
//        // TODO 2. 유저가 요청받은 정산 중 현재 진행중인 정산 리스트 get -> 정산 타겟 유저가 정산 안했을 경우 : isComplete = false
//        List<PayTargetInfoDto> payReceiveInfoDtoListInComplete = payService.getPayReceiveInfoForUser(userId, spaceId, false);
//
//        // TODO 3. 유저가 요청받은 정산 중 완료한 정산 리스트 get -> 정산 타겟 유저가 정산 했을 경우 : isComplete = true
//        List<PayTargetInfoDto> payReceiveInfoDtoListComplete = payService.getPayReceiveInfoForUser(userId, spaceId, true);
//
//        return new BaseResponse<>(new GetReceivePayViewResponse(payReceiveInfoDtoListInComplete, payReceiveInfoDtoListComplete));
//    }
//
//
//    /**
//     * 유저가 최근 정산받은 은행 계좌 정보 조회
//     * 해당 api는 유저가 속한 스페이스의 정보가 필요없다고 판단해서 spaceId 를 request로 받지 않음
//     */
//    @CheckUserSpace(required = false)
//    @GetMapping("/space/pay/recent-bank-info")
//    public BaseResponse<GetRecentPayRequestBankInfoResponse> showRecentBankInfo(@JwtLoginAuth Long userId) {
//
//        return new BaseResponse<>(payService.getRecentPayRequestBankInfoForUser(userId));
//    }
//

    /**
     * 정산 생성
     * response 추가 협의 필요 -> 굳이 PayRequestId를 response 안해도 될꺼같음
     */
    public BaseResponse<String> createPay(@JwtLoginAuth Long userId, @PathVariable Long spaceId, @Validated @RequestBody PayCreateRequest payCreateRequest, BindingResult bindingResult) {
        // request 입력 모델 유효성 검증
        if (bindingResult.hasErrors()) {
            throw new CustomException(INVALID_PAY_CREATE, getErrorMessage(bindingResult));
        }

        payService.createPay(userId, spaceId, payCreateRequest.toServiceRequest());

    }





//    @PostMapping("/space/{spaceId}/pay")
//    public BaseResponse<String> createPay(@JwtLoginAuth Long userId, @PathVariable Long spaceId, @Validated @RequestBody PayCreateRequest payCreateRequest, BindingResult bindingResult) {
//
//        // TODO 1. request dto validation
//        if (bindingResult.hasErrors()) {
//            throw new CustomException(INVALID_PAY_CREATE, getErrorMessage(bindingResult));
//        }
//
//        // TODO 2. 유저가 스페이스에 속하는 지 검증
//        validateIsUserInSpace(userId, spaceId);
//
//        // TODO 3. PostPayCreateRequest의 targetInfoList 유저들이 모두 해당 스페이스에 속하는지 검증
//        // 현재 검증 시 에러가 발생하면 그냥 "스페이스에 속하는 유저가 아닙니다" 라는 에러메시지만 나오고,
//        // 어떤 유저가 스페이스에 속하지 않는지에 대한 정보가 없음
//        // => 추후 에러메시지의 수정이 필요할듯??
//        for (PayCreateRequest.TargetInfo targetInfo : payCreateRequest.getTargetInfoList()) {
//            validateIsUserInSpace(targetInfo.getTargetUserId(), spaceId);
//        }
//
//        // TODO 4. PostPayCreateRequest의 bankName, bankAccountNum 검증
//        // => 추후에 은행 이름들의 목록을 enum으로 만들어서 관리 & 유효성 검사 하도록 ??
//
//        // TODO 5. 정산 요청 금액의 유효성 검사
//        // return 값 : 미정산 금액
//        int unRequestedAmount = validatePayAmount(payCreateRequest);
//
//        // TODO 6. 정산 생성
//        payService.createPay(userId, spaceId, payCreateRequest, unRequestedAmount);
//
//        return new BaseResponse<>("정산 생성 성공");
//    }
//
//    private int validatePayAmount(PostPayCreateRequest postPayCreateRequest) {
//
//        // 1. requestedAmount 들의 합이 totalAmount 와 일치하는지 확인
//
//        // 2-1. 만약 일치한다면 유효성 검사 통과
//        // 2-2. 만약 일치하지 않는다면, totalAmount와 requestedAmount들의 합의 차이가 n-1보다 작거나 같은지 확인
//
//        // 3-1, 만약 n-1보다 작거나 같은 경우, 유효성 검사 통과 ('미정산 금액' 확인)
//        // 3-2, 만약 n-1보다 클 경우, 유효성 검사 통과 X (예외 처리)
//
//        int totalRequestAmount = calculateSumOfRequestAmount(postPayCreateRequest.getTargetInfoList());
//        int totalAmount = postPayCreateRequest.getTotalAmount();
//        int targetSize = postPayCreateRequest.getTargetInfoList().size();
//        int unRequestedAmount = 0;
//
//        if (totalRequestAmount != totalAmount) {
//            if ((totalAmount - totalRequestAmount) <= (targetSize - 1)) {
//                unRequestedAmount = totalAmount - totalRequestAmount;
//                return unRequestedAmount;
//            }
//
//            throw new CustomException(INVALID_PAY_AMOUNT);
//        }
//
//        return unRequestedAmount;
//    }
//
//    private int calculateSumOfRequestAmount(List<PostPayCreateRequest.TargetInfo> targetInfoList) {
//        int totalRequestAmount = 0;
//        for (PostPayCreateRequest.TargetInfo targetInfo : targetInfoList) {
//            totalRequestAmount += targetInfo.getRequestAmount();
//        }
//        return totalRequestAmount;
//    }
//
//    /**
//     * 하나의 정산에 대한 상세정보 조회
//     */
//    @GetMapping("/space/{spaceId}/pay/{payRequestId}")
//    public BaseResponse<TotalPayInfoDto> showTotalPayInfo(@JwtLoginAuth Long userId, @PathVariable Long spaceId, @PathVariable Long payRequestId) {
//
//        // TODO 1. 유저가 스페이스에 속하는 지 검증
//        validateIsUserInSpace(userId, spaceId);
//
//        // TODO 2. 정산 상세 정보 조회
//        return new BaseResponse<>(payService.getTotalPayInfo(spaceId, payRequestId));
//    }
//
//    /**
//     * 정산 타겟 유저의 정산 완료 처리
//     */
//    @PostMapping("/space/{spaceId}/pay/complete")
//    public BaseResponse<PostPayCompleteResponse> setPayComplete(@JwtLoginAuth Long userId, @PathVariable Long spaceId, @RequestBody PostPayCompleteRequest postPayCompleteRequest) {
//
//        // TODO 1. 유저가 스페이스에 속하는 지 검증
//        validateIsUserInSpace(userId, spaceId);
//
//        // TODO 2. 유저와 정산 타겟 유저가 일치하는지 검증
//        checkPayRequestTargetUser(userId, postPayCompleteRequest.getPayRequestTargetId());
//
//        // TODO 3. 정산 타겟 유저의 정산 완료 처리
//        return new BaseResponse<>(payService.setPayRequestTargetToComplete(postPayCompleteRequest.getPayRequestTargetId()));
//    }
//
//    private void checkPayRequestTargetUser(Long userId, Long payRequestTargetId) {
//
//        User userByUserId = userUtils.findUserByUserId(userId);
//        PayRequestTarget payRequestTargetById = payUtils.findPayRequestTargetById(payRequestTargetId);
//
//        Long realUserId = userByUserId.getUserId();
//        Long targetUserId = payRequestTargetById.getTargetUserId();
//
//        if (!realUserId.equals(targetUserId)) {
//            throw new CustomException(INVALID_PAY_REQUEST_TARGET_ID);
//        }
//    }

}
