package space.space_spring.domain.post.adapter.out.persistence.tag;


import static space.space_spring.global.common.enumStatus.BaseStatusType.ACTIVE;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.BOARD_NOT_FOUND;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.TAG_NOT_FOUND;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.post.adapter.out.persistence.board.BoardJpaEntity;
import space.space_spring.domain.post.adapter.out.persistence.board.SpringDataBoardRepository;
import space.space_spring.domain.post.application.port.out.CreateTagPort;
import space.space_spring.domain.post.application.port.out.LoadTagPort;
import space.space_spring.domain.post.domain.Tag;
import space.space_spring.global.exception.CustomException;

import java.util.List;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.*;


@Repository
@RequiredArgsConstructor
public class TagPersistenceAdapter implements LoadTagPort, CreateTagPort {

    private final SpringDataBoardRepository boardRepository;
    private final SpringDataTagRepository tagRepository;
    private final TagMapper tagMapper;

    @Override
    public Tag loadByIdAndBoard(Long tagId, Long boardId) {
        return tagRepository.findByIdAndBoardIdAndStatus(tagId, boardId, ACTIVE).map(tagMapper::toDomainEntity)
                .orElseThrow(() -> new CustomException(TAG_NOT_FOUND));
    }



    public List<Tag> save(List<Tag> tags){
        if(tags==null||tags.isEmpty()){
            return List.of();
        }
        validateTagOnSameBoard(tags);
        BoardJpaEntity boardJpaEntity = boardRepository.findById(tags.get(0).getBoardId()).orElseThrow(()-> {
            throw new CustomException(BOARD_NOT_FOUND);}
        );
        List<TagJpaEntity> tagJpaEntities = tagRepository.saveAll(tags.stream().map(tag->tagMapper.toJpaEntity(tag,boardJpaEntity)).toList());

        return tagJpaEntities.stream().map(tagMapper::toDomainEntity).toList();
    }

    private void validateTagOnSameBoard(List<Tag> tags){
        if( !(tags.stream()
                .map(Tag::getBoardId)
                .distinct()
                .limit(2)
                .count()<=1)
        ){
            throw new CustomException(TAGS_IS_WORNG);
        }
    }
    @Override
    public List<Tag> loadTagsByBoardIds(List<Long> boardIds) {
        return tagRepository.findTagsByBoardIds(boardIds).stream().map(tagMapper::toDomainEntity).toList();

    }


    public List<Tag> loadByDiscordId(List<Long> discordIdOfTag) {
        List<TagJpaEntity> allByIdAndStatus = tagRepository.findAllByDiscordIdAndStatus(discordIdOfTag, ACTIVE);

        return allByIdAndStatus.stream().map(tagMapper::toDomainEntity).toList();
    }

    @Override
    public List<Tag> loadAllByIds(List<Long> tagIds) {
        List<TagJpaEntity> allByIdAndStatus = tagRepository.findAllByIdAndStatus(tagIds, ACTIVE);

        return allByIdAndStatus.stream().map(tagMapper::toDomainEntity).toList();

    }

    @Override
    public Tag loadById(Long tagId) {
        return tagRepository.findByIdAndStatus(tagId, ACTIVE).map(tagMapper::toDomainEntity).orElseThrow(() -> new CustomException(TAG_NOT_FOUND));
    }
}
