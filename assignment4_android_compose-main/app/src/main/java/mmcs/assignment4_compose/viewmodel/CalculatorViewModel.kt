package mmcs.assignment4_compose.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CalculatorViewModel: ViewModel(), Calculator {
    var _display = MutableStateFlow("0")
    override var display = _display.asStateFlow()
    override fun addDigit(dig: Int) {
        var current = display.value
        _display.value = (if (current == "0") dig.toString() else current + dig.toString())
    }

    override fun addPoint() {
        if (!_display.value.endsWith('.')) {
            _display.value += '.'
        }
    }

    override fun addOperation(op: Operation) {

        val currentValue = _display.value

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
            _display.value = newValue
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
        _display.value += "=$result"
    }

    override fun clear() {
        var current = _display.value
        var lengthCurrent = current.length
        if (lengthCurrent>0) _display.value = current.substring(0,lengthCurrent-1)
    }

    override fun reset() {
        _display.value = ""
    }

    private fun digitsOperators(): MutableList<Any>
    {
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for(character in _display.value)
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