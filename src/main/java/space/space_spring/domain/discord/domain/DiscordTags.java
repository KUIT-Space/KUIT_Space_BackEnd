package space.space_spring.domain.discord.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.channel.forums.ForumTag;
import space.space_spring.domain.post.domain.Tag;

import java.util.List;

@Builder
@Getter
@Slf4j
public class DiscordTags {
    private List<DiscordTag> tags;

    public static DiscordTags from(List<ForumTag> forumTags){

        if(forumTags==null|| forumTags.isEmpty()){
            return new DiscordTags(List.of());
        }
//        forumTags.stream()
//                .peek(tag->{
//                    if(tag.getIdLong()==null){
//                        log.error("discord give me null in tag id");
//                        log.info("discord give me null in tag id");
//                    }
//                })
        return new DiscordTags(forumTags.stream().map(tag->{
            return new DiscordTag(tag.getIdLong(),tag.getName());
        }).toList());
    }


    public static DiscordTags empty(){
        return new DiscordTags(List.of());
    }


    public boolean isEmpty(){
        if(tags==null||tags.isEmpty()){
            return true;
        }
        return false;
    }


    private DiscordTags(List<DiscordTag> tags){
        this.tags=tags;
    }

    public List<Tag> getTagsWithoutId(Long boardId){
        return tags.stream().map(tag->{
            return Tag.create(null,tag.getDiscordId(),tag.getName(),boardId);
        }).toList();
    }

    private static class DiscordTag{
        @Getter
        @NotNull
        private Long discordId;
        @Getter
        @NotNull
        private String name;
        public DiscordTag(Long discordId, String name){
            this.discordId = discordId;
            this.name = name;
        }
    }


}
