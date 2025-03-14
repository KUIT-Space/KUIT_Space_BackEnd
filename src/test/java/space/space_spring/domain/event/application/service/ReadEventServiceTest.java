//package space.space_spring.domain.event.application.service;
//
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
//import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.UNAUTHORIZED_USER;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import space.space_spring.domain.event.application.port.out.LoadEventPort;
//import space.space_spring.domain.event.domain.Event;
//import space.space_spring.domain.event.domain.Events;
//import space.space_spring.domain.spaceMember.application.port.out.LoadSpaceMemberPort;
//import space.space_spring.domain.spaceMember.domian.SpaceMember;
//import space.space_spring.global.exception.CustomException;
//
//public class ReadEventServiceTest {
//
//    private LoadSpaceMemberPort loadSpaceMemberPort;
//    private LoadEventPort loadEventPort;
//    private ReadEventService readEventService;
//
//    private static SpaceMember manager;
//    private static Event event1;
//    private static Event event2;
//    private static Event event3;
//
//    @BeforeEach
//    void setup() {
//        loadSpaceMemberPort = Mockito.mock(LoadSpaceMemberPort.class);
//        loadEventPort = Mockito.mock(LoadEventPort.class);
//        readEventService = new ReadEventService(loadSpaceMemberPort, loadEventPort);
//
//        LocalDateTime now = LocalDateTime.now();
//        manager = SpaceMember.create(0L, 0L, 0L, 0L, "manager", "", true);
//        event1 = Event.create(0L, 0L, "event1", "", now, now, now);
//        event2 = Event.create(1L, 0L, "event2", "", now, now, now);
//        event3 = Event.create(2L, 0L, "event3", "", now, now, now);
//    }
//
//    @Test
//    @DisplayName("행사 목록을 조회한다.")
//    void readEvents() {
//        // given
//        Mockito.when(loadSpaceMemberPort.loadById(manager.getId())).thenReturn(manager);
//        Mockito.when(loadEventPort.loadEvents(manager.getSpaceId())).thenReturn(List.of(event1, event2, event3));
//
//        // when
//        Events events = readEventService.readEvents(manager.getId());
//
//        // then
//        assertThat(events.getEvents().size()).isEqualTo(3);
//    }
//
//    @Test
//    @DisplayName("한 행사의 상세 정보를 조회한다.")
//    void readEvent() {
//        // given
//        Mockito.when(loadSpaceMemberPort.loadById(manager.getId())).thenReturn(manager);
//        Mockito.when(loadEventPort.loadEvent(event1.getId())).thenReturn(Optional.ofNullable(event1));
//
//        // when
//        Event event = readEventService.readEvent(manager.getId(), event1.getId());
//
//        // then
//        assertThat(event.getId()).isEqualTo(event1.getId());
//        assertThat(event.getId()).isNotEqualTo(event2.getId());
//    }
//
//    @Test
//    @DisplayName("관리자가 아닌 경우에는 행사 목록을 조회할 수 없다.")
//    void readEvents_unauthorized() {
//        // given
//        SpaceMember spaceMember = SpaceMember.create(1L, 0L, 1L, 1L, "spaceMember", "", false);
//        Mockito.when(loadSpaceMemberPort.loadById(spaceMember.getId())).thenReturn(spaceMember);
//
//        // when & then
//        assertThatThrownBy(() -> readEventService.readEvents(spaceMember.getId()))
//                .isInstanceOf(CustomException.class)
//                .hasMessage(UNAUTHORIZED_USER.getMessage());
//    }
//
//    @Test
//    @DisplayName("관리자가 아닌 경우에는 행사 상세를 조회할 수 없다.")
//    void readEvent_unauthorized() {
//        // given
//        SpaceMember spaceMember = SpaceMember.create(1L, 0L, 1L, 1L, "spaceMember", "", false);
//        Mockito.when(loadSpaceMemberPort.loadById(spaceMember.getId())).thenReturn(spaceMember);
//        Mockito.when(loadEventPort.loadEvent(event1.getId())).thenReturn(Optional.ofNullable(event1));
//
//        // when & then
//        assertThatThrownBy(() -> readEventService.readEvent(spaceMember.getId(), event1.getId()))
//                .isInstanceOf(CustomException.class)
//                .hasMessage(UNAUTHORIZED_USER.getMessage());
//    }
//
//}
