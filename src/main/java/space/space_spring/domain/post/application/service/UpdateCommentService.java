package space.space_spring.domain.post.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.space_spring.domain.post.application.port.in.updateComment.UpdateCommentCommand;
import space.space_spring.domain.post.application.port.in.updateComment.UpdateCommentUseCase;

@Service
@RequiredArgsConstructor
public class UpdateCommentService implements UpdateCommentUseCase {


    @Override
    public void updateComment(UpdateCommentCommand command) {

    }
}
