package space.space_spring.domain.post.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.post.application.port.in.createComment.CreateCommentFromDiscordCommand;
import space.space_spring.domain.post.application.port.in.createComment.CreateCommentFromWebCommand;
import space.space_spring.domain.post.application.port.in.createComment.CreateCommentUseCase;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CreateCommentService implements CreateCommentUseCase {


    @Override
    @Transactional
    public Long createCommentFromWeb(CreateCommentFromWebCommand command) {

    }

    @Override
    public Long createCommentFromDiscord(CreateCommentFromDiscordCommand command) {

    }
}
