# Quem sou eu? — app Android

Aplicativo em Kotlin e Jetpack Compose. Todo o código Kotlin do app fica em
`app/src/main/java/com/grupo/quemsoueu`, organizado pelo pacote real da aplicação.

## Módulos

- `data/local`: entidade, DAO e banco Room do ranking.
- `data`: conjunto inicial de cartas para importação no Firestore.
- `model`: modelo de dados das cartas.
- `admin`: validação de formulário e lógica pura do painel.
- `repository`: contratos/implementações de Room e Firestore, além das chamadas à API OpenAI.
- `viewmodel`: estados e ViewModels do jogo, ranking e painel Admin.
- `ui/telas`: telas Compose; `AdminRotas.kt` liga o painel e o cadastro aos ViewModels.

### Ranking (Room)

`PartidaEntity` salva jogador, personagem, categoria, pontuação, dicas, perguntas à IA,
tempo e data. `PartidaDao` oferece inserir, atualizar, excluir e observar por `Flow`.
`RoomPartidaRepository` converte para `Result`, preserva o cancelamento de coroutines e
o `RankingViewModel` expõe erros em `RankingUiState`. O ranking filtra por categoria e
ordena por pontos, tempo ou data; as partidas ficam no dispositivo.

### OpenAI

O jogo envia perguntas de até 200 caracteres e limita cada partida a 10 perguntas.
OpenAI responde `SIM`, `NAO` ou `TALVEZ`; uma resposta válida custa 5 pontos. O cadastro
de cartas também usa a mesma API para sugerir cinco dicas, que podem ser revisadas antes
de salvar. O modelo configurado é `gpt-4o-mini`.

Configure a chave **fora do repositório**, usando uma propriedade Gradle:

```properties
OPENAI_API_KEY=sua_chave
```

Salve essa linha em `%USERPROFILE%\.gradle\gradle.properties`, ou defina a variável de
ambiente `OPENAI_API_KEY`. Sem chave, as telas mostram o erro de configuração ao usar a
IA. A chave compilada em um app Android pode ser extraída do APK: restrinja-a à API
OpenAI e use um backend para qualquer implantação pública.

### Firestore (cartas)

Adicione o `google-services.json` do projeto Firebase em `app/`. O plugin Google Services
é aplicado automaticamente quando esse arquivo existe. Sem a configuração Firebase, o
app ainda compila, mas as telas de jogo/Admin exibem o erro de conexão em vez de iniciar
com uma falha não tratada.

## Build

Abra o projeto no Android Studio com JDK 17 e Android SDK 35, configure Firebase/OpenAI
se for testar esses serviços e execute:

```powershell
.\gradlew.bat :app:assembleDebug
```
