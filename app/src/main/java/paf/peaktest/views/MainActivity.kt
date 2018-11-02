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
import paf.peaktest.data.Shape
import paf.peaktest.data.ShapeEnum

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
        val posX = controller.getRandomPositionX()
        val posY = controller.getRandomPositionY()

        val shape = controller.createNewShape(shapeValue)

        val shapeView = ImageView(this)
        shapeView.id = shape.id

        val params = RelativeLayout.LayoutParams(SHAPE_WIDTH, SHAPE_HEIGHT)
        params.leftMargin = posX
        params.topMargin = posY

        setShapeToView(shape.currentShape, shapeView)

        displayArea.addView(shapeView, params)

        shapeView.setOnClickListener {
            setShapeToView(getNewShape(shape, shapeView), shapeView)
        }
    }

    private fun getNewShape(shape: Shape, shapeView: ImageView) : ShapeEnum {
        controller.setNewShapeToShape(shape)
        val newShapeValue : ShapeEnum = shape.currentShape

        setShapeToView(newShapeValue, shapeView)

        return newShapeValue
    }

    private fun setShapeToView(shapeValue: ShapeEnum, shapeView: ImageView){
        when(shapeValue) {
            ShapeEnum.SQUARE -> shapeView.setImageResource(R.drawable.shape_square)
            ShapeEnum.CIRCLE -> shapeView.setImageResource(R.drawable.shape_circle)
            ShapeEnum.TRIANGLE -> shapeView.setImageResource(R.drawable.shape_triangle)
        }
    }

    private fun undoAction () {

        if(!controller.actionList.isEmpty() && controller.actionCounter != 0){

            val lastModifiedShape = controller.undoAction()
            val shapeView = displayArea.findViewById<ImageView>(lastModifiedShape!!.id)

            if(!lastModifiedShape.actionChangingShapeList.isEmpty()){
                setShapeToView(lastModifiedShape.currentShape, shapeView)
            }
            else {
                displayArea.removeView(shapeView)
            }
        }
    }

    override fun onClick(v: View?) {
    }

}
