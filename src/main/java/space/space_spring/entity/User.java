package space.space_spring.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "Users")
@Getter
public class User extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "user_name")
    private String userName;

    public void saveUser(String email, String password, String userName) {
        this.email = email;
        this.password = password;
        this.userName = userName;
        initializeBaseEntityFields();
    }

    public boolean passwordMatch(String password) {
        return this.password.equals(password);
    }

}
