package space.space_spring.domain.space.domain;

import lombok.Getter;

@Getter
public class Space {

    private Long id;

    private String name;

    private Long discordId;

    private Space(Long id, String name, Long discordId) {
        this.id = id;
        this.name = name;
        this.discordId = discordId;
    }

    public static Space create(Long id, String name, Long discordId) {
        return new Space(id, name, discordId);
    }

    public static Space withoutId(Long discordId, String name){
        return new Space(null, name, discordId);
    }
}
