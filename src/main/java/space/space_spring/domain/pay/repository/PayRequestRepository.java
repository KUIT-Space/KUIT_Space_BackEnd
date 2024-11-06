package space.space_spring.domain.pay.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.pay.model.entity.PayRequest;
import space.space_spring.domain.user.model.entity.User;
import space.space_spring.entity.Space;

import java.util.List;
import java.util.Optional;

@Repository
//@RequiredArgsConstructor
public interface PayRequestRepository extends JpaRepository<PayRequest, Long> {

//    private final JPAQueryFactory jpaQueryFactory;

    @Query("select pr from PayRequest pr where pr.payCreateUser = :user and pr.space = :space and pr.isComplete = :isComplete and pr.status = 'ACTIVE'")
    List<Optional<PayRequest>> findByUserAndSpace(User user, Space space, boolean isComplete);

}
