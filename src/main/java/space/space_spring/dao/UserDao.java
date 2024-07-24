package space.space_spring.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import space.space_spring.entity.User;
import space.space_spring.dto.user.PostUserSignupRequest;

@Repository
public class UserDao {

    @PersistenceContext
    private EntityManager em;

    public User saveUser(PostUserSignupRequest postUserSignupRequest) {
        User user = new User();
        user.saveUser(postUserSignupRequest.getEmail(), postUserSignupRequest.getPassword(), postUserSignupRequest.getUserName());

        em.persist(user);
        return user;
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

    public User findUserByUserId(Long userId) {
        return em.find(User.class, userId);
    }
}
