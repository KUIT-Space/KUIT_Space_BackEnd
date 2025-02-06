package space.space_spring.domain.space.application.port.out;

public interface CreateSpacePort {

    Long saveSpace(Long discordGuildId,String spaceName);
}
