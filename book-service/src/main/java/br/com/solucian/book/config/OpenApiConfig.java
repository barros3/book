package br.com.solucian.book.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Biblioteca API")
                        .version("v1")
                        .description("CRUD de Livro, Autor e Assunto + relatório geral")
                        .contact(new Contact()
                                .name("Luciano Ferreira Barros")
                                .email("ferreira.obarros@gmail.com")));
    }
}
