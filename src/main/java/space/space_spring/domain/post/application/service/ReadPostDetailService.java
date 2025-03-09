package space.space_spring.domain.post.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.space_spring.domain.post.application.port.in.readPostDetail.ReadPostDetailCommand;
import space.space_spring.domain.post.application.port.in.readPostDetail.ReadPostDetailUseCase;
import space.space_spring.domain.post.application.port.in.readPostDetail.ResultOfReadPostDetail;

@Service
@RequiredArgsConstructor
public class ReadPostDetailService implements ReadPostDetailUseCase {

    @Override
    public ResultOfReadPostDetail readPostDetail(ReadPostDetailCommand command) {
        return null;
    }
}
