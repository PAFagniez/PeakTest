package paf.peaktest.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_stats.*
import paf.peaktest.R
import paf.peaktest.controllers.Controller
import paf.peaktest.data.ShapeEnum
import paf.peaktest.data.ShapeEnum.*

class StatsActivity : AppCompatActivity(), View.OnClickListener{

    companion object {
        private const val CONTROLLER_KEY = "CONTROLLER"
    }

    private lateinit var controller: Controller

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)

        controller = intent.getSerializableExtra(CONTROLLER_KEY) as Controller

        squareNumberTextView.text = "${getShapeNumber(SQUARE)}"
        circleNumberTextView.text = "${getShapeNumber(CIRCLE)}"
        triangleNumberTextView.text = "${getShapeNumber(TRIANGLE)}"

        deleteSquaresButton.setOnClickListener {
            controller.deleteShapeGroup(SQUARE)
            squareNumberTextView.text = "${getShapeNumber(SQUARE)}"
        }

        deleteCirclesButton.setOnClickListener {
            controller.deleteShapeGroup(CIRCLE)
            circleNumberTextView.text = "${getShapeNumber(CIRCLE)}"
        }

        deleteTrianglesButton.setOnClickListener {
            controller.deleteShapeGroup(TRIANGLE)
            triangleNumberTextView.text = "${getShapeNumber(TRIANGLE)}"
        }

        backToMainActivityButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(CONTROLLER_KEY, controller)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    private fun getShapeNumber(shape: ShapeEnum): Int{
        return when(shape) {
            SQUARE -> controller.getSquareNumber()
            CIRCLE -> controller.getCircleNumber()
            TRIANGLE -> controller.getTriangleNumber()
        }
    }

    override fun onClick(p0: View?) {

    }

    override fun onBackPressed() {
    }


}
