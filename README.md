# TP4 Integrado

Aplicacao unica em Spring Boot e Gradle que integra os sistemas de produtos do TP1 e recursos do TP3.

## Como executar

```bash
./gradlew bootRun
```

No Windows:

```powershell
.\gradlew.bat bootRun
```

Acesse `http://localhost:8080`.

## Testes e cobertura

```bash
./gradlew clean test jacocoTestReport jacocoTestCoverageVerification
```

Relatorios:

- `build/reports/tests/test/index.html`
- `build/reports/jacoco/test/html/index.html`

## Principais integracoes

- produtos podem ser vinculados a recursos existentes
- listagem de produtos mostra os recursos associados
- listagem de recursos mostra os produtos vinculados
- ao remover um recurso, os vinculos sao removidos automaticamente dos produtos

## CI/CD

Os workflows do GitHub Actions estao em `.github/workflows/`.
