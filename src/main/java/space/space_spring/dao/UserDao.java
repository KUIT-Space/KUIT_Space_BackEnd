package space.space_spring.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.User;
import space.space_spring.dto.PostUserRequest;

@Repository
public class UserDao {

    @PersistenceContext
    private EntityManager em;

    public Long saveUser(PostUserRequest postUserRequest) {
        User user = new User();
        user.saveUser(postUserRequest.getEmail(), postUserRequest.getPassword(), postUserRequest.getUserName());

        em.persist(user);
        return user.getUserId();
    }

    public User findUserByEmail(String email) {
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
        query.setParameter("email", email);
        return query.getSingleResult();
    }
}
