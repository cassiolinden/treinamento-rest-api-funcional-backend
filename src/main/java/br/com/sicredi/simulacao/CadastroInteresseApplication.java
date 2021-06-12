package br.com.sicredi.simulacao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class CadastroInteresseApplication {

    public static void main(String[] args) {
        SpringApplication.run(CadastroInteresseApplication.class, args);
    }
}
