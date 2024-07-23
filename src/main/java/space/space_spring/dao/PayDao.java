package space.space_spring.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import space.space_spring.entity.PayRequest;
import space.space_spring.entity.PayRequestTarget;
import space.space_spring.entity.User;

import java.util.List;

@Repository
public class PayDao {

    @PersistenceContext
    private EntityManager em;

    public List<PayRequest> findPayRequestListByUser(User user) {
        // 아직 정산이 완료되지 않은 payRequest 엔티티만 select
        String jpql = "SELECT pr FROM PayRequest pr WHERE pr.payCreateUser = :user AND pr.isComplete = false";

        return em.createQuery(jpql, PayRequest.class)
                .setParameter("user", user)
                .getResultList();
    }


    public List<PayRequestTarget> findPayRequestTargetList(PayRequest payRequest) {
        // 해당 정산 요청의 모든 타겟 엔티티를 select
        String jpql = "SELECT prt FROM PayRequestTarget prt WHERE prt.payRequest = :payRequest";

        return em.createQuery(jpql, PayRequestTarget.class)
                .setParameter("payRequest", payRequest)
                .getResultList();
    }

}
