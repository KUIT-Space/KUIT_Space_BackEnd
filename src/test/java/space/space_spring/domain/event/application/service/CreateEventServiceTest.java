package space.space_spring.domain.event.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.UNAUTHORIZED_USER;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import space.space_spring.domain.event.application.port.in.CreateEventCommand;
import space.space_spring.domain.event.application.port.out.CreateEventPort;
import space.space_spring.domain.event.domain.Event;
import space.space_spring.domain.spaceMember.application.port.out.LoadSpaceMemberPort;
import space.space_spring.domain.spaceMember.domian.SpaceMember;
import space.space_spring.global.exception.CustomException;

public class CreateEventServiceTest {

    private CreateEventPort createEventPort;
    private LoadSpaceMemberPort loadSpaceMemberPort;
    private CreateEventService createEventService;

    private static SpaceMember manager;
    private static SpaceMember general;
    private static CreateEventCommand createEventCommand;
    private static Event event;

    @BeforeEach
    void setup() {
        createEventPort = Mockito.mock(CreateEventPort.class);
        loadSpaceMemberPort = Mockito.mock(LoadSpaceMemberPort.class);
        createEventService = new CreateEventService(createEventPort, loadSpaceMemberPort);

        LocalDateTime now = LocalDateTime.now();

        manager = SpaceMember.create(0L, 0L, 0L, 0L, "manager", "", true);
        general = SpaceMember.create(1L, 0L, 1L, 1L, "general", "", false);
        createEventCommand = new CreateEventCommand("event", "", now, now, now);
        event  = Event.create(0L, 0L, "event", "", now, now, now);

        Mockito.when(loadSpaceMemberPort.loadById(manager.getId())).thenReturn(manager);
        Mockito.when(loadSpaceMemberPort.loadById(general.getId())).thenReturn(general);
    }

    @Test
    @DisplayName("스페이스 관리자가 행사를 생성한다.")
    void createEvent() {
        // Given
        Mockito.when(createEventPort.createEvent(any(Event.class))).thenReturn(event); // ID 있는 Event 반환

        // When
        Long eventId = createEventService.createEvent(manager.getId(), createEventCommand);

        // Then
        assertThat(eventId).isEqualTo(event.getId());
        verify(createEventPort).createEvent(any(Event.class));
    }

    @Test
    @DisplayName("스페이스 관리자가 아닌 사용자는 행사를 생성할 수 없다.")
    void createEvent_unauthorized() {
        assertThatThrownBy(() -> createEventService.createEvent(general.getId(), createEventCommand))
                .isInstanceOf(CustomException.class)
                .hasMessage(UNAUTHORIZED_USER.getMessage());
    }
}
