package paf.peaktest.data

import java.io.Serializable

class ShapeHolder(var currentShape: ShapeEnum?,
                  var id: Int,
                  var actionNumber: Int,
                  var posX: Int,
                  var posY: Int) : Serializable {

    var actionList: LinkedHashMap<Int, ShapeEnum?> = LinkedHashMap()
    var deleted = false

    init {
        actionList.put(actionNumber, currentShape!!)
    }

    fun setNewShape(actionNumber: Int, newShape: ShapeEnum?) {
        actionList.put(actionNumber, newShape)
        this.actionNumber = actionNumber
        this.currentShape = newShape
    }

    fun undoAction(){

        if(actionList.size == 1) {
            actionList.remove(actionNumber)
            deleted = true
            actionNumber = 0
        }
        else if(actionNumber > 1 && !actionList.isEmpty()) {
            actionList.remove(actionNumber)
            actionNumber = getLastKeyFromActionList()
            currentShape = actionList.get(actionNumber)!!
        } else {
            actionNumber = 0
        }
    }

    fun getLastKeyFromActionList(): Int {
        var lastKey = 0

        if (!actionList.isEmpty()) {
            for (key: Int in actionList.keys){
                lastKey = key
            }
            return lastKey
        }
        else {
            return -1
        }
    }

    fun delete(actionNumber: Int) {
        this.actionNumber = actionNumber
        setNewShape(actionNumber, null)
    }

//    fun restoreShape(){
//        deleted = false
//    }

}