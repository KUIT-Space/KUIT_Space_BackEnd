package space.space_spring.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.argument_resolver.jwtLogin.JwtLoginAuth;
import space.space_spring.dto.pay.GetPayViewResponse;
import space.space_spring.dto.pay.GetRequestPayViewResponse;
import space.space_spring.dto.pay.PayReceiveInfoDto;
import space.space_spring.dto.pay.PayRequestInfoDto;
import space.space_spring.entity.UserSpace;
import space.space_spring.response.BaseResponse;
import space.space_spring.service.PayService;
import space.space_spring.util.userSpace.UserSpaceUtils;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PayController {

    private final PayService payService;
    private final UserSpaceUtils userSpaceUtils;

    /**
     * 정산 홈 view
     */
    @GetMapping("/space/{spaceId}/pay")
    public BaseResponse<GetPayViewResponse> showPayListForUser(@JwtLoginAuth Long userId, @PathVariable Long spaceId) {
        // TODO 1. 유저가 스페이스에 속하는 지 검증
        validateIsUserInSpace(userId, spaceId);

        // TODO 2. 유저가 요청한 정산 중 현재 진행중인 정산 리스트 get
        // 현재 진행중인 정산 -> isComplete = false
        List<PayRequestInfoDto> payRequestInfoDtoList = payService.getPayRequestInfoForUser(userId, spaceId, false);

        // TODO 3. 유저가 요청받은 정산 중 현재 진행중인 정산 리스트 get
        // 현재 진행중인 정산 -> isComplete = false
        List<PayReceiveInfoDto> payReceiveInfoDtoList = payService.getPayReceiveInfoForUser(userId, spaceId, false);

        return new BaseResponse<>(new GetPayViewResponse(payRequestInfoDtoList, payReceiveInfoDtoList));
    }

    private void validateIsUserInSpace(Long userId, Long spaceId) {
        // 유저가 스페이스에 속할 경우 exception이 터지지 않을 것임
        // 그렇지 않을 경우, USER_IS_NOT_IN_SPACE 예외가 터질 것임 -> 추후 exception handling 과정 필요
        userSpaceUtils.isUserInSpace(userId, spaceId);
    }

    /**
     * 내가 요청한 정산 view
     */
    @GetMapping("/space/{spaceId}/pay/request")
    public BaseResponse<GetRequestPayViewResponse> showRequestPayListForUser(@JwtLoginAuth Long userId, @PathVariable Long spaceId) {
        // TODO 1. 유저가 스페이스에 속하는 지 검증 -> 추후에 인터셉터에서 처리하게끔 리펙토링 필요
        validateIsUserInSpace(userId, spaceId);

        // TODO 2. 유저가 요청한 정산 중 현재 진행중인 정산 리스트 get
        List<PayRequestInfoDto> payRequestInfoDtoListInComplete = payService.getPayRequestInfoForUser(userId, spaceId, false);

        // TODO 3. 유저가 요청한 정산 중 완료한 정산 리스트 get
        List<PayRequestInfoDto> payRequestInfoDtoListComplete = payService.getPayRequestInfoForUser(userId, spaceId, true);

        return new BaseResponse<>(new GetRequestPayViewResponse(payRequestInfoDtoListInComplete, payRequestInfoDtoListComplete));
    }
}
