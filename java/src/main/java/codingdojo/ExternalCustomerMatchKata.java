package codingdojo;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "codingdojo.repository")
public class ExternalCustomerMatchKata {

    public static void main(String[] args) {
        SpringApplication.run(ExternalCustomerMatchKata.class, args);
    }
}
