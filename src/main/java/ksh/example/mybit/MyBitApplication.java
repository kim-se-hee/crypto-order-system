package ksh.example.mybit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class MyBitApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyBitApplication.class, args);
    }

}
