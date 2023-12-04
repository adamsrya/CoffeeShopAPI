package coffeeshopproject.CoffeeShopAPI.repository;

import coffeeshopproject.CoffeeShopAPI.entity.Address;
import coffeeshopproject.CoffeeShopAPI.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

    Optional<Address> findFirstByUserAndId(User user, Integer id);

}
