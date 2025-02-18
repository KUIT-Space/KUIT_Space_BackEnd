package space.space_spring.domain.event.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EventParticipantInfo {

    private Long id;

    private String name;

    private String profileImageUrl;

    public static EventParticipantInfo create(Long id, String name, String profileImageUrl) {
        return new EventParticipantInfo(id, name, profileImageUrl);
    }

}
