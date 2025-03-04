package space.space_spring.domain.event.adapter.in.web.updateEvent;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.INVALID_EVENT_CREATE;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.domain.event.application.port.in.ModifyEventUseCase;
import space.space_spring.domain.event.application.port.in.UpdateEventParticipantCommand;
import space.space_spring.global.argumentResolver.jwtLogin.JwtLoginAuth;
import space.space_spring.global.argumentResolver.jwtLogin.JwtSpaceId;
import space.space_spring.global.common.response.BaseResponse;
import space.space_spring.global.common.response.SuccessResponse;
import space.space_spring.global.exception.CustomException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/space/{spaceId}")
@Tag(name = "Event", description = "행사 관련 API")
public class UpdateEventParticipantController {

    private final ModifyEventUseCase modifyEventUseCase;

    @Operation(summary = "행사 참여자 추가", description = """
        
        관리자가 행사 참여자를 추가합니다.
        
        """)
    @PostMapping("/event/{eventId}/participant/update")
    public BaseResponse<SuccessResponse> addEventParticipant(@JwtLoginAuth Long spaceMemberId, @JwtSpaceId Long spaceId, @PathVariable Long eventId, @Validated @RequestBody UpdateEventParticipantRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomException(INVALID_EVENT_CREATE);
        }

        UpdateEventParticipantCommand updateEventParticipantCommand = UpdateEventParticipantCommand.builder()
                .spaceMemberIds(request.getSpaceMemberId()).build();

        boolean isJoinSuccess = modifyEventUseCase.addParticipants(spaceMemberId, eventId, updateEventParticipantCommand);
        return new BaseResponse<>(new SuccessResponse(isJoinSuccess));
    }

    @Operation(summary = "행사 참여자 삭제", description = """
        
        관리자가 행사 참여자를 삭제합니다.
        
        """)
    @PostMapping("/event/{eventId}/participant/delete")
    public BaseResponse<SuccessResponse> deleteEventParticipant(@JwtLoginAuth Long spaceMemberId, @JwtSpaceId Long spaceId, @PathVariable Long eventId, @Validated @RequestBody UpdateEventParticipantRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomException(INVALID_EVENT_CREATE);
        }

        UpdateEventParticipantCommand updateEventParticipantCommand = UpdateEventParticipantCommand.builder()
                .spaceMemberIds(request.getSpaceMemberId()).build();

        boolean isDeleteSuccess = modifyEventUseCase.deleteParticipants(spaceMemberId, eventId, updateEventParticipantCommand);
        return new BaseResponse<>(new SuccessResponse(isDeleteSuccess));
    }
}
