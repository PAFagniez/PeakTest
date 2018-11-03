package paf.peaktest.data

import java.io.Serializable
import java.util.*

class ShapeHolder(var currentShape: ShapeEnum?,
                  var id: Int,
                  var actionNumber: Int,
                  var posX: Int,
                  var posY: Int) : Serializable {

    var actionList: TreeMap<Int, ShapeEnum?> = TreeMap()

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
            actionNumber = 0
        }
        else if(actionNumber > 1 && !actionList.isEmpty()) {
            actionList.remove(actionNumber)
            actionNumber = actionList.lastKey()
            currentShape = actionList.get(actionNumber)!!
        } else {
            actionNumber = 0
        }
    }

    fun delete(actionNumber: Int) {
        setNewShape(actionNumber, null)
    }

}