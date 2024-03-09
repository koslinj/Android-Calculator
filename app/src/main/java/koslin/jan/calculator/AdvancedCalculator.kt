package koslin.jan.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import koslin.jan.calculator.databinding.ActivityAdvancedCalculatorBinding
import org.mariuszgromada.math.mxparser.Expression


class AdvancedCalculator : AppCompatActivity() {
    val HELPER_STATE = "HELPER"
    val RESULT_STATE = "RESULT"
    private lateinit var binding: ActivityAdvancedCalculatorBinding

    private var canPutDecimal = false
    private var canMakeOperation = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdvancedCalculatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.helperTv.text = "log(3,27)"
        //binding.helperTv.text = savedInstanceState?.getCharSequence(HELPER_STATE);
        binding.resultTv.text = savedInstanceState?.getCharSequence(RESULT_STATE);
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putCharSequence(HELPER_STATE, binding.helperTv.text)
        outState.putCharSequence(RESULT_STATE, binding.resultTv.text)
    }

    fun handleNumber(view: View) {
        view as Button
        binding.helperTv.append(view.text)
        canPutDecimal = true
        canMakeOperation = true
    }

    fun handleOperation(view: View) {
        view as Button
        if (canMakeOperation) {
            if (view.text == "sin" ||
                view.text == "cos" ||
                view.text == "tan" ||
                view.text == "ln" ||
                view.text == "sqrt" ||
                view.text == "log"
            ) {
                val currentText = binding.helperTv.text.toString()
                val newText = "${view.text}($currentText)"
                binding.helperTv.text = newText
            } else if (view.text == "+/-") {
                val currentText = binding.helperTv.text.toString()
                if (currentText[0] == '-') binding.helperTv.text = currentText
                else binding.helperTv.text = "-$currentText"
                canMakeOperation = true
                return
            } else if (view.text == "x^2") {
                val currentText = binding.helperTv.text.toString()
                val newText = "($currentText)^2"
                binding.helperTv.text = newText
            } else if (view.text == "x^y") {
                val currentText = binding.helperTv.text.toString()
                val newText = "($currentText)^"
                binding.helperTv.text = newText
            } else {
                binding.helperTv.append(view.text)
            }
            canMakeOperation = false
        }
    }

    fun handleClear(view: View) {
        binding.helperTv.text = ""
        binding.resultTv.text = ""
    }

    fun handleBackspace(view: View) {
        val len = binding.helperTv.text.length
        if (len > 0) {
            if (binding.helperTv.text.get(len - 1) == ')') {
                var i = 0
                while (binding.helperTv.text.get(i) != '(') {
                    i++
                }
                binding.helperTv.text = binding.helperTv.text.substring(i + 1, len - 1)
            } else binding.helperTv.text = binding.helperTv.text.substring(0, len - 1)
        }
    }

    fun handleEquals(view: View) {
        val e = Expression(binding.helperTv.text.toString())
        binding.resultTv.text = e.calculate().toString()
    }
}