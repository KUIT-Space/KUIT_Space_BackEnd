package space.space_spring.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.validator.internal.constraintvalidators.bv.AssertFalseValidator;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.entity.PayRequest;
import space.space_spring.entity.PayRequestTarget;
import space.space_spring.entity.Space;
import space.space_spring.entity.User;

import java.util.List;

@Repository
public class PayDao {

    @PersistenceContext
    private EntityManager em;

    public List<PayRequest> findPayRequestListByUser(User user, Space space, boolean isComplete) {
        // 유저가 해당 스페이스에서 요청한 정산 목록만을 select
        String jpql = "SELECT pr FROM PayRequest pr WHERE pr.payCreateUser = :user AND pr.space = :space AND pr.isComplete = :isComplete";

        return em.createQuery(jpql, PayRequest.class)
                .setParameter("user", user)
                .setParameter("space", space)
                .setParameter("isComplete", isComplete)
                .getResultList();
    }


    public List<PayRequestTarget> findPayRequestTargetListByPayRequest(PayRequest payRequest) {
        // 해당 정산 요청의 모든 타겟 엔티티를 select
        String jpql = "SELECT prt FROM PayRequestTarget prt WHERE prt.payRequest = :payRequest";

        return em.createQuery(jpql, PayRequestTarget.class)
                .setParameter("payRequest", payRequest)
                .getResultList();
    }

    public List<PayRequestTarget> findPayRequestTargetListByUser(User user, Space space, boolean isComplete) {
        // 유저가 해당 스페이스에서 요청받은 정산 목록만을 select
        String jpql = "SELECT prt FROM PayRequestTarget prt " +
                "JOIN prt.payRequest pr " +
                "WHERE prt.targetUserId = :userId AND pr.space = :space AND pr.isComplete = :isComplete";

        return em.createQuery(jpql, PayRequestTarget.class)
                .setParameter("userId", user.getUserId())
                .setParameter("space", space)
                .setParameter("isComplete", isComplete)
                .getResultList();
    }

}
