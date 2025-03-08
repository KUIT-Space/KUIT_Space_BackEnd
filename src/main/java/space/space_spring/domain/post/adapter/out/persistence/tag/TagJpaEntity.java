package space.space_spring.domain.post.adapter.out.persistence.tag;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.space_spring.domain.post.adapter.out.persistence.board.BoardJpaEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "Tag")
public class TagJpaEntity {

    @Id
    @GeneratedValue
    @Column(name="tag_id")
    @NotNull
    private Long id;

    @Column(name = "tag_name")
    @NotNull
    private String tagName;

    @ManyToOne
    @JoinColumn(name = "board_id")
    @NotNull
    private BoardJpaEntity board;


}
