package koslin.jan.calculator

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel: ViewModel() {
    private val _helper = MutableStateFlow("")
    val helper: StateFlow<String> = _helper.asStateFlow()

    private val _result = MutableStateFlow("")
    val result: StateFlow<String> = _result.asStateFlow()

    fun setHelper(s: String) {
        _helper.value = s
    }
    fun setResult(s: String) {
        _result.value = s
    }

}