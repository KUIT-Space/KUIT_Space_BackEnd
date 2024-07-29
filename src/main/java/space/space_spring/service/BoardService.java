package space.space_spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.space_spring.dao.BoardDao;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardDao boardDao;


}
