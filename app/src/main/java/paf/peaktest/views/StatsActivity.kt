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

class StatsActivity : AppCompatActivity(), View.OnClickListener{

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
        squareNumber = controller.getSquareNumber()
        circleNumber = controller.getCircleNumber()
        triangleNumber = controller.getTriangleNumber()

        squareNumberTextView.text = "$squareNumber"
        circleNumberTextView.text = "$circleNumber"
        triangleNumberTextView.text = "$triangleNumber"

        deleteSquaresButton.setOnClickListener {
            controller.deleteShapeGroup(ShapeEnum.SQUARE)
            squareNumberTextView.text = "${controller.getSquareNumber()}"
        }

        deleteCirclesButton.setOnClickListener {
            controller.deleteShapeGroup(ShapeEnum.CIRCLE)
            circleNumberTextView.text = "${controller.getCircleNumber()}"
        }

        deleteTrianglesButton.setOnClickListener {
            controller.deleteShapeGroup(ShapeEnum.TRIANGLE)
            triangleNumberTextView.text = "${controller.getTriangleNumber()}"
        }

        backToMainActivityButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(CONTROLLER_KEY, controller)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun onClick(p0: View?) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBackPressed() {
    }


}
