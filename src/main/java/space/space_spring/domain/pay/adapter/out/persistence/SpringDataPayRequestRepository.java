package space.space_spring.domain.pay.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataPayRequestRepository extends JpaRepository<PayRequestJpaEntity, Long> {

}
