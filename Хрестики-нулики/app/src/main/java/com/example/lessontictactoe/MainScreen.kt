package com.example.lessontictactoe

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lessontictactoe.ui.theme.LessonTicTacToeTheme
import kotlinx.coroutines.delay

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    var selectedSize by remember { mutableStateOf<Int?>(null) }
    var scoreX by remember { mutableStateOf(0) }
    var scoreO by remember { mutableStateOf(0) }
    var isDarkTheme by remember { mutableStateOf(false) }

    LessonTicTacToeTheme(darkTheme = isDarkTheme) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column {
                Spacer(modifier = Modifier.height(32.dp)) // відступ від статус-бару
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Темна тема", modifier = Modifier.padding(end = 8.dp))
                    Switch(
                        checked = isDarkTheme,
                        onCheckedChange = { isDarkTheme = it }
                    )
                }

                if (selectedSize == null) {
                    SetupScreen { size ->
                        selectedSize = size
                    }
                } else {
                    GameScreen(
                        dim = selectedSize!!,
                        onNewGame = { selectedSize = null },
                        scoreX = scoreX,
                        scoreO = scoreO,
                        onUpdateScore = { winner ->
                            if (winner == "X") scoreX++
                            if (winner == "0") scoreO++
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SetupScreen(onStartGame: (Int) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Оберіть розмір поля", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        listOf(3, 4, 5).forEach { size ->
            Button(
                onClick = { onStartGame(size) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("${size} x $size")
            }
        }
    }
}

@Composable
fun GameScreen(
    dim: Int,
    onNewGame: () -> Unit,
    scoreX: Int,
    scoreO: Int,
    onUpdateScore: (String) -> Unit
) {
    var field by remember { mutableStateOf(List(dim * dim) { "_" }) }
    var currentPlayer by remember { mutableStateOf("X") }
    var winner by remember { mutableStateOf<String?>(null) }
    var isDraw by remember { mutableStateOf(false) }
    var timer by remember { mutableStateOf(10) }
    var showScore by remember { mutableStateOf(false) }

    LaunchedEffect(currentPlayer, field, winner, isDraw) {
        timer = 10
        while (timer > 0 && winner == null && !isDraw) {
            delay(1000)
            timer--
        }
        if (timer == 0 && winner == null && !isDraw) {
            currentPlayer = if (currentPlayer == "X") "0" else "X"
        }
    }

    fun checkWin(): String? {
        for (i in 0 until dim) {
            val row = field.subList(i * dim, (i + 1) * dim)
            if (row.all { it == "X" }) return "X"
            if (row.all { it == "0" }) return "0"
        }

        for (i in 0 until dim) {
            val col = (0 until dim).map { field[it * dim + i] }
            if (col.all { it == "X" }) return "X"
            if (col.all { it == "0" }) return "0"
        }

        val diag1 = (0 until dim).map { field[it * dim + it] }
        val diag2 = (0 until dim).map { field[(it + 1) * (dim - 1)] }
        if (diag1.all { it == "X" }) return "X"
        if (diag1.all { it == "0" }) return "0"
        if (diag2.all { it == "X" }) return "X"
        if (diag2.all { it == "0" }) return "0"

        return null
    }

    fun checkDraw(): Boolean {
        return field.none { it == "_" } && winner == null
    }

    fun resetRound() {
        field = List(dim * dim) { "_" }
        currentPlayer = "X"
        winner = null
        isDraw = false
        timer = 10
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Tic Tac Toe", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        for (row in 0 until dim) {
            Row {
                for (col in 0 until dim) {
                    val index = row * dim + col
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .padding(4.dp)
                            .border(2.dp, MaterialTheme.colorScheme.primary)
                            .clickable(
                                enabled = field[index] == "_" && winner == null && !isDraw
                            ) {
                                field = field.toMutableList().also { it[index] = currentPlayer }
                                winner = checkWin()
                                isDraw = checkDraw()
                                if (winner != null) {
                                    onUpdateScore(winner!!)
                                } else if (!isDraw) {
                                    currentPlayer = if (currentPlayer == "X") "0" else "X"
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(field[index], style = MaterialTheme.typography.headlineMedium)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            winner != null -> Text("Гравець $winner переміг!", style = MaterialTheme.typography.titleLarge)
            isDraw -> Text("Нічия!", style = MaterialTheme.typography.titleLarge)
            else -> {
                Text("Хід: $currentPlayer", style = MaterialTheme.typography.titleMedium)
                Text("Час: $timer секунд", style = MaterialTheme.typography.bodyLarge)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { resetRound() }) {
                    Text("Скинути")
                }
                Button(onClick = { showScore = !showScore }) {
                    Text("Рахунок")
                }
            }
            Button(onClick = { onNewGame() }) {
                Text("Нова гра")
            }
        }

        if (showScore) {
            Spacer(modifier = Modifier.height(12.dp))
            Text("X = $scoreX, 0 = $scoreO", style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}
