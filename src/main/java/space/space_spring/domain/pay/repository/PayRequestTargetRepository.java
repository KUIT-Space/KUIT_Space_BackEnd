package space.space_spring.domain.pay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.pay.model.entity.PayRequestTarget;
import space.space_spring.domain.user.model.entity.User;
import space.space_spring.entity.Space;

import java.util.List;
import java.util.Optional;

@Repository
public interface PayRequestTargetRepository extends JpaRepository<PayRequestTarget, Long> {

    @Query("select prt from PayRequestTarget prt where prt.targetUserId = :userId and prt.payRequest.space = :space and prt.payRequest.isComplete = :isComplete and prt.status = 'ACTIVE'")
    List<Optional<PayRequestTarget>> findByUserAndSpace(Long userId, Space space, boolean isComplete);


}
