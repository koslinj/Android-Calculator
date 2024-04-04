package koslin.jan.calculator

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun openSimpleCalculatorActivity(v: View) {
        val intent = Intent(this, SimpleCalculator::class.java)
        startActivity(intent)
    }

    fun openAdvancedCalculatorActivity(v: View) {
        val intent = Intent(this, AdvancedCalculator::class.java)
        startActivity(intent)
    }

    fun openAuthorActivity(v: View) {
        val intent = Intent(this, About::class.java)
        startActivity(intent)
    }

    fun exitApp(view: View) {
        finish();
        System.exit(0);
    }
}