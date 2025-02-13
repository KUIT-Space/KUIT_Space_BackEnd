package space.space_spring.domain.post.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.post.application.port.in.createPost.CreatePostCommand;
import space.space_spring.domain.post.application.port.in.createPost.CreatePostUseCase;
import space.space_spring.domain.post.application.port.out.CreatePostBasePort;
import space.space_spring.domain.post.application.port.out.CreatePostPort;
import space.space_spring.domain.post.domain.PostBase;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CreatePostService implements CreatePostUseCase {

//    private final CreatePostBasePort createPostBasePort;
//    private final CreatePostPort createPostPort;

    @Override
    @Transactional
    public Long createPost(CreatePostCommand command) {
//
//        // 1. PostBase 생성
//        PostBase postBase = PostBase.withoutId(
//                command.getPostCreatorId(),
//                command.getBoardId(),
//                command.getContent()
//        );
//        Long postBaseId = createPostBasePort.createPostBase(postBase);
    }
}
