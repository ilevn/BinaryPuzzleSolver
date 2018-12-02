package io.gitlab.ilevn.methods

import io.gitlab.ilevn.BinaryPuzzle
import io.gitlab.ilevn.helpers.Symbol

import java.util.*

fun backtrack(bp: BinaryPuzzle): BinaryPuzzle? {
    val queue: Queue<BinaryPuzzle> = LinkedList()
    queue.add(bp)

    loop@ while (queue.iterator().hasNext()) {
        val newBp = queue.poll()

        when {
            newBp.isSolved() -> return newBp
            !newBp.isValid() -> continue@loop
        }

        fun queueUp() {
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