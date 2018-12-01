package main.methods

import main.BinaryPuzzle
import main.helpers.Symbol

import java.util.*

fun backtrack(bp: BinaryPuzzle): BinaryPuzzle? {
    val queue: Queue<BinaryPuzzle> = LinkedList()
    queue.add(bp)

    loop@ while (queue.iterator().hasNext()) {
        val newBp = queue.poll()

        when {
            newBp.isSolved() -> {
                println("Solved BinaryPuzzle!")
                return newBp
            }
            !newBp.isValid() -> continue@loop
        }

        println("Now testing")
        println(newBp.toString())

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