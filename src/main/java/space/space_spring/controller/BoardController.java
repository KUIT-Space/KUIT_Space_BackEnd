package space.space_spring.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.service.BoardService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/space/{spaceId}/board")
@Slf4j
public class BoardController {
    private final BoardService postService;

}
