package space.space_spring.domain.pay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.pay.model.entity.PayRequest;
import space.space_spring.domain.pay.model.entity.PayRequestTarget;
import space.space_spring.domain.space.model.entity.Space;

import java.util.List;
import java.util.Optional;

@Repository
public interface PayRequestTargetRepository extends JpaRepository<PayRequestTarget, Long> {

    @Query("select prt from PayRequestTarget prt where prt.targetUserId = :userId and prt.payRequest.space = :space and prt.isComplete = :isComplete and prt.status = 'ACTIVE'")
    List<PayRequestTarget> findAllByUserAndSpace(Long userId, Space space, boolean isComplete);

    @Query("select prt from PayRequestTarget prt where prt.payRequest = :payRequest and prt.status = 'ACTIVE'")
    List<PayRequestTarget> findAllByPayRequest(PayRequest payRequest);

}
