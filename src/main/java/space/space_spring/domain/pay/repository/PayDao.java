package space.space_spring.domain.pay.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.pay.model.dto.RecentPayRequestBankInfoDto;
import space.space_spring.domain.pay.model.entity.PayRequest;
import space.space_spring.domain.pay.model.entity.PayRequestTarget;
import space.space_spring.entity.Space;
import space.space_spring.domain.user.model.entity.User;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<RecentPayRequestBankInfoDto> findRecentPayRequestBankInfoByUser(User user) {
        // 유저가 최근에 정산받은 계좌 정보를 select
        // 어느 스페이스에서 정산받았는지는 중요하지 X & 최대 5개만 select
        String jpql = "SELECT pr.bankName, pr.bankAccountNum " +
                "FROM PayRequest pr " +
                "WHERE pr.payCreateUser = :user ORDER BY pr.lastModifiedAt DESC";
        TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class)
                .setParameter("user", user)
                .setMaxResults(5);          // 최대 5개만 return

        List<Object[]> resultList = query.getResultList();

        return resultList.stream()
                .map(result -> new RecentPayRequestBankInfoDto(
                        (String) result[0],
                        (String) result[1]))
                .collect(Collectors.toList());
    }

    public PayRequest createPayRequest(User payCreateUser, Space space, int totalAmount, String bankName, String bankAccountNum, int unRequestedAmount, boolean isComplete) {
//        PayRequest payRequest = new PayRequest();
//        payRequest.savePayRequest(payCreateUser, space, totalAmount, bankName, bankAccountNum, unRequestedAmount, isComplete);
        PayRequest payRequest = PayRequest.create(payCreateUser, space, totalAmount, bankName, bankAccountNum, unRequestedAmount, isComplete);

        em.persist(payRequest);
        return payRequest;
    }

    public PayRequestTarget createPayRequestTarget(PayRequest payRequest, Long targetUserId, int requestAmount, boolean isComplete) {
        PayRequestTarget payRequestTarget = new PayRequestTarget();
        payRequestTarget.savePayRequestTarget(payRequest, targetUserId, requestAmount, isComplete);

        em.persist(payRequestTarget);
        return payRequestTarget;
    }

    public PayRequest findPayRequestById(Long payRequestId) {

        return em.find(PayRequest.class, payRequestId);
    }

    public PayRequestTarget findPayRequestTargetById(Long payRequestTargetId) {
        return em.find(PayRequestTarget.class, payRequestTargetId);
    }

}
