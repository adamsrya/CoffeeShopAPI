package coffeeshopproject.CoffeeShopAPI;

import coffeeshopproject.CoffeeShopAPI.services.impl.AddressServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableAutoConfiguration(exclude = {ErrorMvcAutoConfiguration.class})
@SpringBootApplication
public class CoffeeShopApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoffeeShopApiApplication.class, args);
	}

}
