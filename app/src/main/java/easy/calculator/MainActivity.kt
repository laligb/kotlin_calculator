package easy.calculator.lb

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
                    //Row (modifier = Modifier.padding(30.dp)) {

                    //}
                    Column (
                        modifier = Modifier.padding(30.dp)
                    ) {
                        Welcome()
                        Calculate()
                    }
                }


            }
        }
    }
}


// Taking expression in a String and calculate the result in Double
fun calculateExpression(expression: String): Double {
    // Regex to match numbers (including percentages) and operators
    val regex = Regex("\\d+\\.?\\d*%?|[+*/-]")
    val tokens = regex.findAll(expression).map { it.value }.toList()

    // Lists to hold numbers and operators
    val numbers = mutableListOf<Double>()
    val operators = mutableListOf<Char>()

    // Parse tokens into numbers and operators
    for (token in tokens) {
        when {
            token.endsWith("%") -> {
                // Convert percentage to a decimal and add to numbers
                val number = token.dropLast(1).toDouble() / 100.0
                numbers.add(number)
            }
            token.toDoubleOrNull() != null -> numbers.add(token.toDouble())
            token in "+-*/" -> operators.add(token[0])
        }
    }



    return evaluateExpression(numbers, operators)
}

fun evaluateExpression(numbers: List<Double>, operators: List<Char>): Double {
    // Mutables for numbers and operators, since we'll modify them
    val mutableNumbers = numbers.toMutableList()
    val mutableOperators = operators.toMutableList()

    // First, handle '*' and '/' operations
    var index = 0
    while (index < mutableOperators.size) {
        val operator = mutableOperators[index]
        if (operator == '*' || operator == '/') {
            val result = when (operator) {
                '*' -> mutableNumbers[index] * mutableNumbers[index + 1]
                '/' -> mutableNumbers[index] / mutableNumbers[index + 1]
                else -> throw IllegalArgumentException("Unknown operator: $operator")
            }
            // Replace the numbers at index and index + 1 with the result
            mutableNumbers[index] = result
            mutableNumbers.removeAt(index + 1)
            // Remove the operator
            mutableOperators.removeAt(index)
        } else {
            index++
        }
    }

    // Now, handle '+' and '-' operations
    var result = mutableNumbers[0]
    for (i in 1 until mutableNumbers.size) {
        result = when (mutableOperators[i - 1]) {
            '+' -> result + mutableNumbers[i]
            '-' -> result - mutableNumbers[i]
            else -> throw IllegalArgumentException("Unknown operator: ${mutableOperators[i - 1]}")
        }
    }

    return result
}

// Main
@SuppressLint("MutableCollectionMutableState")
@Composable
private fun Calculate() {

    var clickedSymbol by remember { mutableStateOf("") }
    var outputs by remember { mutableStateOf("")    }
    var result by remember { mutableStateOf<Double?>(null) }


    Column (

    ) {
        CalculatorSymbols {symbol ->
            clickedSymbol = symbol
            when (symbol) {
                "<-" -> outputs = outputs.substring(0, outputs.length - 1)//outputs += getNextParenthesis(outputs)// And here if we found opened "(" then close it with ")"
                "C" -> {
                    outputs = "" // Clean memory
                    }
//                "+-" -> {
//                    if (outputs.isEmpty()){
//                        outputs = "-"
//                        }
//                } // Change sign add to string in a first only
                "=" -> {
                   result = calculateExpression(outputs)
                    outputs = result?.toString() ?: ""
                }
                "." -> {
                    // Check if outputs is not empty and the last character is a digit
                    if (outputs.isNotEmpty() && outputs.last().isDigit()) {
                        val lastNumber = outputs.takeLastWhile { it.isDigit() || it == '.' }
                        if (!lastNumber.contains('.')) {
                            outputs += "."
                        }
                    }
                }
                // Default case
                else -> {
                    // Prevent multiple consecutive operators
                    if (symbol in "+-*/" && outputs.isNotEmpty() && outputs.last() in "+-*/") {
                        outputs = outputs.dropLast(1)
                    }

                    outputs += symbol
                }
             }

        }

        PrintResult(outputs)
    }

}


@Composable
private fun CalculatorSymbols(clicked: (String) -> Unit) {
    var numbersAndSymbols = listOf(
        listOf("C", "<", "%", "/"),
        listOf("7", "8", "9", "*"),
        listOf("4", "5", "6", "-"),
        listOf("1", "2", "3", "+"),
        listOf("", "0", ".", "=")
    )
    Column {
        numbersAndSymbols.forEach { symbols ->
            Row (

            ) {
                symbols.forEach { symbol ->
                    Button(
                        onClick = { clicked(symbol) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Magenta,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .padding(5.dp)
                            .height(70.dp)
                            .width(70.dp)


                    ) {
                        Text(text = symbol,
                            style = MaterialTheme.typography.bodyLarge,
                            fontSize = 24.sp
                            )


                    }
                }

            }

        }

    }
}

@Composable
private fun Welcome() {
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

@Composable
private fun PrintResult(symbol: String) {

    Row (
        modifier = Modifier.padding(30.dp),

    ){
        Text(text = "${symbol} ")
    }
}




