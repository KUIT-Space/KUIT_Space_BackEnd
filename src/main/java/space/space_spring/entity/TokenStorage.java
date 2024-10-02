package space.space_spring.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Token_Storage")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenStorage {

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

}
