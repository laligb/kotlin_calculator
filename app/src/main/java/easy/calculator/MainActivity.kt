package easy.calculator

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
                    Row (modifier = Modifier.padding(30.dp)) {

                    }
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
    // Updated regex to properly handle negative numbers
    val regex = Regex("\\d+\\.?\\d*%?|[+*/-]")
    val tokens = regex.findAll(expression).map { it.value }.toList()

    //println("TOKENS = "+ tokens)

    // Convert string to real numbers and operators
    val numbers = mutableListOf<Double>()
    val operators = mutableListOf<Char>()



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
    println("tokens="+tokens+" numbers="+numbers+" operators="+operators)

    // Evaluate the expression
    var result = numbers[0]
    var currentIndex = 1

    for (operator in operators) {
        val nextNumber = numbers[currentIndex]
        result = when (operator) {
            '+' -> result + nextNumber
            '-' -> result - nextNumber
            '*' -> result * nextNumber
            '/' -> result / nextNumber
            else -> throw IllegalArgumentException("Unknown operator: $operator")
        }
        currentIndex++
        println( "tokens bla bla operator="+operator)
    }
    return result
}



// Main
@SuppressLint("MutableCollectionMutableState")
@Composable
private fun Calculate() {

    var clickedSymbol by remember { mutableStateOf("") }
    var outputs by remember { mutableStateOf("")    }
    var numbers by remember { mutableStateOf(mutableListOf<Double>()) }
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
                "+-" -> {
                    if (outputs.isEmpty()){
                        outputs = "-"
                        }
                } // Change sign add to string in a first only
                "=" -> {
                   result = calculateExpression(outputs)
                    outputs = result?.toString() ?: ""
                } // take string and give result here write function of calculation
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

            //outputs = outputs  +  clickedSymbol
        }

        PrintResult(outputs)

    }

}


@Composable
private fun CalculatorSymbols(clicked: (String) -> Unit) {
    var numbersAndSymbols = listOf(
        listOf("C", "<-", "%", "/"),
        listOf("7", "8", "9", "*"),
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
                        onClick = { clicked(symbol) },
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




