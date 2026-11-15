package com.grupo.quemsoueu

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.ui.Modifier
import com.grupo.quemsoueu.ui.theme.QuemSouEuTema
import com.grupo.quemsoueu.ui.telas.TelaInicio

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Ícones claros na barra de status (o fundo do app é roxo).
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
        )
        setContent {
            QuemSouEuTema {
                // A navegação entre as 7 telas é do Matheus Rodrigues (AppNavigation com NavHost).
                // Até ela chegar, o app abre só na tela inicial; as outras telas
                // podem ser vistas pelos @Preview de cada arquivo em ui/telas.
                TelaInicio(
                    onJogar = { },
                    onEntrarAdmin = { },
                    modifier = Modifier
                        .fillMaxSize()
                        .safeDrawingPadding(),
                )
            }
        }
    }
}
