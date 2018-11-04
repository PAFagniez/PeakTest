package paf.peaktest.controllers

import android.os.Build
import android.view.View
import paf.peaktest.data.ShapeEnum
import paf.peaktest.data.ShapeHolder
import paf.peaktest.helpers.IdGeneratorHelper
import java.io.Serializable
import java.util.*

class Controller(displayAreaWidth: Int, displayAreaHeight: Int, shapeWidth: Int, shapeHeight: Int) : Serializable {

    private val maxWidth = displayAreaWidth - shapeWidth
    private val minWidth = 0 + shapeWidth

    private val maxHeight = displayAreaHeight - shapeHeight
    private val minHeight = 0 + shapeHeight

    var actionNumber: Int = 0

    var actionList : TreeMap<Int, TreeMap<Int, ShapeHolder>> = TreeMap()
    private var squareHolderList : TreeMap<Int, ShapeHolder> = TreeMap()
    private var circleHolderList : TreeMap<Int, ShapeHolder> = TreeMap()
    private var triangleHolderList : TreeMap<Int, ShapeHolder> = TreeMap()
    private var deletedHolderList : TreeMap<Int, ShapeHolder> = TreeMap()

    private var squareNumber: Int = 0
    private var circleNumber: Int = 0
    private var triangleNumber: Int = 0

    private val random = Random()

    private fun getRandomPositionX() : Int{
        return random.nextInt(maxWidth + 1 - minWidth) + minWidth
    }

    private fun getRandomPositionY() : Int{
        return random.nextInt(maxHeight + 1 - minHeight) + minHeight
    }

    fun createNewShapeHolder(shape: ShapeEnum) : ShapeHolder {
        actionNumber++
        val shapeHolder = ShapeHolder(shape, generateUniqueId(), actionNumber, getRandomPositionX(), getRandomPositionY())
        addNewShapeHolderToListAndUpdateActionList(shapeHolder)
        return shapeHolder
    }


    private fun generateUniqueId() : Int {

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            View.generateViewId()
        } else {
            IdGeneratorHelper.generateViewId()
        }
    }

    fun setNewShapeToShapeHolder(shapeHolder: ShapeHolder){
        val currentShape = shapeHolder.currentShape
        val newShape: ShapeEnum? = when(currentShape) {
            ShapeEnum.SQUARE -> ShapeEnum.CIRCLE
            ShapeEnum.CIRCLE -> ShapeEnum.TRIANGLE
            ShapeEnum.TRIANGLE -> ShapeEnum.SQUARE
            else -> null
        }

        val shapeHolderList = getShapeHolderList(currentShape)
        val updatedShapeHolder = shapeHolderList[shapeHolder.actionNumber]
        removeShapeHolderFromList(shapeHolder, shapeHolder.actionNumber)

        actionNumber++
        updatedShapeHolder!!.setNewShape(actionNumber, newShape)
        addNewShapeHolderToListAndUpdateActionList(updatedShapeHolder)
    }

    fun undoAction () : ShapeHolder? {

        if (!actionList.isEmpty() || actionNumber != 0){
            val lastModifiedList = actionList[actionNumber]

            if (lastModifiedList != null && !lastModifiedList.isEmpty()) {
                val lastModifiedShapeHolder = lastModifiedList[actionNumber]

                if(lastModifiedShapeHolder != null){
                    removeShapeHolderFromList(lastModifiedShapeHolder, lastModifiedShapeHolder.actionNumber)

                    lastModifiedShapeHolder.undoAction()
                    addNewShapeHolderToListAndUpdateActionList(lastModifiedShapeHolder)
                }

                actionNumber--
                return lastModifiedShapeHolder
            }
            return null
        } else {
            actionNumber = 0
            squareNumber = 0
            circleNumber = 0
            triangleNumber = 0
            return null
        }
    }

    private fun getPreviousKeyFromShapeHolderList(linkedHashMap: TreeMap<Int, ShapeHolder>): Int? {
        var lastKey = 0

        for ((i, key: Int) in linkedHashMap.keys.withIndex()){
                if(i == linkedHashMap.size-2) lastKey = key
            }
        return lastKey
    }

    fun deleteShapeHolder(shapeHolder: ShapeHolder){
        val shapeHolderList = getShapeHolderList(shapeHolder.currentShape)
        val updatedShapeHolder = shapeHolderList[shapeHolder.actionNumber]
        removeShapeHolderFromList(shapeHolder, shapeHolder.actionNumber)

        actionNumber++
        updatedShapeHolder!!.delete(actionNumber)
        addNewShapeHolderToListAndUpdateActionList(updatedShapeHolder)
    }

    private fun addNewShapeHolderToListAndUpdateActionList(shapeHolder: ShapeHolder) {
        val shape = shapeHolder.currentShape
        val shapeHolderList = getShapeHolderList(shape)

        if(!shapeHolder.actionList.isEmpty()){
            shapeHolderList[shapeHolder.actionNumber] = shapeHolder
        }
        else {
            shapeHolderList.remove(shapeHolder.actionNumber)
        }
        updateActionList(shapeHolder)
    }

    private fun removeShapeHolderFromList(shapeHolder: ShapeHolder, actionNumber: Int) {
        val shape = shapeHolder.currentShape
        val shapeHolderList = getShapeHolderList(shape)

        shapeHolderList.remove(actionNumber)
        updateActionList(shapeHolder)
    }

    private fun updateActionList(shapeHolder: ShapeHolder){
        val shapeHolderList = getShapeHolderList(shapeHolder.currentShape)

        if(shapeHolderList.size > 1 && !actionList.isEmpty()){
            val lastKeyOfList = getPreviousKeyFromShapeHolderList(shapeHolderList)
            actionList.remove(lastKeyOfList)
        }

        actionList.remove(shapeHolder.actionNumber)
        if (!shapeHolderList.isEmpty()){
            actionList[shapeHolderList.lastKey()] = shapeHolderList
        }
    }

    private fun getShapeHolderList(shape: ShapeEnum?) : TreeMap<Int, ShapeHolder> {
        return when(shape) {
            ShapeEnum.SQUARE -> squareHolderList
            ShapeEnum.CIRCLE -> circleHolderList
            ShapeEnum.TRIANGLE -> triangleHolderList
            null -> deletedHolderList
        }
    }

    fun deleteShapeGroup(shape: ShapeEnum) {
        val shapeHolderList = getShapeHolderList(shape)

        while (shapeHolderList.isNotEmpty())
            deleteShapeHolder(shapeHolderList.lastEntry().value)
    }

    fun getSquareNumber(): Int{
        return squareHolderList.size
    }

    fun getCircleNumber(): Int{
        return circleHolderList.size
    }

    fun getTriangleNumber(): Int{
        return triangleHolderList.size
    }

}