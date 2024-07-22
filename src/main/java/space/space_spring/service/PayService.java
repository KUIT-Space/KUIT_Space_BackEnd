package space.space_spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.space_spring.dao.PayDao;
import space.space_spring.dto.pay.PayReceiveInfoDto;
import space.space_spring.dto.pay.PayRequestInfoDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PayService {

    private final PayDao payDao;

    public List<PayRequestInfoDto> getPayRequestInfoForUser(Long userId, Long spaceId) {
        
    }

    public List<PayReceiveInfoDto> getPayReceiveInfoForUser(Long userId, Long spaceId) {
    }
}
