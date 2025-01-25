package space.space_spring.domain.pay.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.pay.application.port.in.readPayRequestList.ReadPayRequestListUseCase;
import space.space_spring.domain.pay.application.port.in.readPayRequestList.ResultOfReadPayRequestList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReadPayRequestListService implements ReadPayRequestListUseCase {

    @Override
    public ResultOfReadPayRequestList readPayRequestList(Long payCreatorId) {
        return null;
    }
}
