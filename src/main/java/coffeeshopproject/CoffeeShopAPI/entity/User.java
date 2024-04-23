package coffeeshopproject.CoffeeShopAPI.entity;

import coffeeshopproject.CoffeeShopAPI.model.user.RoleUserModel;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @Column(name = "email",unique = true)
    private String email;
    @Column(name = "password")
    private String password;
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
    private boolean revoked;
    private boolean expired;
    @Enumerated(EnumType.STRING)
    @Column(name = "role",unique = true)
    private RoleUserModel role;
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user",cascade = CascadeType.ALL)
    private List<Address> addresses;
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user",cascade = CascadeType.ALL)
    private List<Cart> carts;

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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
