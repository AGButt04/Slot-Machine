package com.zybooks.stopwatchapp

import android.os.Bundle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zybooks.stopwatchapp.ui.theme.StopWatchAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StopWatchAppTheme {
                SlotMachine(modifier = Modifier)
            }
        }
    }
}

sealed class Routes {
    @Serializable
    data object Points

    @Serializable
    data object Main
}

@Composable
fun SlotMachine(
    modifier: Modifier
) {
    val navController = rememberNavController()
    var points by remember { mutableIntStateOf(0) }
    NavHost(
        navController = navController,
        startDestination = Routes.Main
    ) {
        composable<Routes.Points> {
            PointsScreen(
                modifier = modifier,
                onUpClick = {
                    navController.navigateUp()
                },
                points = points
            )
        }
        composable<Routes.Main> {
            MainScreen(
                modifier = modifier,
                points = points,
                onPointsChange = { points = it },
                onUpClick = { navController.navigate(Routes.Points) }
            )
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier,
               onUpClick: () -> Unit,
               points: Int,
               onPointsChange: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            PointsAppBar(
                "Slot Machine", modifier = modifier
            )
        }
    ) { innerPadding ->
        SlotMachineScreen(modifier = modifier.padding(innerPadding),
            onUpClick = onUpClick,
            onPointsChange = onPointsChange,
            points = points
        )
    }
}

