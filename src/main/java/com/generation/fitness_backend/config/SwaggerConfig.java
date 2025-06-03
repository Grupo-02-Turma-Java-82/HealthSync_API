package com.generation.fitness_backend.config;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI fitnessBackendOpenAPI() {
    String descricao = "A **API HealthSync** é uma solução backend robusta, desenvolvida em Java com Spring Boot, " +
        "projetada para otimizar e gerenciar o **cadastro de exercícios e a criação de treinos personalizados**.\n\n" +
        "Esta API RESTful oferece um conjunto de endpoints para facilitar a integração com sistemas frontend " +
        "e outras aplicações, **centralizando as informações e processos de planejamento e execução de atividades físicas**.\n\n"
        +
        "**Principais Funcionalidades da API:**\n" +
        "* **Gerenciamento de Exercícios (CRUD):** Cadastro, consulta, atualização, exclusão e busca de exercícios, com detalhes como nome, descrição, tipo e equipamentos necessários.\n"
        +
        "* **Gestão de Treinos (CRUD):** Criação, acompanhamento, atualização de status (Concluído, Em Andamento, Pendente) e exclusão de treinos personalizados para usuários.\n"
        +
        "* **Organização por Categorias de Exercícios:** Associação de exercícios a diferentes categorias (e.g., força, cardio, flexibilidade) para facilitar a busca e a organização.\n"
        +
        "* **Gerenciamento de Usuários do Sistema:** Cadastro, consulta e atualização de usuários, incluindo suas informações pessoais, objetivos e histórico de treinos.\n"
        +
        "* **Rastreamento de Progresso:** Funcionalidades para acompanhar o progresso dos usuários ao longo do tempo (implícito via treinos e exercícios).\n";

    return new OpenAPI()
        .info(new Info()
            .title("Health Sync - API")
            .description(descricao)
            .version("v0.0.1")
            .license(new License()
                .name("Generation Brasil / Grupo 02 - Turma Java 82")
                .url("https://brazil.generation.org/"))
            .contact(new Contact()
                .name("Grupo 02 - Turma Java 82 (Projeto Health Sync)")
                .url("https://github.com/Grupo-02-Turma-Java-82/HealthSync")
                .email("grupo02turmajava82@hotmail.com")))
        .externalDocs(new ExternalDocumentation()
            .description("Repositório do Projeto no Github")
            .url("https://github.com/Grupo-02-Turma-Java-82/HealthSync"));
  }

  @Bean
  OpenApiCustomizer customerGlobalHeaderOpenApiCustomiser() {
    return openApi -> {
      openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(operation -> {
        ApiResponses apiResponses = operation.getResponses();
        apiResponses.addApiResponse("200", createApiResponse("Sucesso!"));
        apiResponses.addApiResponse("201", createApiResponse("Objeto Criado/Persistido!"));
        apiResponses.addApiResponse("204",
            createApiResponse("Operação bem-sucedida, sem conteúdo de retorno (Ex: Objeto Excluído)."));
        apiResponses.addApiResponse("400", createApiResponse("Erro na Requisição (Ex: Dados inválidos)."));
        apiResponses.addApiResponse("401", createApiResponse("Acesso Não Autorizado (Requer autenticação)."));
        apiResponses.addApiResponse("403", createApiResponse("Acesso Proibido (Autenticado, mas sem permissão)."));
        apiResponses.addApiResponse("404", createApiResponse("Recurso Não Encontrado."));
        apiResponses.addApiResponse("500", createApiResponse("Erro Interno na Aplicação."));
      }));
    };
  }

  private ApiResponse createApiResponse(String message) {
    return new ApiResponse().description(message);
  }

}