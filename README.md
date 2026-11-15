# Quem sou eu? — Parte do Gustavo (Cartas, Painel Admin e Firestore)

## 1. Estrutura de arquivos

```
app/src/main/java/com/exemplo/quemsoueu/
├── model/
│   └── Carta.kt                      # data class Carta, enums Categoria e Dificuldade
├── data/
│   └── CartasIniciais.kt             # lista inicial com 15 cartas
├── admin/
│   ├── AdminValidacao.kt             # FormularioCarta + validação do formulário
│   └── PainelAdminLogica.kt          # CRUD, filtro, ordenação e resumo (lógica pura)
├── repository/
│   ├── CartaRepository.kt            # interface (contrato de acesso aos dados)
│   └── FirestoreCartaRepository.kt   # implementação com Firestore + try-catch
└── viewmodel/
    ├── PainelAdminViewModel.kt       # ViewModel com StateFlow (PainelAdminUiState)
    └── PainelAdminViewModelFactory.kt # Factory para criar o ViewModel com o repositório
```

Troque o pacote `com.exemplo.quemsoueu` pelo pacote real do seu projeto em todos os arquivos.

## 2. Dependências (`app/build.gradle.kts`)

```kotlin
dependencies {
    // Firebase (use a BOM para não precisar informar versões individuais)
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
    implementation("com.google.firebase:firebase-firestore")

    // Coroutines (.await() nas tarefas do Firebase)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.8.1")

    // ViewModel + Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.4")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.4")
}
```

Confira sempre as versões mais recentes no Android Studio antes de usar. Também é preciso:

- No `build.gradle.kts` do projeto, adicionar o plugin `id("com.google.gms.google-services") version "4.4.2" apply false`, e aplicá-lo no módulo `app`.
- Colocar o arquivo `google-services.json` (gerado no console do Firebase) dentro de `app/`.

## 3. Estrutura no Firestore

Coleção: `cartas`. Cada documento tem como ID o campo `id` da carta (ex.: `carta_001`).

| Campo         | Tipo            | Exemplo                       |
|---------------|-----------------|-------------------------------|
| `personagem`  | string          | `"Albert Einstein"`           |
| `dicas`       | array de string | `["Nasci na Alemanha", ...]`  |
| `categoria`   | string          | `"PESSOA_REAL"`               |
| `dificuldade` | string          | `"FACIL"`                     |
| `ativa`       | boolean         | `true`                        |

Regras de teste (apenas durante o desenvolvimento, porque deixam o banco aberto):

```
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /{document=**} {
      allow read, write: if true;
    }
  }
}
```

## 4. Como usar no Compose (integração com a tela do Matheus)

```kotlin
@Composable
fun TelaPainelAdmin() {
    val viewModel: PainelAdminViewModel = viewModel(
        factory = PainelAdminViewModelFactory.comFirestore()
    )
    val estado by viewModel.uiState.collectAsStateWithLifecycle()

    // estado.cartasExibidas -> lista para a tela
    // estado.carregando / estado.erroGeral / estado.mensagemSucesso -> feedback
    // viewModel.salvarCarta(FormularioCarta(...)) -> ao clicar em "Salvar"
    // viewModel.importarCartasIniciais() -> botão de uso único
}
```

`collectAsStateWithLifecycle` vem da dependência `lifecycle-runtime-compose`.

## 5. Pontos de integração com a equipe

- **Matheus (UI/UX):** a tela do Painel Admin observa `PainelAdminUiState`. Os estados de carregando, vazio e erro (Fase 2) devem usar `carregando`, `cartasExibidas.isEmpty()` e `erroGeral`.
- **Matheus (Jogo):** o ViewModel do jogo deve usar `CartaRepository.listarCartas()` e filtrar apenas as cartas com `ativa == true` (`PainelAdminLogica.cartasAtivas`).
- **Gabriel (IA e Ranking):** a IA pode gerar novas cartas e enviá-las pelo `CartaRepository.salvarCarta`. O Room do ranking é independente deste módulo.

## 6. Decisões de projeto

- **Result e try-catch:** o repositório nunca lança exceção para a tela. Toda falha vira `Result.failure` e é mostrada como mensagem.
- **CancellationException:** é relançada nos blocos `catch`, para não quebrar o cancelamento das coroutines.
- **Dados malformados:** documentos inválidos no Firestore são ignorados (`mapNotNull` e `runCatching`) em vez de travar a lista.
- **Lógica pura:** `PainelAdminLogica` e `AdminValidacao` não dependem de Android, então podem ser testadas com testes unitários comuns.

## 7. Checklist antes de entregar

- [ ] Trocar o pacote para o do projeto
- [ ] Adicionar `google-services.json` e o plugin do Google Services
- [ ] Rodar o app e clicar em "Importar cartas iniciais"
- [ ] Conferir no console do Firebase se a coleção `cartas` tem 15 documentos
- [ ] Testar criar, editar, remover e ativar/desativar uma carta
- [ ] Testar o formulário com nome vazio, dica curta e categoria não escolhida
