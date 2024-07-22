package space.space_spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.space_spring.dao.PayDao;

@Service
@RequiredArgsConstructor
public class PayService {

    private final PayDao payDao;
}
