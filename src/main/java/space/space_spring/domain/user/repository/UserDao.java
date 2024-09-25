package space.space_spring.domain.user.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import space.space_spring.entity.User;
import space.space_spring.entity.enumStatus.UserSignupType;

import java.util.Optional;

@Repository
public class UserDao {

    @PersistenceContext
    private EntityManager em;

    public User saveUser(String email, String password, String userName, UserSignupType signupType) {
        User user = new User();
        user.saveUser(email, password, userName, signupType);

        em.persist(user);
        return user;
    }

    public User findUserByEmailAndSignupType(String email, UserSignupType signupType) {
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.email = :email AND u.signupType = :signupType", User.class);
        query.setParameter("email", email);
        query.setParameter("signupType", signupType.getSignupType());
        return query.getSingleResult();
    }

    public boolean hasDuplicateEmail(String email, UserSignupType signupType) {
        String jpql = "SELECT COUNT(u) FROM User u WHERE u.email = :email AND u.signupType = :signupType";
        Long count = em.createQuery(jpql, Long.class)
                .setParameter("email", email)
                .setParameter("signupType", signupType.getSignupType())
                .getSingleResult();
        return count > 0;
    }

    public User getUserByEmail(String email, UserSignupType signupType) {
        try {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.email = :email AND u.signupType = :userSignupType AND u.status = 'ACTIVE'", User.class);
            query.setParameter("email", email);
            query.setParameter("userSignupType", signupType.getSignupType());
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public User findUserByUserId(Long userId) {
        return em.find(User.class, userId);
    }

    public Optional<User> findUserByRefreshToken(String refreshToken) {
        String jpql = "SELECT u FROM User u WHERE u.refreshToken = :refreshToken AND u.status = 'ACTIVE'";
        TypedQuery<User> query = em.createQuery(jpql, User.class);
        query.setParameter("refreshToken", refreshToken);

        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
