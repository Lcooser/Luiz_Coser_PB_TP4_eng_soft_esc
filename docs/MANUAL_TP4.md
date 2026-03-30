# Manual de Execucao do TP4

## Objetivo

O TP4 integra em uma unica aplicacao os cadastros de produtos do TP1 e de recursos do TP3.

## Requisitos

- Java 17 ou superior
- Gradle Wrapper incluido no projeto

## Como rodar a aplicacao

No Linux ou macOS:

```bash
./gradlew bootRun
```

No Windows:

```powershell
.\gradlew.bat bootRun
```

Depois, acessar `http://localhost:8080`.

## Fluxo integrado

1. Cadastre recursos em `/recursos`.
2. Cadastre produtos em `/produtos`.
3. No formulario de produto, selecione os recursos vinculados.
4. Consulte as duas listagens para verificar a integracao.
5. Ao remover um recurso, os vinculos nos produtos sao atualizados automaticamente.

## Como executar testes

```bash
./gradlew clean test jacocoTestReport jacocoTestCoverageVerification
```

No Windows:

```powershell
.\gradlew.bat clean test jacocoTestReport jacocoTestCoverageVerification
```

Relatorios gerados:

- `build/reports/tests/test/index.html`
- `build/reports/jacoco/test/html/index.html`

## Como interpretar os workflows

### `CI`

- `Build`: compila e monta o projeto com `./gradlew assemble`
- `Test And Coverage`: executa testes, gera relatorio JaCoCo e valida a cobertura minima

### `Quality`

- executa `./gradlew clean build --warning-mode all`
- utiliza `-Xlint:all` na compilacao para destacar avisos de qualidade no log

## Runners

Foi escolhida a estrategia de `GitHub-hosted runner` com `ubuntu-latest`, porque o projeto precisa apenas de Java e Gradle para compilar, testar e gerar relatórios.

Em um cenario com dependencia obrigatoria de navegador especifico, driver local ou rede interna, a alternativa seria um `self-hosted runner`.

## Principais mudancas de refatoracao

- unificacao dos dois sistemas em uma unica aplicacao Spring Boot
- migracao do build de Maven para Gradle
- criacao de objetos de valor para ID, preco, quantidade e colecao de recursos vinculados
- criacao de servico integrado para manter consistencia entre produtos e recursos
- centralizacao da persistencia em repositórios reutilizaveis em memoria
- cobertura automatizada validada com JaCoCo
