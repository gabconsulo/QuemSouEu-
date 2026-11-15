# Quem Sou Eu? — parte de UI/UX (Matheus)

App Android em **Kotlin + Jetpack Compose**. Este projeto tem **só a parte de UI/UX** (Matheus Henrique):
o tema cartoon, os componentes, o mascote e o visual das **7 telas**.

Não tem navegação, regras do jogo, banco, Firebase nem IA: isso é dos colegas.
Cada colega liga a sua parte nos parâmetros e callbacks de cada tela (tabela abaixo).

## Como abrir

1. Android Studio → **File › Open** → escolha a pasta `QuemSouEu` (a que tem este arquivo).
2. Espere o **Gradle Sync** terminar (na primeira vez ele baixa as bibliotecas; precisa de internet).
3. **Para ver as telas:** abra o arquivo da tela em `ui/telas` e use a aba **Split** ou **Design** (cada tela tem um `@Preview` com dados de exemplo).
4. Rodando no celular (▶ Run 'app'), o app abre só na tela inicial. Os botões passam a levar às outras telas quando a navegação do Matheus Rodrigues entrar.

Se o Android Studio oferecer "atualizar o Android Gradle Plugin", pode aceitar ou ignorar: o projeto funciona do jeito que está.

**Versões usadas:** AGP 8.7.3 · Gradle 8.11.1 · Kotlin 2.1.0 · Compose BOM 2024.12.01 · Navigation Compose 2.8.5 · compileSdk/targetSdk 35 · minSdk 24 · JDK 17 ou mais novo (o Android Studio já traz um).

## Estrutura

```
app/src/main/java/com/grupo/quemsoueu/
├── MainActivity.kt   abre a TelaInicio (Matheus Rodrigues troca pela AppNavigation)
└── ui/
    ├── theme/        Cores.kt, Tipografia.kt (Lilita One + Nunito), Tema.kt
    ├── componentes/  peças reutilizáveis do visual cartoon
    │   ├── Modificadores.kt   Modifier.cartoon() (contorno + sombra dura) e Modifier.fundoJogo()
    │   ├── TituloCartoon.kt   título branco com contorno
    │   ├── Botoes.kt          BotaoCartoon (afunda ao apertar) e BotaoIconeCartoon
    │   ├── Pecas.kt           CartaCartoon, PillCartoon, SeloCartoon, CarimboResposta, AvisoCartoon
    │   ├── CampoCartoon.kt    OutlinedTextField com o tema (rótulo + erro)
    │   ├── Estrutura.kt       TopoTela, BarraJogador (bottom bar), SeletorDificuldade
    │   ├── Estados.kt         EstadoCarregando, EstadoVazio, EstadoErro
    │   └── Mascote.kt         mascote desenhado com Canvas (100% Kotlin)
    ├── modelos/      ModelosUi.kt (o que cada tela recebe) e DadosExemplo.kt (só para os @Preview)
    └── telas/        as 7 telas + DialogoFimDePartida
app/src/main/res/font/   fontes Lilita One e Nunito (licença OFL em licencas-fontes/)
```

## As 7 telas e quem liga cada uma

As telas **não decidem regras**: elas mostram o que recebem e avisam o que a pessoa fez.
Quem liga a lógica passa os dados nos parâmetros e trata os callbacks (`on...`).

| Tela | Arquivo | Recebe | Avisa | Quem liga |
|---|---|---|---|---|
| 1 · Início | `TelaInicio.kt` | — | `onJogar`, `onEntrarAdmin` | Matheus Rodrigues (rotas) |
| 2 · Jogo rodando | `TelaJogo.kt` | `EstadoJogoUi` | `onVirarDica`, `onChutar(palpite)` | Matheus Rodrigues |
| | | parte de IA do `EstadoJogoUi` | `onPerguntar(pergunta)` | Gabriel |
| Fim de partida | `DialogoFimDePartida.kt` | `ResultadoPartidaUi` | `onSalvar(nome)`, `onJogarDeNovo` | Matheus Rodrigues |
| 3 · Ajustes | `TelaAjustes.kt` | `AjustesUi` | `onSalvar(AjustesUi)` | Matheus Rodrigues |
| 4 · Ranking | `TelaRanking.kt` | lista de `PartidaUi` já filtrada e ordenada, `carregando`, `erro` | `onFiltroChange`, `onOrdemChange`, `onEditarNome`, `onExcluir` | Gabriel |
| 5 · Login do Admin | `TelaLoginAdmin.kt` | `erro`, `carregando` | `onEntrar(usuario, senha)`, `onVoltar` | Matheus Rodrigues |
| 6 · Painel Admin | `TelaPainelAdmin.kt` | lista de `CartaUi` já filtrada, `busca`, `sincronizando`, `carregando`, `erro` | `onBuscaChange`, `onFiltroChange`, `onSincronizar`, `onNovaCarta`, `onEditar`, `onExcluir`, `onSair` | Gustavo |
| 7 · Cadastro de carta | `TelaCadastroCarta.kt` | `cartaInicial` (null = nova), `dicasGeradas`, `gerandoDicas`, `erroIa` | `onSalvar(CartaUi)` (Gustavo), `onGerarDicas(...)` (Gabriel) | Gustavo e Gabriel |

**Modelos de UI × modelos de dados:** `CartaUi` e `PartidaUi` são só das telas. As `data class` oficiais
(`Personagem` e `Partida`) são do Gustavo; quem liga a tela converte uma na outra.

**Bottom bar:** `BarraJogador` (em `Estrutura.kt`) é o visual. Matheus Rodrigues coloca no `bottomBar` do `Scaffold`
e muda de rota em `onSelecionar`.

**Rotas combinadas no plano:** `inicio`, `jogo`, `ajustes`, `ranking`, `login`, `admin`, `carta/{id}`.

**O que a tela faz sozinha:** só confere campo vazio (ex.: "Digite a senha") para mostrar a mensagem embaixo do campo.
Qualquer regra de verdade (senha correta, palpite certo, pontos, filtros, salvar) vem de fora pelos callbacks.

## Decisões de UI/UX

- **Peças de papelão:** tudo usa `Modifier.cartoon()` — contorno de 3 dp e sombra dura embaixo. Botões "afundam" ao apertar.
- **Cor nunca sozinha:** Sim/Não/Talvez, Ativa/Pausada e as categorias sempre aparecem escritas.
- **Botões sempre ativos:** em vez de desabilitar "Entrar" ou "Salvar", a tela explica o que falta embaixo do campo.
- **Textos de ação:** o botão diz o que acontece ("Salvar carta", "Virar dica (−15 pts)", "Chutar!").
- **Estados de lista:** Ranking e Painel Admin já mostram carregando, vazio (com o próximo passo) e erro com "Tentar de novo".
- **Acessibilidade:** alvos de toque de 48 dp, `contentDescription` nos ícones, títulos marcados como cabeçalho,
  avisos anunciados pelo leitor de tela (`liveRegion`), contraste de texto acima de 4,5:1.
- **Edge-to-edge:** o app desenha atrás das barras do sistema (obrigatório no Android 15) e respeita os espaços delas.
