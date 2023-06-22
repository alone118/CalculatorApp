package com.example.calculatorapp

import kotlinx.coroutines.flow.MutableStateFlow

class Calculator {

    val calculateFlow = MutableStateFlow(CalculatorModel())

    fun enterNumber(number: String) {
        val currentCalculate: CalculatorModel = calculateFlow.value
        if (currentCalculate.operation.isEmpty()) {
            val newModel = currentCalculate.copy(
                numberFirst = currentCalculate.numberFirst + number
            )
            calculateFlow.tryEmit(newModel)
            return
        }

        val newModel = currentCalculate.copy(
            numberSecond = currentCalculate.numberSecond + number
        )
        calculateFlow.tryEmit(newModel)
    }

    fun enterOperation(operation: String) {
        val currentCalculate: CalculatorModel = calculateFlow.value
        if (currentCalculate.numberFirst.isNotEmpty() &&
            currentCalculate.numberFirst.lastOrNull() != '.'
        ) {
            val newModel = currentCalculate.copy(
                operation = operation
            )
            calculateFlow.tryEmit(newModel)
        }
    }

    fun enterCalculate() {
        val currentCalculate: CalculatorModel = calculateFlow.value

        if (currentCalculate.numberSecond.lastOrNull() == '.') return
        val numberFirst = currentCalculate.numberFirst.toDoubleOrNull() ?: return
        val numberSecond = currentCalculate.numberSecond.toDoubleOrNull() ?: return

        val result = when (currentCalculate.operation) {
            "+" -> numberFirst + numberSecond
            "-" -> numberFirst - numberSecond
            "x" -> numberFirst * numberSecond
            "/" -> numberFirst / numberSecond
            else -> 0.0
        }

        calculateFlow.tryEmit(
            currentCalculate.copy(
                numberFirst = result.toString(),
                numberSecond = "",
                operation = ""
            )
        )
    }

    fun enterDelete() {
        val currentCalculate: CalculatorModel = calculateFlow.value

        when {
            currentCalculate.numberSecond.isNotEmpty() -> {
                val numberSecond = currentCalculate.numberSecond.dropLast(1)
                calculateFlow.tryEmit(
                    currentCalculate.copy(
                        numberSecond = numberSecond
                    )
                )
            }

            currentCalculate.operation.isNotEmpty() -> {
                calculateFlow.tryEmit(
                    currentCalculate.copy(
                        operation = ""
                    )
                )
            }

            currentCalculate.numberFirst.isNotEmpty() -> {
                val numberFirst = currentCalculate.numberFirst.dropLast(1)
                calculateFlow.tryEmit(
                    currentCalculate.copy(
                        numberFirst = numberFirst
                    )
                )
            }
        }
    }

    fun enterDecimal() {
        val currentCalculate: CalculatorModel = calculateFlow.value

        if (currentCalculate.operation.isEmpty()
            && !currentCalculate.numberFirst.contains(".")
                && currentCalculate.numberFirst.isNotEmpty()
        ){
            calculateFlow.tryEmit(
                currentCalculate.copy(
                    numberFirst = currentCalculate.numberFirst + "."
                )
            )
            return
        } else if (
            currentCalculate.operation.isNotEmpty()
            && !currentCalculate.numberSecond.contains(".")
            && currentCalculate.numberSecond.isNotEmpty()

        ){
            calculateFlow.tryEmit(
                currentCalculate.copy(
                    numberSecond = currentCalculate.numberSecond + "."
                )
            )
            return
        }
    }

    fun enterClear() {
        calculateFlow.tryEmit(CalculatorModel())
    }
}