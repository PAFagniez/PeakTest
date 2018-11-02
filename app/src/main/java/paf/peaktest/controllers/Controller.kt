package paf.peaktest.controllers

import android.os.Build
import android.view.View
import paf.peaktest.data.Shape
import paf.peaktest.data.ShapeEnum
import paf.peaktest.helpers.IdGeneratorHelper
import java.io.Serializable
import java.util.*
import kotlin.collections.LinkedHashMap

class Controller(displayAreaWidth: Int, displayAreaHeight: Int, shapeWidth: Int, shapeHeight: Int) : Serializable {

    private val maxWidth = displayAreaWidth - shapeWidth
    private val minWidth = 0 + shapeWidth

    private val maxHeight = displayAreaHeight - shapeHeight
    private val minHeight = 0 + shapeHeight

    var actionCounter: Int = 0

    var actionList : LinkedHashMap<Int, Shape> = LinkedHashMap()

    var squareNumber: Int = 0
    var circleNumber: Int = 0
    var triangleNumber: Int = 0

    private val random = Random()

    fun getRandomPositionX() : Int{
        return random.nextInt(maxWidth + 1 - minWidth) + minWidth
    }

    fun getRandomPositionY() : Int{
        return random.nextInt(maxHeight + 1 - minHeight) + minHeight
    }

    fun createNewShape(shapeValue: ShapeEnum) : Shape {
        actionCounter++
        val shape = Shape(shapeValue, generateUniqueId(), actionCounter)
        actionList.put(actionCounter, shape)

        incrementShapeNumber(shapeValue)

        return shape
    }


    fun generateUniqueId() : Int {
        val id = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            View.generateViewId()
        } else {
            IdGeneratorHelper.generateViewId()
        }

        return id
    }

    fun setNewShapeToShape(shape: Shape){
        val newShape: ShapeEnum = when(shape.currentShape) {
            ShapeEnum.SQUARE -> ShapeEnum.CIRCLE
            ShapeEnum.CIRCLE -> ShapeEnum.TRIANGLE
            ShapeEnum.TRIANGLE -> ShapeEnum.SQUARE
        }

        incrementShapeNumber(newShape)
        decrementShapeNumber(shape.currentShape)

        actionCounter++
        shape.setNewShape(actionCounter, newShape)
        actionList.put(actionCounter, shape)
    }

    fun undoAction () : Shape? {

        return if (!actionList.isEmpty()){

            val lastModifiedShape = actionList.get(actionCounter)

            if (lastModifiedShape != null) {
                decrementShapeNumber(lastModifiedShape.currentShape)
                lastModifiedShape.undoAction()
                if(!lastModifiedShape.deleted){
                    incrementShapeNumber(lastModifiedShape.currentShape)
                }
                actionList.remove(actionCounter)
                actionCounter--
            }
            lastModifiedShape
        } else {
            actionCounter = 0
            squareNumber = 0
            circleNumber = 0
            triangleNumber = 0
            null
        }

    }

    fun getLastKeyFromActionList(): Int? {
        var lastKey = 0

        return if (!actionList.isEmpty()) {
            for (key: Int in actionList.keys){
                lastKey = key
            }
            lastKey
        }
        else {
            null
        }
    }

    fun deleteShape(shape: Shape){
        shape.deleted = true
        actionCounter++
    }

    private fun incrementShapeNumber(shape: ShapeEnum) {
        when(shape) {
            ShapeEnum.SQUARE -> squareNumber++
            ShapeEnum.CIRCLE -> circleNumber++
            ShapeEnum.TRIANGLE -> triangleNumber++
        }
    }

    private fun decrementShapeNumber(shape: ShapeEnum) {
        when(shape) {
            ShapeEnum.SQUARE -> squareNumber--
            ShapeEnum.CIRCLE -> circleNumber--
            ShapeEnum.TRIANGLE -> triangleNumber--
        }
    }

}