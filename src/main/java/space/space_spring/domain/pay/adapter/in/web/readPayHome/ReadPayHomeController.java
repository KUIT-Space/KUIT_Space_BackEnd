package space.space_spring.domain.pay.adapter.in.web.readPayHome;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.domain.pay.application.port.in.readPayHome.ReadPayHomeUseCase;
import space.space_spring.global.common.response.BaseResponse;

@RestController
@RequiredArgsConstructor
public class ReadPayHomeController {

    private final ReadPayHomeUseCase readPayHomeUseCase;

    @GetMapping("/pay")
    public BaseResponse<>
}
