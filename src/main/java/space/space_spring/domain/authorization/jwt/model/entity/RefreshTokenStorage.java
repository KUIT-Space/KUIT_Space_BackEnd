package space.space_spring.domain.authorization.jwt.model.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.space_spring.domain.user.model.entity.User;

@Entity
@Table(name = "Token_Storage")
@Getter
@NoArgsConstructor
public class RefreshTokenStorage {

    @Id @GeneratedValue
    @Column(name = "token_storage_id")
    private Long tokenStorageId;

    @OneToOne(orphanRemoval = true)
//    @Column(name = "user_id")
    private User user;

    @Column(name = "token_value")
    private String tokenValue;

    public void updateTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    public boolean checkTokenValue(String tokenValue) {
        return this.tokenValue.equals(tokenValue);
    }

    @Builder
    private RefreshTokenStorage(User user, String tokenValue) {
        this.user = user;
        this.tokenValue = tokenValue;
    }

    public static RefreshTokenStorage create(User user, String tokenValue) {
        return RefreshTokenStorage.builder()
                .user(user)
                .tokenValue(tokenValue)
                .build();
    }

}
