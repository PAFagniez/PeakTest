package paf.peaktest.views

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_stats.*
import paf.peaktest.R
import paf.peaktest.controllers.Controller

class StatsActivity : AppCompatActivity() {

    companion object {
        private val CONTROLLER_KEY = "CONTROLLER"
    }

    private var squareNumber: Int = 0
    private var circleNumber: Int = 0
    private var triangleNumber: Int = 0
    private lateinit var controller: Controller

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)

        controller = intent.getSerializableExtra(CONTROLLER_KEY) as Controller
        squareNumber = controller.squareNumber
        circleNumber = controller.circleNumber
        triangleNumber = controller.triangleNumber

        squareNumberTextView.text = "$squareNumber"
        circleNumberTextView.text = "$circleNumber"
        triangleNumberTextView.text = "$triangleNumber"
    }
}
