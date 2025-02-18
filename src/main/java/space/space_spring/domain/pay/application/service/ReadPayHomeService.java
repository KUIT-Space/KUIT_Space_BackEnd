package space.space_spring.domain.pay.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.pay.application.port.in.readPayHome.ReadPayHomeUseCase;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReadPayHomeService implements ReadPayHomeUseCase {


}