@Composable
fun PointsScreen(modifier: Modifier, onUpClick: () -> Unit, points: Int) {
    var chips by remember { mutableIntStateOf(points) }
    var dollars by remember { mutableIntStateOf(0) }
    Scaffold(
        topBar = {
            PointsAppBar(
                "Points Won!",
                canNavigateBack = true,
                onUpClick = onUpClick,
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxWidth().background(Color.Gray),
            verticalArrangement = Arrangement.spacedBy(space = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.dollars),
                contentDescription = "Dollars",
                modifier = Modifier.size(350.dp)
            )
            Text(
                text = "You have $chips chips \uD83C\uDF89\"",
                fontSize = 25.sp
            )
            Button(
                onClick = {
                    dollars = points/10
                }
            ) {
                Text("Convert your points to $$$ Dollars")
            }
            Text(
                text = "You have $$dollars to your name! \uD83D\uDC48 ",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PointsAppBar(
    title: String,
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = false,
    onUpClick: () -> Unit = { }
) {
    TopAppBar(
        title = { Text(title) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = onUpClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                }
            }
        }
    )
}

@Composable
fun SlotMachineScreen(
    modifier: Modifier = Modifier,
    onUpClick: () -> Unit,
    points: Int,
    onPointsChange: (Int) -> Unit
) {
    val symbols = listOf(
        R.drawable.bell, R.drawable.cherry, R.drawable.clover,
        R.drawable.diamond, R.drawable.lemon, R.drawable.seven
    )
    var sym1 by remember { mutableIntStateOf(R.drawable.bell)}
    var sym2 by remember { mutableIntStateOf(R.drawable.cherry)}
    var sym3 by remember { mutableIntStateOf(R.drawable.clover)}
    var isSpinning1 by remember { mutableStateOf(false)}
    var isSpinning2 by remember { mutableStateOf(false)}
    var isSpinning3 by remember { mutableStateOf(false)}
    var sym1Job: Job? = null
    var sym2Job: Job? = null
    var sym3Job: Job? = null
    var stopCount by remember {mutableIntStateOf(0)}
    var speed by remember { mutableStateOf("Normal")}
    var time by remember { mutableLongStateOf(80) }
    var resultText by remember { mutableStateOf("")}

    val coroutineScope = rememberCoroutineScope()

    fun checkResult() {
        if (!isSpinning1 && !isSpinning2 && !isSpinning3) {
            if (sym1 == sym2 && sym2 == sym3) {
                resultText = "🎉 JACKPOT!"
                onPointsChange(points + 100)
            } else if (sym1 == sym2 || sym2 == sym3 || sym1 == sym3){
                resultText = "😮‍💨 Almost there!"
                onPointsChange(points + 50)
            } else {
                resultText =  "😅 Try Again"
            }
        }
    }

    fun speed() {
        when (speed) {
            "Normal" -> {
                time = 180
            }

            "Fast" -> {
                time = 150
            }

            "Slow" -> {
                time = 200
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier.background(color = Color.Green).padding(20.dp)
    ) {
        Row (horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.padding(25.dp).fillMaxWidth()){
            Image(
                painter = painterResource(id = sym1),
                contentDescription = null,
                modifier = modifier.size(100.dp)
            )
            Image(
                painter = painterResource(id = sym2),
                contentDescription = null,
                modifier = modifier.size(100.dp)
            )
            Image(
                painter = painterResource(id = sym3),
                contentDescription = null,
                modifier = modifier.size(100.dp)
            )
        }
        Text(resultText, fontSize = 30.sp)
        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
            RadioGroup(
                labelText = "Adjust Spinning Speed",
                radioOptions = listOf("Fast", "Normal", "Slow"),
                selectedOption = speed,
                onSelected = { speed = it}
            )
        }
        Row (horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.padding(25.dp).fillMaxWidth()){
            if (isSpinning3) {
                Button(
                    onClick = {
                        stopCount++
                        when (stopCount) {
                            1 -> {
                                isSpinning1 = false
                                sym1Job?.cancel()
                            }
                            2 -> {
                                isSpinning2 = false
                                sym2Job?.cancel()
                            }
                            3 -> {
                                isSpinning3 = false
                                sym3Job?.cancel()
                                checkResult()
                            }
                        }
                    }
                ) {
                    Text("STOP")
                }
            } else {
                Button(
                    onClick = {
                        stopCount = 0
                        isSpinning1 = true
                        isSpinning2 = true
                        isSpinning3 = true
                        speed()
                        sym1Job = coroutineScope.launch(Dispatchers.Default) {
                            while (isSpinning1) {
                                sym1 = symbols.random()
                                delay(time)
                            }
                        }
                        sym2Job = coroutineScope.launch(Dispatchers.Default) {
                            while (isSpinning2) {
                                sym2 = symbols.random()
                                delay(time)
                            }
                        }
                        sym3Job = coroutineScope.launch(Dispatchers.Default) {
                            while (isSpinning3) {
                                sym3 = symbols.random()
                                delay(time)
                            }
                        }

                    }
                ) {
                    Text("SPIN")
                }
            }
            Button(
                onClick = {
                    sym1 = R.drawable.bell
                    sym2 = R.drawable.cherry
                    sym3 = R.drawable.clover
                    isSpinning1 = false
                    isSpinning2 = false
                    isSpinning3 = false
                    resultText = ""
                }
            ) {
                Text("RESET")
            }
            Button(
                onClick = onUpClick
            ) {
                Text("POINTS")
            }
        }
    }
}

@Composable
fun RadioGroup(
    labelText: String,
    radioOptions: List<String>,
    selectedOption: String,
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isSelectedOption: (String) -> Boolean = { selectedOption == it}
    Column(modifier = modifier.padding(10.dp),
        verticalArrangement = Arrangement.SpaceEvenly) {
        Text(
            text = labelText,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = modifier.align(Alignment.CenterHorizontally))
        Row ( modifier = modifier.fillMaxWidth().padding(10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly) {
            radioOptions.forEach { option ->
                Row(
                    modifier = Modifier
                        .selectable(
                            selected = isSelectedOption(option),
                            onClick = { onSelected(option) },
                            role = Role.RadioButton
                        )
                        .padding(start = 10.dp, end = 6.dp),
                ) {
                    RadioButton(
                        selected = isSelectedOption(option),
                        onClick = null,
                        modifier = Modifier
                    )
                    Text(text = option)
                }
            }
        }
    }
}













