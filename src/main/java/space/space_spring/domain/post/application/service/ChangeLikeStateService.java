package space.space_spring.domain.post.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.post.application.port.in.changeLikeState.ChangeLikeStateCommand;
import space.space_spring.domain.post.application.port.in.changeLikeState.ChangeLikeStateUseCase;
import space.space_spring.domain.post.application.port.out.like.ChangeLikeStatePort;
import space.space_spring.domain.post.application.port.out.like.CreateLikePort;
import space.space_spring.domain.post.domain.Like;
import space.space_spring.global.exception.CustomException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChangeLikeStateService implements ChangeLikeStateUseCase {

    private final CreateLikePort createLikePort;
    private final ChangeLikeStatePort changeLikeStatePort;

    @Transactional
    @Override
    public void changeLikeState(ChangeLikeStateCommand command) {
        try {
            changeLikeStatePort.changeLikeState(command.getTargetId(), command.getSpaceMemberId());
        } catch (CustomException e) {       // 아직 스페이스 멤버가 target에 좋아요 표시를 한 적이 없는 경우
            Like newLike = Like.withoutId(command.getTargetId(), command.getSpaceMemberId(), true);
            createLikePort.createLike(newLike);
        }
    }
}
