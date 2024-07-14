package space.space_spring.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.User;
import space.space_spring.dto.PostUserSignupRequest;

@Repository
public class UserDao {

    @PersistenceContext
    private EntityManager em;

    public Long saveUser(PostUserSignupRequest postUserSignupRequest) {
        User user = new User();
        user.saveUser(postUserSignupRequest.getEmail(), postUserSignupRequest.getPassword(), postUserSignupRequest.getUserName());

        em.persist(user);
        return user.getUserId();
    }

    public User findUserByEmail(String email) {
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
        query.setParameter("email", email);
        return query.getSingleResult();
    }

    public boolean hasDuplicateEmail(String email) {
        String jpql = "SELECT COUNT(u) FROM User u WHERE u.email = :email";
        Long count = em.createQuery(jpql, Long.class).setParameter("email", email).getSingleResult();
        return count > 0;
    }

    public User getUserByEmail(String email) {
        try {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
