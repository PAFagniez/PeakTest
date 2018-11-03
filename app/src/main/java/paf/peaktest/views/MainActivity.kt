@file:Suppress("PLUGIN_WARNING")

package paf.peaktest.views

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.activity_main.*
import paf.peaktest.R
import paf.peaktest.controllers.Controller
import paf.peaktest.data.ShapeEnum
import paf.peaktest.data.ShapeHolder

class MainActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        private val CONTROLLER_KEY = "CONTROLLER"
        private const val SHAPE_WIDTH = 64
        private const val SHAPE_HEIGHT = 64
    }

    private lateinit var controller: Controller

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        displayArea.post {
            val displayAreaHeight = displayArea.height
            val displayAreaWidth = displayArea.width

            Log.e("SIZE", "height : $displayAreaHeight, width : $displayAreaWidth")
            controller = Controller(displayAreaWidth, displayAreaHeight, SHAPE_WIDTH, SHAPE_HEIGHT)
        }

        squareButton.setOnClickListener {
            createShapeView(ShapeEnum.SQUARE)
        }

        circleButton.setOnClickListener {
            createShapeView(ShapeEnum.CIRCLE)
        }

        triangleButton.setOnClickListener {
            createShapeView(ShapeEnum.TRIANGLE)
        }

        undoButton.setOnClickListener {
            undoAction()
        }

        statsButton.setOnClickListener {
            val statIntent = Intent(this, StatsActivity::class.java)
            statIntent.putExtra(CONTROLLER_KEY, controller)
            startActivity(statIntent)
        }
    }

    private fun createShapeView(shapeValue: ShapeEnum) {

        val shapeHolder = controller.createNewShapeHolder(shapeValue)

        val shapeView = ImageView(this)
        shapeView.id = shapeHolder.id

        val params = RelativeLayout.LayoutParams(SHAPE_WIDTH, SHAPE_HEIGHT)
        params.leftMargin = shapeHolder.posX
        params.topMargin = shapeHolder.posY

        setShapeToView(shapeHolder.currentShape!!, shapeView)

        displayArea.addView(shapeView, params)

        shapeView.setOnClickListener {
            setShapeToView(getNewShape(shapeHolder, shapeView), shapeView)
        }

        shapeView.isLongClickable = true
        shapeView.setOnLongClickListener {
            deleteImageView(shapeView, shapeHolder)
            true
        }
    }

    private fun getNewShape(shapeHolder: ShapeHolder, shapeView: ImageView) : ShapeEnum? {
        controller.setNewShapeToShapeHolder(shapeHolder)
        val newShape : ShapeEnum? = shapeHolder.currentShape

        setShapeToView(newShape, shapeView)

        return newShape
    }

    private fun setShapeToView(shape: ShapeEnum?, shapeView: ImageView){
        when(shape) {
            ShapeEnum.SQUARE -> shapeView.setImageResource(R.drawable.shape_square)
            ShapeEnum.CIRCLE -> shapeView.setImageResource(R.drawable.shape_circle)
            ShapeEnum.TRIANGLE -> shapeView.setImageResource(R.drawable.shape_triangle)
        }
    }

    private fun undoAction () {

        if(!controller.actionList.isEmpty() && controller.actionNumber != 0){

            val lastModifiedShapeHolder = controller.undoAction()
            val currentShape = lastModifiedShapeHolder?.currentShape
            val shapeView = displayArea.findViewById<ImageView>(lastModifiedShapeHolder!!.id)

            if(!(lastModifiedShapeHolder.actionList.isEmpty() || currentShape == null) && shapeView != null){

                setShapeToView(currentShape, shapeView)
                if(shapeView.visibility == View.GONE){
                    shapeView.visibility = View.VISIBLE
                }
            }
            else {
                displayArea.removeView(shapeView)
            }
        }
    }

    private fun deleteImageView(shapeView: ImageView, shapeHolder: ShapeHolder) {
        controller.deleteShapeHolder(shapeHolder)
        shapeView.visibility = View.GONE
//        displayArea.removeView(shapeView)
    }

    override fun onClick(v: View?) {
    }


    override fun onBackPressed() {
        // do nothing.
    }


}
