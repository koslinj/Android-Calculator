package koslin.jan.calculator

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import koslin.jan.calculator.databinding.ActivitySimpleCalculatorBinding
import org.mariuszgromada.math.mxparser.Expression

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
        val e = Expression(binding.helperTv.text.toString())
        val res = e.calculate().toString()
        vm.setResult(res)
        binding.resultTv.text = vm.result.value
    }
}