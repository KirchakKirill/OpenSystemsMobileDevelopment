package com.example.calculator.viewmodel

import androidx.databinding.BaseObservable
import androidx.databinding.ObservableField

class CalculatorViewModel: BaseObservable(), Calculator {
    override var display = ObservableField<String>("")


    override fun addDigit(dig: Int) {
        var current = display.get().toString()
        display.set(if (current == "0") dig.toString() else current + dig.toString())
    }

    override fun addPoint() {
        if (!display.get()!!.endsWith(".")) {
            display.set(display.get() + ".")
        }
    }

    override fun addOperation(op: Operation) {
        val currentValue = display.get() ?: return


        val endsWithOperatorOrDot = when (currentValue.lastOrNull()) {
            '.', '+', '-', '*', '/', '%' -> true
            else -> false
        }


        if (!endsWithOperatorOrDot) {
            val newValue = when (op) {
                Operation.ADD -> "$currentValue+"
                Operation.SUB -> "$currentValue-"
                Operation.DIV -> "$currentValue/"
                Operation.MUL -> "$currentValue*"
                Operation.PERC -> "$currentValue%"
                Operation.NEG -> "$currentValue!"
            }
            display.set(newValue)
        }


    }

    override fun compute() {
        val digitsOperators = digitsOperators()

        val endsWithOperatorOrDot = when (digitsOperators.lastOrNull())
        {   '+', '-', '*', '/', '%' -> true
            else -> false
        }
        if(endsWithOperatorOrDot) return


        if(digitsOperators.isEmpty()|| digitsOperators.size==1)
        {
            return
        }

        val timesDivision = timesDivisionCalculate(digitsOperators)
        if(timesDivision.isEmpty()) return

        val result = addSubtractCalculate(timesDivision)
        display.set(display.get().toString()+"="+result.toString())
    }

    override fun clear() {
        var current = display.get().toString()
        var lengthCurrent = current.length
        if (lengthCurrent>0) display.set(current.substring(0,lengthCurrent-1))
    }

    override fun reset() {
        display.set("")
    }

    private fun digitsOperators(): MutableList<Any>
    {
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for(character in display.get().toString())
        {
            if(character.isDigit() || character == '.')
                currentDigit += character
            else
            {
                if (currentDigit!="") list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }

        if(currentDigit != "")
            list.add(currentDigit.toFloat())

        return list
    }

    private fun timesDivisionCalculate(passedList: MutableList<Any>): MutableList<Any>
    {
        var list = passedList
        while (list.contains('*') || list.contains('/') || list.contains('%') || list.contains('!'))
        {
            list = calcTimesDiv(list)
        }
        return list
    }
    private fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any> {
        val newList = mutableListOf<Any>()
        var i = 0

        while (i < passedList.size) {
            when (val item = passedList[i]) {
                '!' -> {
                    if (i > 0 && passedList[i-1] is Float) {
                        val number = passedList[i-1] as Float
                        if (newList.isNotEmpty() && newList.last() == passedList[i-1]) {
                            newList.removeAt(newList.size - 1)
                        }
                        newList.add(-number)
                        i += 1
                    } else {
                        newList.add(item)
                        i += 1
                    }
                }
                '*', '/', '%' -> {
                    if (i > 0 && i < passedList.size - 1 &&
                        passedList[i-1] is Float && passedList[i+1] is Float) {
                        val left = passedList[i-1] as Float
                        val right = passedList[i+1] as Float
                        val result = when (item) {
                            '*' -> left * right
                            '/' -> left / right
                            '%' -> left % right
                            else -> throw IllegalStateException()
                        }

                        if (newList.isNotEmpty() && newList.last() == passedList[i-1]) {
                            newList.removeAt(newList.size - 1)
                        }

                        newList.add(result)
                        i += 2
                    } else {
                        newList.add(item)
                        i += 1
                    }
                }
                else -> {
                    newList.add(item)
                    i += 1
                }
            }
        }

        return newList
    }

    private fun addSubtractCalculate(passedList: MutableList<Any>): Float
    {
        var result = passedList[0] as Float

        for(i in passedList.indices)
        {
            if(passedList[i] is Char && i != passedList.lastIndex)
            {
                val operator = passedList[i]
                val nextDigit = passedList[i + 1] as Float
                if (operator == '+')
                    result += nextDigit
                if (operator == '-')
                    result -= nextDigit
            }
        }

        return result
    }
}