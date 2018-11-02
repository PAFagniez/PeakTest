package paf.peaktest.controllers

import android.os.Build
import android.view.View
import paf.peaktest.data.ShapeEnum
import paf.peaktest.data.ShapeHolder
import paf.peaktest.helpers.IdGeneratorHelper
import java.io.Serializable
import java.util.*
import kotlin.collections.LinkedHashMap

class Controller(displayAreaWidth: Int, displayAreaHeight: Int, shapeWidth: Int, shapeHeight: Int) : Serializable {

    private val maxWidth = displayAreaWidth - shapeWidth
    private val minWidth = 0 + shapeWidth

    private val maxHeight = displayAreaHeight - shapeHeight
    private val minHeight = 0 + shapeHeight

    var actionNumber: Int = 0

    var actionList : LinkedHashMap<Int, ShapeHolder> = LinkedHashMap()
    var shapeHoldersToDisplayList : LinkedHashMap<Int, ShapeHolder> = LinkedHashMap()

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

    fun createNewShapeHolder(shape: ShapeEnum) : ShapeHolder {
        actionNumber++
        val shapeHolder = ShapeHolder(shape, generateUniqueId(), actionNumber)
        actionList.put(actionNumber, shapeHolder)
        shapeHoldersToDisplayList.put(actionNumber, shapeHolder)
        incrementShapeNumber(shape)

        return shapeHolder
    }


    private fun generateUniqueId() : Int {
        val id = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            View.generateViewId()
        } else {
            IdGeneratorHelper.generateViewId()
        }

        return id
    }

    fun setNewShapeToShapeHolder(shapeHolder: ShapeHolder){
        val currentShape = shapeHolder.currentShape
        val newShape: ShapeEnum? = when(currentShape) {
            ShapeEnum.SQUARE -> ShapeEnum.CIRCLE
            ShapeEnum.CIRCLE -> ShapeEnum.TRIANGLE
            ShapeEnum.TRIANGLE -> ShapeEnum.SQUARE
            else -> {
                null
            }
        }

        if(newShape != null) incrementShapeNumber(newShape)
        if(currentShape != null) decrementShapeNumber(currentShape)

        actionNumber++
        shapeHolder.setNewShape(actionNumber, newShape)
        actionList[actionNumber] = shapeHolder
    }

    fun undoAction () : ShapeHolder? {

        return if (!actionList.isEmpty() || actionNumber != 0){

            val lastModifiedShapeHolder = actionList[actionNumber]

            if (lastModifiedShapeHolder != null) {
                val currentShape = lastModifiedShapeHolder.currentShape
                currentShape?.let { decrementShapeNumber(it) }

                lastModifiedShapeHolder.undoAction()

                if(!lastModifiedShapeHolder.actionList.isEmpty()) {
                    lastModifiedShapeHolder.currentShape?.let { incrementShapeNumber(it) }
                }

                actionList.remove(actionNumber)
                actionNumber--
            }
            lastModifiedShapeHolder
        } else {
            actionNumber = 0
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

    fun deleteShapeHolder(shapeHolder: ShapeHolder){

        shapeHolder.currentShape?.let {
            decrementShapeNumber(it)
            actionNumber++
            shapeHolder.delete(actionNumber)
            actionList[actionNumber] = shapeHolder
        }
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