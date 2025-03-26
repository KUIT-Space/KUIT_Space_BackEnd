package space.space_spring.domain.home.application.port.in;

import space.space_spring.domain.home.adapter.in.web.ReadHomeResult;

public interface ReadHomeUseCase {
    ReadHomeResult readHome(Long spaceMemberId, Long spaceId);
}
