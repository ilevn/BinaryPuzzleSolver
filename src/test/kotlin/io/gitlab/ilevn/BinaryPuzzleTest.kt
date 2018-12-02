package io.gitlab.ilevn

import io.gitlab.ilevn.helpers.Symbol
import io.gitlab.ilevn.methods.solver
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

internal class BinaryPuzzleTest {
    private val puzzle = BinaryPuzzle("examples/very_easy.bp")

    @Test
    fun getTile() {
        assertEquals(puzzle.getTile(0, 0), Symbol.One)
    }

    @Test
    fun getTileArray() {
        val row = "1..0..".toCharArray().map { Symbol.from(it) }.toTypedArray()
        val col = "1...0.".toCharArray().map { Symbol.from(it) }.toTypedArray()

        assertTrue(puzzle.getTileArray(0, null).contentEquals(row))
        assertTrue(puzzle.getTileArray(null, 0).contentEquals(col))

        assertFailsWith(BinaryPuzzle.InvalidArg::class) { puzzle.getTileArray(null, null) }
    }

    @Test
    fun setTile() {
        assertFailsWith(BinaryPuzzle.InvalidArg::class) { puzzle.setTile(null, null, Symbol.Zero) }
    }

    @Test
    fun isSolved() {
        val toSolveEasy = solver(puzzle)!!.isSolved()
        val alreadySolved = BinaryPuzzle("examples/very_easy_solved.bp")
        assertTrue(toSolveEasy)
        assertTrue(alreadySolved.isSolved())

    }
}