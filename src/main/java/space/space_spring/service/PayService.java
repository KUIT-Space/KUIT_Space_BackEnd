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
        // TODO 1. userId에 해당하는 user find


        // TODO 1. 유저가 요청한 정산 중 진행 중인 정산 리스트 select
        payDao.findPayRequestListByUser()

    }

    public List<PayReceiveInfoDto> getPayReceiveInfoForUser(Long userId, Long spaceId) {
    }
}
