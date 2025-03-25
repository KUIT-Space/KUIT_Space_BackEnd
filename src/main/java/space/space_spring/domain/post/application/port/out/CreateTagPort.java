package space.space_spring.domain.post.application.port.out;

import space.space_spring.domain.post.domain.Tag;

import java.util.List;

public interface CreateTagPort {
    List<Tag> save(List<Tag> tags);
}
