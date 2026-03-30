# Relatorio de Cobertura e Integracao do TP4

## Resumo

O TP4 consolida os sistemas de produtos e recursos em uma unica aplicacao web, com integracao entre os fluxos de cadastro, listagem e remocao.

## Principais melhorias de refatoracao

- substituicao de valores primitivos por objetos de valor (`EntityId`, `MoneyValue`, `StockQuantity`)
- encapsulamento da colecao de relacionamentos em `LinkedResourceIds`
- reducao de duplicacao com repositório generico (`CatalogRepository`) e armazenamento reutilizavel (`InMemoryStore`)
- isolamento da regra de integracao em `CatalogoIntegradoService`
- separacao clara entre dominio, infraestrutura, aplicacao e web

## Integracao entre TP1 e TP3

- produtos podem referenciar multiplos recursos
- a listagem de produtos exibe os titulos dos recursos vinculados
- a listagem de recursos exibe os produtos vinculados
- a exclusao de um recurso remove o vinculo correspondente nos produtos, preservando a integridade dos dados

## Evidencias de testes

- testes unitarios de dominio e objetos de valor
- testes de repositório
- testes de controller com `MockMvc`
- teste integrado da comunicacao entre produtos e recursos

## Cobertura

- cobertura de instrucoes: `91,48%`
- cobertura de linhas: `91,13%`
- regra minima exigida no build: `85%`

Comando executado:

```bash
./gradlew clean test jacocoTestReport jacocoTestCoverageVerification
```

## CI/CD

Workflows criados:

- `.github/workflows/ci.yml`
- `.github/workflows/quality.yml`

Triggers configurados:

- `push`
- `pull_request`
- `workflow_dispatch`

## Observacao sobre analise estatica

A esteira de qualidade utiliza compilacao com `-Xlint:all` e build com `--warning-mode all` para destacar problemas de compilacao e qualidade diretamente na aba `Actions`.
