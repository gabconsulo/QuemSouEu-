package com.grupo.quemsoueu.navegacao

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.grupo.quemsoueu.login.Perfil
import com.grupo.quemsoueu.repository.FirestoreCartaRepository
import com.grupo.quemsoueu.ui.componentes.BarraJogador
import com.grupo.quemsoueu.ui.componentes.EstadoCarregando
import com.grupo.quemsoueu.ui.componentes.EstadoErro
import com.grupo.quemsoueu.ui.componentes.fundoJogo
import com.grupo.quemsoueu.ui.modelos.PartidaUi
import com.grupo.quemsoueu.ui.telas.CadastroCartaRota
import com.grupo.quemsoueu.ui.telas.DialogoFimDePartida
import com.grupo.quemsoueu.ui.telas.PainelAdminRota
import com.grupo.quemsoueu.ui.telas.RankingRota
import com.grupo.quemsoueu.ui.telas.TelaAjustes
import com.grupo.quemsoueu.ui.telas.TelaInicio
import com.grupo.quemsoueu.ui.telas.TelaJogo
import com.grupo.quemsoueu.ui.telas.TelaLoginAdmin
import com.grupo.quemsoueu.viewmodel.JogoUiState
import com.grupo.quemsoueu.viewmodel.JogoViewModel
import com.grupo.quemsoueu.viewmodel.JogoViewModelFactory
import com.grupo.quemsoueu.viewmodel.LoginViewModel

fun irParaAba(navController: NavController, rota: String) {
    navController.navigate(rota) {
        popUpTo(ROTA_INICIO) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

@Composable
fun RotaJogo(
    jogo: JogoUiState,
    jogoViewModel: JogoViewModel,
    aoSalvarPartida: (PartidaUi) -> Unit,
    aoIrParaRanking: () -> Unit
) {
    LaunchedEffect(jogo.precisaIniciar) {
        if (jogo.precisaIniciar) {
            jogoViewModel.onIniciarPartida()
        }
    }

    val estadoTela = jogo.estadoTela
    val resultado = jogo.resultado
    val erro = jogo.erro

    if (estadoTela == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .fundoJogo()
                .padding(18.dp)
        ) {
            if (erro != null) {
                EstadoErro(mensagem = erro, onTentarDeNovo = { jogoViewModel.onCarregarCartas() })
            } else {
                EstadoCarregando()
            }
        }
        return
    }

    TelaJogo(
        estado = estadoTela,
        onVirarDica = { jogoViewModel.onVirarDica() },
        onPerguntar = { },
        onChutar = { palpite -> jogoViewModel.onChutar(palpite) }
    )

    if (resultado != null) {
        DialogoFimDePartida(
            resultado = resultado,
            onSalvar = { nome ->
                val partida = jogoViewModel.montarPartida(nome)
                if (partida != null) {
                    aoSalvarPartida(partida)
                }
                jogoViewModel.onLimparPartida()
                aoIrParaRanking()
            },
            onJogarDeNovo = { jogoViewModel.onIniciarPartida() }
        )
    }
}

@Composable
fun NavegacaoApp(
    loginViewModel: LoginViewModel = viewModel(),
    jogoViewModel: JogoViewModel = viewModel(
        factory = JogoViewModelFactory(FirestoreCartaRepository())
    ),
    aoSalvarPartida: (PartidaUi) -> Unit = { }
) {
    val navController = rememberNavController()

    val login by loginViewModel.uiState.collectAsStateWithLifecycle()
    val jogo by jogoViewModel.uiState.collectAsStateWithLifecycle()
    val entradaAtual by navController.currentBackStackEntryAsState()
    val rotaAtual = entradaAtual?.destination?.route
    val abaAtual = abaDaRota(rotaAtual)

    LaunchedEffect(login.perfil) {
        if (login.perfil == Perfil.ADMIN && rotaAtual == ROTA_LOGIN) {
            navController.navigate(ROTA_ADMIN) {
                popUpTo(ROTA_LOGIN) { inclusive = true }
            }
        }
    }

    Scaffold(
        bottomBar = {
            if (abaAtual != null) {
                BarraJogador(
                    selecionada = abaAtual,
                    onSelecionar = { aba -> irParaAba(navController, rotaDaAba(aba)) }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = ROTA_INICIO,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(ROTA_INICIO) {
                TelaInicio(
                    onJogar = { irParaAba(navController, ROTA_JOGO) },
                    onEntrarAdmin = {
                        if (login.ehAdmin) {
                            navController.navigate(ROTA_ADMIN)
                        } else {
                            navController.navigate(ROTA_LOGIN)
                        }
                    }
                )
            }
            composable(ROTA_JOGO) {
                RotaJogo(
                    jogo = jogo,
                    jogoViewModel = jogoViewModel,
                    aoSalvarPartida = aoSalvarPartida,
                    aoIrParaRanking = { irParaAba(navController, ROTA_RANKING) }
                )
            }
            composable(ROTA_AJUSTES) {
                TelaAjustes(
                    ajustesIniciais = jogo.ajustes,
                    onSalvar = { novosAjustes -> jogoViewModel.onAjustesChange(novosAjustes) }
                )
            }
            composable(ROTA_RANKING) {
                RankingRota(onJogar = { irParaAba(navController, ROTA_JOGO) })
            }
            composable(ROTA_LOGIN) {
                TelaLoginAdmin(
                    onVoltar = {
                        loginViewModel.onLimparErro()
                        navController.popBackStack()
                    },
                    onEntrar = { usuario, senha -> loginViewModel.onEntrar(usuario, senha) },
                    erro = login.erro
                )
            }
            composable(ROTA_ADMIN) {
                if (login.ehAdmin) {
                    PainelAdminRota(
                        onNovaCarta = { navController.navigate(rotaCarta(ID_CARTA_NOVA)) },
                        onEditarCarta = { id -> navController.navigate(rotaCarta(id)) },
                        onSair = {
                            loginViewModel.onSair()
                            navController.navigate(ROTA_INICIO) {
                                popUpTo(ROTA_INICIO) { inclusive = true }
                            }
                        }
                    )
                }
            }
            composable(
                route = ROTA_CARTA,
                arguments = listOf(navArgument("id") { type = NavType.StringType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")
                if (login.ehAdmin) {
                    CadastroCartaRota(
                        cartaId = if (id == ID_CARTA_NOVA) null else id,
                        onVoltar = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
