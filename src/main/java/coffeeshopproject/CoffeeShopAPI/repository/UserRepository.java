package coffeeshopproject.CoffeeShopAPI.repository;

import coffeeshopproject.CoffeeShopAPI.entity.User;
import coffeeshopproject.CoffeeShopAPI.model.user.RoleUserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    Optional<User> findFirstByToken(String token);
    Optional<User> findByEmail(String email);
    List<User> findByEmailAndRevokedFalseAndExpiredFalse(String email);
    boolean existsByRole(RoleUserModel roleUserModel);
}
