package easy.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import easy.calculator.ui.theme.CalculatorTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Row (modifier = Modifier.padding(30.dp)) {

                    }
                    Column (
                        modifier = Modifier.padding(30.dp)
                    ) {
                        val calculator = Calculator(10, 2)
                        Calculate(calculator)
                        CalculatorSymbols()

                    }
                }


            }
        }
    }
}

data class Calculator(var number1: Int, var number2: Int){
    fun add(): Int {
        return number1 + number2
    }
    fun substract(): Int {
        return number1 - number2
    }
    fun multiplicate(): Int {
        return number1 * number2
    }
    fun divide (): Int {
        return number1 / number2
    }
}

@Composable
private fun CalculatorSymbols() {
    var numbersAndSymbols = listOf(
        listOf("C", "()", "%", "/"),
        listOf("7", "8", "9", "X"),
        listOf("4", "5", "6", "-"),
        listOf("1", "2", "3", "+"),
        listOf("+-", "0", ".", "=")
    )
    Column {
        numbersAndSymbols.forEach { symbols ->
            Row (
//                horizontalArrangement = Arrangement.SpaceEvenly,

            ) {
                symbols.forEach { symbol ->
                    Button(
                        onClick = { /*TODO*/ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Magenta,
                            contentColor = Color.Yellow
                        ),
                        modifier = Modifier
                            .padding(5.dp)
                            .height(70.dp)
                            .width(70.dp)


                    ) {
                        Text(text = symbol,
                            style = MaterialTheme.typography.bodyLarge,
                            fontSize = 21.sp)
                    }
                }

            }
        }

    }


}

@Composable
private fun Calculate(action: Calculator) {
//    var addItems = action.add()
//    var substractItems = action.substract()
//    var multiplicateItems = action.multiplicate()
//    var divideItems = action.divide()

    Column (
        modifier = Modifier
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text(text = "Hello! :)")
        Text(text = "Here you have simple calculator")
        Text(text = "Enjoy your calculations! ")
    }

}



