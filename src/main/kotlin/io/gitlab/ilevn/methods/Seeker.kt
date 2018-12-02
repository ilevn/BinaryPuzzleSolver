package io.gitlab.ilevn.methods

import io.gitlab.ilevn.BinaryPuzzle
import io.gitlab.ilevn.helpers.Symbol
import java.util.*


fun solver(bp: BinaryPuzzle): BinaryPuzzle? {
    rules.remove(::backtrack)

    val queue: Queue<BinaryPuzzle> = LinkedList()
    queue.add(bp)

    loop@ while (queue.iterator().hasNext()) {
        var newBp = queue.poll()

        when {
            newBp.isSolved() -> return newBp
            !newBp.isValid() -> continue@loop
        }

        while (true) {
            var updated = false

            for_@
            for (rule in rules) {
                //println("Currently trying ${rule.name}")
                val currentBp = rule(newBp)

                if (currentBp != null) {
                    newBp = currentBp
                    updated = true
                    break@for_
                }
            }
            if (!updated) break
        }
        if (newBp.isSolved()) return newBp

        fun queueUp() {
            //println("Guessing one")
            for (row in 0 until newBp.size) {
                for (col in 0 until newBp.size) {
                    if (newBp.getTile(row, col) == Symbol.Unset) {
                        for (value in arrayOf(Symbol.Zero, Symbol.One)) {
                            queue.add(newBp.setTile(row, col, value))
                        }
                        return
                    }
                }
            }
        }
        queueUp()
    }
    return null
}