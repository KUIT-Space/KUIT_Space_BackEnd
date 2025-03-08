package space.space_spring.domain.post.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.post.application.port.in.changeLikeState.ChangeLikeStateCommand;
import space.space_spring.domain.post.application.port.in.changeLikeState.ChangeLikeStateUseCase;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChangeLikeStateService implements ChangeLikeStateUseCase {

    @Override
    public void changeLikeState(ChangeLikeStateCommand command) {

    }
}
