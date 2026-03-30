package com.example.mycalculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var tvDisplay: TextView
    private var currentNumber = ""
    private var operator = ""
    private var firstNumber = 0.0
    private var isNewOperation = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvDisplay = findViewById(R.id.tvDisplay)

        setupNumberButtons()
        setupOperatorButtons()
        setupFunctionButtons()
    }

    private fun setupNumberButtons() {
        val numberButtons = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        )

        numberButtons.forEach { id ->
            findViewById<Button>(id).setOnClickListener {
                val number = (it as Button).text.toString()
                appendNumber(number)
            }
        }

        findViewById<Button>(R.id.btnDecimal).setOnClickListener {
            appendDecimal()
        }
    }

    private fun setupOperatorButtons() {
        findViewById<Button>(R.id.btnAdd).setOnClickListener { setOperator("+") }
        findViewById<Button>(R.id.btnSubtract).setOnClickListener { setOperator("-") }
        findViewById<Button>(R.id.btnMultiply).setOnClickListener { setOperator("×") }
        findViewById<Button>(R.id.btnDivide).setOnClickListener { setOperator("÷") }
        findViewById<Button>(R.id.btnPercent).setOnClickListener { calculatePercent() }
        findViewById<Button>(R.id.btnEquals).setOnClickListener { calculateResult() }
    }

    private fun setupFunctionButtons() {
        findViewById<Button>(R.id.btnClear).setOnClickListener { clear() }
        findViewById<Button>(R.id.btnDelete).setOnClickListener { delete() }
    }

    private fun appendNumber(number: String) {
        if (isNewOperation) {
            currentNumber = number
            isNewOperation = false
        } else {
            currentNumber += number
        }
        updateDisplay()
    }

    private fun appendDecimal() {
        if (isNewOperation) {
            currentNumber = "0."
            isNewOperation = false
        } else if (!currentNumber.contains(".")) {
            currentNumber += "."
        }
        updateDisplay()
    }

    private fun setOperator(op: String) {
        if (currentNumber.isNotEmpty()) {
            if (operator.isNotEmpty()) {
                calculateResult()
            }
            firstNumber = currentNumber.toDoubleOrNull() ?: 0.0
            operator = op
            isNewOperation = true
        }
    }

    private fun calculateResult() {
        if (operator.isEmpty() || currentNumber.isEmpty()) return

        val secondNumber = currentNumber.toDoubleOrNull() ?: 0.0
        val result = when (operator) {
            "+" -> firstNumber + secondNumber
            "-" -> firstNumber - secondNumber
            "×" -> firstNumber * secondNumber
            "÷" -> if (secondNumber != 0.0) firstNumber / secondNumber else 0.0
            else -> 0.0
        }

        currentNumber = formatResult(result)
        operator = ""
        isNewOperation = true
        updateDisplay()
    }

    private fun calculatePercent() {
        if (currentNumber.isNotEmpty()) {
            val number = currentNumber.toDoubleOrNull() ?: 0.0
            currentNumber = formatResult(number / 100)
            updateDisplay()
        }
    }

    private fun clear() {
        currentNumber = ""
        operator = ""
        firstNumber = 0.0
        isNewOperation = true
        tvDisplay.text = "0"
    }

    private fun delete() {
        if (currentNumber.isNotEmpty()) {
            currentNumber = currentNumber.dropLast(1)
            updateDisplay()
        }
    }

    private fun updateDisplay() {
        tvDisplay.text = if (currentNumber.isEmpty()) "0" else currentNumber
    }

    private fun formatResult(result: Double): String {
        return if (result % 1.0 == 0.0) {
            result.toInt().toString()
        } else {
            result.toString()
        }
    }
}