package koslin.jan.calculator

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import koslin.jan.calculator.databinding.ActivitySimpleCalculatorBinding

class SimpleCalculator : AppCompatActivity() {
    private lateinit var binding: ActivitySimpleCalculatorBinding
    private val vm: MainViewModel by viewModels()

    private var canMakeOperation = false
    private var canInsertDecimal = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySimpleCalculatorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.helperTv.text = vm.helper.value
        binding.resultTv.text = vm.result.value

        val orientation = resources.configuration.orientation

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Change orientation to left-right for landscape
            binding.layoutSimple.orientation = LinearLayout.HORIZONTAL
            binding.helperTv.textAlignment = View.TEXT_ALIGNMENT_VIEW_START
            binding.resultTv.textAlignment = View.TEXT_ALIGNMENT_VIEW_START
            binding.writingPart.layoutParams.width = 0
            binding.writingPart.layoutParams.height = LinearLayout.LayoutParams.MATCH_PARENT
            binding.buttonsPart.layoutParams.width = 0
            binding.buttonsPart.layoutParams.height = LinearLayout.LayoutParams.MATCH_PARENT
        } else {
            // Keep the default top-down orientation for portrait
            binding.layoutSimple.orientation = LinearLayout.VERTICAL
            binding.helperTv.textAlignment = View.TEXT_ALIGNMENT_VIEW_END
            binding.resultTv.textAlignment = View.TEXT_ALIGNMENT_VIEW_END
            binding.writingPart.layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT
            binding.writingPart.layoutParams.height = 0
            binding.buttonsPart.layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT
            binding.buttonsPart.layoutParams.height = 0
        }
    }

    fun handleNumber(view: View) {
        view as Button
        if (view.text == ",") {
            if (canInsertDecimal) {
                vm.setHelper("${vm.helper.value}.")
                binding.helperTv.text = vm.helper.value
                canInsertDecimal = false
            }
        } else {
            vm.setHelper(vm.helper.value + view.text)
            binding.helperTv.text = vm.helper.value
            canInsertDecimal = true
            canMakeOperation = true
        }
    }

    fun handleOperation(view: View) {
        view as Button
        if (canMakeOperation && canInsertDecimal) {
            vm.setHelper(vm.helper.value + view.text)
            binding.helperTv.text = vm.helper.value
            canMakeOperation = false
            canInsertDecimal = true
        }
    }

    fun handleClear(view: View) {
        vm.setHelper("")
        binding.helperTv.text = vm.helper.value
        vm.setResult("")
        binding.resultTv.text = vm.result.value
    }

    fun handleBackspace(view: View) {
        val len = binding.helperTv.length()
        if (len > 0) {
            vm.setHelper(vm.helper.value.substring(0, len - 1))
            binding.helperTv.text = vm.helper.value
        }
    }

    fun handleEquals(view: View) {
        vm.setResult(calculate())
        binding.resultTv.text = vm.result.value
    }

    private fun calculate(): String {
        val equationParts = equationParts()
        if (equationParts.isEmpty()) return ""

        val multiplyDivide = removeMultiplyAndDivision(equationParts)
        if (multiplyDivide.isEmpty()) return ""

        val result = addSubtractCalculate(multiplyDivide)
        return result.toString()
    }

    private fun addSubtractCalculate(list: MutableList<Any>): Float {
        var res = list[0] as Float

        for (i in list.indices) {
            if (list[i] is Char && i != list.lastIndex) {
                val operator = list[i]
                val nextNumber = list[i + 1] as Float
                if (operator == '+') res += nextNumber
                if (operator == '-') res -= nextNumber
            }
        }

        return res
    }

    private fun removeMultiplyAndDivision(prevList: MutableList<Any>): MutableList<Any> {
        var list = prevList
        while (list.contains('X') || list.contains('÷')) {
            list = calcMultiplyAndDivision(list)
        }
        return list
    }

    private fun calcMultiplyAndDivision(prevList: MutableList<Any>): MutableList<Any> {
        val newList = mutableListOf<Any>()
        var restartIndex = prevList.size

        for (i in prevList.indices) {
            if (prevList[i] is Char && i != prevList.lastIndex && i < restartIndex) {
                val operator = prevList[i]
                val prevNumber = prevList[i - 1] as Float
                val nextNumber = prevList[i + 1] as Float
                when (operator) {
                    'X' -> {
                        newList.add(prevNumber * nextNumber)
                        restartIndex = i + 1
                    }

                    '÷' -> {
                        newList.add(prevNumber / nextNumber)
                        restartIndex = i + 1
                    }

                    else -> {
                        newList.add(prevNumber)
                        newList.add(operator)
                    }
                }
            }
            if (i > restartIndex)
                newList.add(prevList[i])
        }

        return newList
    }

    private fun equationParts(): MutableList<Any> {
        val list = mutableListOf<Any>()
        var current = ""

        for (char in binding.helperTv.text) {
            if (char.isDigit() || char == '.') {
                current += char
            } else {
                list.add(current.toFloat())
                current = ""
                list.add(char)
            }
        }

        if (current != "") {
            list.add(current.toFloat())
        }

        return list;
    }
}