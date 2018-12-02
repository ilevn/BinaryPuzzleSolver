package main

import main.helpers.Symbol
import java.io.File
import java.io.FileNotFoundException
import kotlin.system.exitProcess


class BinaryPuzzle(filePath: String?) {
    var size: Int = 0
    private var board: Array<Array<Symbol>> = emptyArray()
    private var lines: List<String> = emptyList()
    private var parent: BinaryPuzzle? = null

    constructor(parent: BinaryPuzzle) : this(filePath = null) {
        // Allow for deep copies of parent nodes.
        this.parent = parent
        this.board = parent.board.deepCopy()
        this.size = parent.size
    }

    private fun Array<Array<Symbol>>.deepCopy() = Array(size) { get(it).clone() }

    init {
        if (filePath != null) {
            try {
                lines = File(filePath).readLines()
            } catch (e: FileNotFoundException) {
                println("Could not find puzzle file.")
                exitProcess(1)
            }

            board = Array(lines.size) { Array(lines.size) { Symbol.Unset } }

            lines.forEachIndexed { row, s ->
                s.forEachIndexed { col, c ->
                    board[row][col] = Symbol.from(c)
                }
                size = lines.size

            }
        }
    }


    override fun toString() = board.joinToString("\n") {
        it.toList().map { c -> c.sym }.joinToString(" ")
    }

    fun getTile(row: Int, col: Int): Symbol {
        return board[row][col].map(parent?.getTile(row, col))
    }

    class InvalidArg(message: String) : Exception(message)

    fun getTileArray(row: Int? = null, col: Int? = null): Array<Symbol> {
        return when {
            row != null -> (0 until size).map { getTile(row, it) }.toTypedArray()
            col != null -> (0 until size).map { getTile(it, col) }.toTypedArray()
            else -> throw InvalidArg("Could not get tile.")
        }
    }

    fun setTile(row: Int? = null, col: Int? = null, value: Symbol): BinaryPuzzle {
        // Duplicate parent node.
        val child = BinaryPuzzle(this)

        when {
            row != null && col != null -> child.board[row][col] = value
            row != null -> for (c in 0 until size) {
                if (child.getTile(row, c) == Symbol.Unset)
                    child.board[row][c] = value
            }
            col != null -> for (r in 0 until size) {
                if (child.getTile(r, col) == Symbol.Unset)
                    child.board[r][col] = value
            }
            else -> throw InvalidArg("Provide at least one tile.")
        }
        return child
    }


    private fun isFilledCompletely(): Boolean {
        for (row in 0 until size) {
            for (col in 0 until size) {
                if (getTile(row, col) == Symbol.Unset) {
                    return false
                }
            }
        }
        return true
    }

    private fun inBounds(check: Int) = board.size - 1 < check && check > 0

    private fun rowEqual(row: Int, col: Int): Boolean {
        if (!inBounds(row)) return false

        val offsetEqual = getTile(row - 1, col) == getTile(row, col)
        val trueEqual = getTile(row, col) == getTile(row + 1, col)
        return offsetEqual && trueEqual
    }

    private fun colEqual(row: Int, col: Int): Boolean {
        if (!inBounds(col)) return false

        val offsetEqual = getTile(row, col - 1) == getTile(row, col)
        val trueEqual = getTile(row, col) == getTile(row, col + 1)
        return offsetEqual && trueEqual
    }

    fun isValid(): Boolean {
        for (row in 0 until size) {
            loop@ for (col in 0 until size) {
                when {
                    getTile(row, col) == Symbol.Unset -> continue@loop
                    rowEqual(row, col) -> return false
                    colEqual(row, col) -> return false
                }
            }
        }
        for (i in 0 until size) {
            when {
                getTileArray(row = i).count { it == Symbol.Zero } > size / 2
                        || getTileArray(row = i).count { it == Symbol.One } > size / 2
                        || getTileArray(col = i).count { it == Symbol.Zero } > size / 2
                        || getTileArray(col = i).count { it == Symbol.One } > size / 2 -> return false

            }
        }
        for (i in 0 until size) {
            for (j in 0..i) {
                if (getTileArray(row = i).distinct().size == 1
                    && getTileArray(col = i).distinct().size == 1
                    && (getTileArray(row = i).contentEquals(getTileArray(row = j))
                            || getTileArray(col = i).contentEquals(getTileArray(col = j)))
                )
                    return false

            }
        }
        return true
    }

    fun isSolved(): Boolean = isFilledCompletely() && isValid()
}
