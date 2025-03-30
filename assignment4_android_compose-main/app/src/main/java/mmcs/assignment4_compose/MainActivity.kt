package mmcs.assignment4_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mmcs.assignment4_compose.ui.theme.Assignment4_composeTheme
import mmcs.assignment4_compose.viewmodel.Operation
import mmcs.assignment4_compose.viewmodel.Calculator
import mmcs.assignment4_compose.viewmodel.CalculatorViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: CalculatorViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Assignment4_composeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CalculatorScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun CalculatorScreen(viewModel: Calculator) {
    val display by viewModel.display.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp))
        {

            Text(
                text = display,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                fontSize = 48.sp,
                textAlign = TextAlign.End
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp))
                {

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CalculatorButton("AC",true) { viewModel.reset() }
                        CalculatorButton("C",true) { viewModel.clear() }
                        CalculatorButton("%",true) { viewModel.addOperation(Operation.PERC) }
                        CalculatorButton("/", true) { viewModel.addOperation(Operation.DIV) }
                    }


                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CalculatorButton("7") { viewModel.addDigit(7) }
                        CalculatorButton("8") { viewModel.addDigit(8) }
                        CalculatorButton("9") { viewModel.addDigit(9) }
                        CalculatorButton("×", true) { viewModel.addOperation(Operation.MUL) }
                    }


                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CalculatorButton("4") { viewModel.addDigit(4) }
                        CalculatorButton("5") { viewModel.addDigit(5) }
                        CalculatorButton("6") { viewModel.addDigit(6) }
                        CalculatorButton("-", true) { viewModel.addOperation(Operation.SUB) }
                    }


                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                            CalculatorButton("1") { viewModel.addDigit(1) }
                            CalculatorButton("2") { viewModel.addDigit(2) }
                            CalculatorButton("3") { viewModel.addDigit(3) }
                            CalculatorButton("+", true) { viewModel.addOperation(Operation.ADD) }
                        }


                                Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp))
                                {
                                    CalculatorButton("+/-",true) { viewModel.addOperation(Operation.NEG) }
                                    CalculatorButton("0") { viewModel.addDigit(0) }
                                    CalculatorButton(".",true) { viewModel.addPoint() }
                                    CalculatorButton("=", true) { viewModel.compute() }
                                }

                }
        }
}

@Composable
fun CalculatorButton(
    text: String,
    isOperator: Boolean = false,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.size(80.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isOperator) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.secondaryContainer
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 2.dp,
            pressedElevation = 8.dp
        )
    ) {
        Text(text = text, fontSize = 24.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorScreenPreview() {
    Assignment4_composeTheme {
        CalculatorScreen(CalculatorViewModel())
    }
}