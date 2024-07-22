package space.space_spring.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.service.PayService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PayController {

    private final PayService payService;


}
