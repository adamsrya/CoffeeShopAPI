package coffeeshopproject.CoffeeShopAPI.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    private String repassword;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(columnDefinition = "timestamp default now()",updatable = false)
    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private Date created;

    @Column(columnDefinition = "timestamp default now()")
    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private Date updated;
    @Column(unique = true)
    private String token;

    @Column(name = "token_expired_at")
    private Long tokenExpiredAt;


    @OneToMany(fetch = FetchType.LAZY,mappedBy = "user",cascade = CascadeType.ALL)
    private List<Address> addresses;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

}
