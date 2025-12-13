package dinamico;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LoaderDinamicoApplication {
    public static void main(String[] args) {
        SpringApplication.run(LoaderDinamicoApplication.class, args);
    }
}
