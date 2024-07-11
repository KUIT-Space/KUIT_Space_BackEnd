package space.space_spring.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "Users")
public class User extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "jwt_token")
    private String jwt;

}
