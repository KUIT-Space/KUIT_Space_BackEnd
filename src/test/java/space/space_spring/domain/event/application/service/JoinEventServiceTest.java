package space.space_spring.domain.event.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.ALREADY_IN_EVENT;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import space.space_spring.domain.event.application.port.out.CreateEventParticipantPort;
import space.space_spring.domain.event.application.port.out.LoadEventParticipantPort;
import space.space_spring.domain.event.domain.Event;
import space.space_spring.domain.event.domain.EventParticipant;
import space.space_spring.domain.event.domain.EventParticipants;
import space.space_spring.domain.spaceMember.domian.SpaceMember;
import space.space_spring.global.exception.CustomException;

public class JoinEventServiceTest {

    private CreateEventParticipantPort createEventParticipantPort;
    private LoadEventParticipantPort loadEventParticipantPort;
    private JoinEventService joinEventService;

    private static SpaceMember spaceMember;
    private static Event event;
    private static EventParticipant eventParticipant;

    @BeforeEach
    void setup() {
        createEventParticipantPort = Mockito.mock(CreateEventParticipantPort.class);
        loadEventParticipantPort = Mockito.mock(LoadEventParticipantPort.class);
        joinEventService = new JoinEventService(createEventParticipantPort, loadEventParticipantPort);

        LocalDateTime now = LocalDateTime.now();

        spaceMember = SpaceMember.create(0L, 5L, 0L, 0L, "spaceMember", "", false);
        event  = Event.create(1L, 5L, "event", "", now, now, now);
        eventParticipant = EventParticipant.create(2L, 1L, 0L);
    }

    @Test
    @DisplayName("사용자는 행사에 참여한다.")
    void joinEvent() {
        // given
        Mockito.when(loadEventParticipantPort.loadByEventId(event.getId())).thenReturn(EventParticipants.create(List.of()));
        Mockito.when(createEventParticipantPort.createParticipant(any(EventParticipant.class))).thenReturn(eventParticipant);

        // when
        boolean isSuccess = joinEventService.joinEvent(spaceMember.getId(), event.getId());

        // then
        assertThat(isSuccess).isTrue();

    }

    @Test
    @DisplayName("사용자는 이미 참여한 행사에 중복 참여할 수 없다.")
    void joinEvent_duplicate() {
        // given
        Mockito.when(loadEventParticipantPort.loadByEventId(event.getId())).thenReturn(EventParticipants.create(List.of(eventParticipant)));

        // when & then
        assertThatThrownBy(() -> joinEventService.joinEvent(spaceMember.getId(), event.getId()))
                .isInstanceOf(CustomException.class)
                .hasMessage(ALREADY_IN_EVENT.getMessage());
    }
}
