package paf.peaktest.helpers

import java.util.concurrent.atomic.AtomicInteger

class IdGeneratorHelper {

    companion object {
        private val nextGeneratedId = AtomicInteger(1)
        fun generateViewId(): Int {
            while (true) {
                val result = nextGeneratedId.get()

                var newValue = result + 1
                if (newValue > 0x00FFFFFF) newValue = 1

                if (nextGeneratedId.compareAndSet(result, newValue)) {
                    return result
                }
            }
        }
    }
}