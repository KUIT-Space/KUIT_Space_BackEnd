package space.space_spring.domain.post.adapter.out.persistence.tag;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.space_spring.domain.post.adapter.out.persistence.board.BoardJpaEntity;
import space.space_spring.global.common.entity.BaseJpaEntity;
import space.space_spring.global.common.enumStatus.BaseStatusType;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "Tag")
public class TagJpaEntity extends BaseJpaEntity {

    @Id
    @GeneratedValue
    @Column(name="tag_id")
    @NotNull
    private Long id;

    @Column(name = "tag_name")
    @NotNull
    private String tagName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    @NotNull
    private BoardJpaEntity board;

    @Builder
    private TagJpaEntity(String tagName, BoardJpaEntity board, LocalDateTime createdAt, LocalDateTime lastModifiedAt, BaseStatusType baseStatus) {
        super(createdAt, lastModifiedAt, baseStatus);
        this.tagName = tagName;
        this.board = board;
    }

    public static TagJpaEntity create(String tagName, BoardJpaEntity board, LocalDateTime createdAt, LocalDateTime lastModifiedAt, BaseStatusType baseStatus) {
        return TagJpaEntity.builder()
                .tagName(tagName)
                .board(board)
                .createdAt(createdAt)
                .lastModifiedAt(lastModifiedAt)
                .baseStatus(baseStatus)
                .build();
    }

}
