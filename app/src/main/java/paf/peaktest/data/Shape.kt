package paf.peaktest.data

import java.io.Serializable

class Shape(var currentShape: ShapeEnum,
            var id: Int,
            var actionNumber: Int) : Serializable {

    var actionChangingShapeList: LinkedHashMap<Int, ShapeEnum> = LinkedHashMap()
    var deleted = false

    init {
        actionChangingShapeList.put(actionNumber, currentShape)
    }

    fun setNewShape(actionNumber: Int, newShape: ShapeEnum) {
        actionChangingShapeList.put(actionNumber, newShape)
        this.actionNumber = actionNumber
        this.currentShape = newShape
    }

    fun undoAction(){

        if(actionChangingShapeList.size == 1) {
            actionChangingShapeList.remove(actionNumber)
            deleted = true
            actionNumber = 0
        }
        else if(actionNumber > 1 && !actionChangingShapeList.isEmpty()) {
            actionChangingShapeList.remove(actionNumber)
            actionNumber = getLastKeyFromActionList()
            currentShape = actionChangingShapeList.get(actionNumber)!!
        } else {
            actionNumber = 0
        }
    }

    fun getLastKeyFromActionList(): Int {
        var lastKey = 0

        if (!actionChangingShapeList.isEmpty()) {
            for (key: Int in actionChangingShapeList.keys){
                lastKey = key
            }
            return lastKey
        }
        else {
            return -1
        }
    }
}