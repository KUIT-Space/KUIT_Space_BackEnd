package space.space_spring.domain.event.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.UNAUTHORIZED_USER;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import space.space_spring.domain.event.application.port.out.LoadEventParticipantPort;
import space.space_spring.domain.event.domain.Event;
import space.space_spring.domain.event.domain.EventParticipant;
import space.space_spring.domain.event.domain.EventParticipantInfos;
import space.space_spring.domain.event.domain.EventParticipants;
import space.space_spring.domain.spaceMember.application.port.out.LoadSpaceMemberPort;
import space.space_spring.domain.spaceMember.domian.SpaceMember;
import space.space_spring.global.exception.CustomException;

public class ReadEventParticipantServiceTest {

    private LoadSpaceMemberPort loadSpaceMemberPort;
    private LoadEventParticipantPort loadEventParticipantPort;
    private ReadEventParticipantService readEventParticipantService;

    private static SpaceMember manager;
    private static SpaceMember spaceMember1;
    private static SpaceMember spaceMember2;
    private static SpaceMember spaceMember3;
    private static Event event;
    private static EventParticipants eventParticipants;

    @BeforeEach
    void setup() {
        loadSpaceMemberPort = Mockito.mock(LoadSpaceMemberPort.class);
        loadEventParticipantPort = Mockito.mock(LoadEventParticipantPort.class);
        readEventParticipantService = new ReadEventParticipantService(loadSpaceMemberPort, loadEventParticipantPort);

        LocalDateTime now = LocalDateTime.now();

        manager = SpaceMember.create(0L, 0L, 0L, 0L, "manager", "", true);
        spaceMember1 = SpaceMember.create(1L, 0L, 1L, 1L, "spaceMember1", "", false);
        spaceMember2 = SpaceMember.create(2L, 0L, 2L, 2L, "spaceMember2", "", false);
        spaceMember3 = SpaceMember.create(3L, 0L, 3L, 3L, "spaceMember3", "", false);
        event = Event.create(0L, 0L, "event1", "", now, now, now);

        EventParticipant participant1 = EventParticipant.create(0L, 0L, 1L);
        EventParticipant participant2 = EventParticipant.create(1L, 0L, 2L);
        EventParticipant participant3 = EventParticipant.create(2L, 0L, 3L);
        eventParticipants = EventParticipants.create(List.of(participant1, participant2, participant3));
    }

    @Test
    @DisplayName("행사에 참여하는 사람들의 목록을 읽어온다.")
    void readEventParticipants() {
        // given
        Mockito.when(loadSpaceMemberPort.loadById(manager.getId())).thenReturn(manager);
        Mockito.when(loadEventParticipantPort.loadByEventId(event.getId())).thenReturn(eventParticipants);
        Mockito.when(loadSpaceMemberPort.loadAllById(eventParticipants.getSpaceMemberIds())).thenReturn(List.of(spaceMember1, spaceMember2, spaceMember3));

        // when
        EventParticipantInfos eventParticipantInfos = readEventParticipantService.readEventParticipants(manager.getId(), event.getId());

        // then
        assertThat(eventParticipantInfos.getParticipantInfos().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("관리자가 아닌 경우에는 행사에 참여하는 사람들의 목록을 읽어올 수 없다.")
    void readEventParticipants_unauthorized() {
        // given
        Mockito.when(loadSpaceMemberPort.loadById(spaceMember1.getId())).thenReturn(spaceMember1);

        // when & then
        assertThatThrownBy(() -> readEventParticipantService.readEventParticipants(spaceMember1.getId(), event.getId()))
                .isInstanceOf(CustomException.class)
                .hasMessage(UNAUTHORIZED_USER.getMessage());
    }
}
