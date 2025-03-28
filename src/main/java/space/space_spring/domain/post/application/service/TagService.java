package space.space_spring.domain.post.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import space.space_spring.domain.discord.domain.DiscordTags;
import space.space_spring.domain.post.application.port.in.Tag.CreateTagUseCase;
import space.space_spring.domain.post.application.port.in.Tag.LoadTagUseCase;
import space.space_spring.domain.post.application.port.out.CreateTagPort;
import space.space_spring.domain.post.application.port.out.LoadTagPort;
import space.space_spring.domain.post.domain.Tag;

import java.util.List;
import java.util.Map;
@Service
@RequiredArgsConstructor
@Slf4j
public class TagService implements CreateTagUseCase, LoadTagUseCase {
    private final CreateTagPort createTagPort;
    private final LoadTagPort loadTagPort;
    @Override
    public List<Tag> create(DiscordTags tags,Long boardId){
        if(tags.isEmpty()){
            log.info("tags is empty");
            return null;
        }
        return createTagPort.save(tags.getTagsWithoutId(boardId));


    }
    @Override
    public List<Tag> findByDiscordId(List<Long> discordIds){

        return loadTagPort.loadByDiscordId(discordIds);

    }

}
