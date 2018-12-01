package main.methods

import main.BinaryPuzzle
import main.helpers.Symbol

data class Offset(val firstRow: Int, val firstCol: Int, val secondRow: Int, val secondCol: Int)

fun ruleThirdOfAKind(bp: BinaryPuzzle): BinaryPuzzle? {
    val offsets = arrayOf(
        // Check for two identical tiles in a row.
        Offset(0, 1, 0, 2),
        Offset(0, -1, 0, -2),
        Offset(1, 0, 2, 0),
        Offset(-1, 0, -2, 0),
        // Check for two with an unset tile inbetween them.
        Offset(0, 1, 0, -1),
        Offset(1, 0, -1, 0)
    )

    for (row in 0 until bp.size) {
        for (col in 0 until bp.size) {
            if (bp.getTile(row, col) != Symbol.Unset) continue
            for (offset in offsets) {
                try {
                    val firstOffset = bp.getTile(row + offset.firstRow, col + offset.firstCol)
                    val secondOffset = bp.getTile(row + offset.secondRow, col + offset.secondCol)

                    // Invert tile if setting an additional identical one would violate the sandwich rule.
                    if (firstOffset != Symbol.Unset && secondOffset != Symbol.Unset && firstOffset == secondOffset) {
                        return bp.setTile(row, col, firstOffset.invert())
                    }
                } catch (_: ArrayIndexOutOfBoundsException) {
                    continue
                }
            }
        }
    }
    return null
}

fun ruleFillRows(bp: BinaryPuzzle): BinaryPuzzle? {
    for (index in 0 until bp.size) {
        for (pair in arrayOf(Pair(index, null), Pair(null, index))) {
            for (value in arrayOf(Symbol.Zero, Symbol.One)) {

                val tile = bp.getTileArray(pair.first, pair.second)

                // Fill rows that have all of our needed tiles for one value but are missing the other.
                if (tile.count { it == value } == bp.size / 2) {
                    if (tile.count { it == value.invert() } < bp.size / 2) {
                        return bp.setTile(pair.first, pair.second, value.invert())
                    }
                }
            }
        }
    }
    return null
}

fun generatePermutations(tiles: Array<Symbol>): List<Array<Symbol>> {
    /* Recursively fills unset tiles with Zeros and Ones */

    val perms = mutableListOf<Array<Symbol>>()

    if (tiles.isEmpty()) {
        perms.add(emptyArray())
    } else {
        if (tiles[0] != Symbol.Unset) {
            for (rec in generatePermutations(tiles.drop(1).toTypedArray()))
                perms.add(arrayOf(tiles[0]) + rec)
        } else {
            for (value in arrayOf(Symbol.Zero, Symbol.One))
                for (rec in generatePermutations(tiles.drop(1).toTypedArray()))
                    perms.add(arrayOf(value) + rec)
        }
    }
    return perms
}

fun Array<Symbol>.containsTriplet(which: String) = which in this.map { it.sym }.joinToString("")

fun ruleFillAnyDuplicates(bp: BinaryPuzzle): BinaryPuzzle? {
    fun resolver(what: String, index: Int): Array<Symbol> {
        return if (what == "row") bp.getTileArray(row = index)
        else bp.getTileArray(col = index)
    }

    var toSet = bp
    for (toCheck in arrayOf("row", "col")) {
        val completedTiles = (0 until bp.size).map { resolver(toCheck, it) }.filter { t ->
            t.all { it != Symbol.Unset }
        }

        for (index in 0 until bp.size) {
            val current = resolver(toCheck, index)
            if (current.all { it != Symbol.Unset }) continue

            // Generate possibilities
            val possibilities = ArrayList<Array<Symbol>>()
            for (option in generatePermutations(current).toTypedArray()) {
                if (
                    option !in completedTiles
                    && option.count { it == Symbol.Zero } == bp.size / 2
                    && option.count { it == Symbol.One } == bp.size / 2
                    // Dirty but it works.
                    && !option.containsTriplet("000") && !option.containsTriplet("111")
                ) {

                    possibilities.add(option)
                }
            }
            if (possibilities.size == 1) {
                // Set new tiles if we only have one possibility.
                for ((secondIndex, value) in possibilities.first().withIndex()) {
                    toSet = if (toCheck == "row") {
                        toSet.setTile(index, secondIndex, value)
                    } else {
                        toSet.setTile(secondIndex, index, value)
                    }
                }
                return toSet
            }
        }
    }
    return null
}


fun solve(bp: BinaryPuzzle): BinaryPuzzle? {
    val rules = arrayOf(::ruleThirdOfAKind, ::ruleFillRows, ::ruleFillAnyDuplicates, ::backtrack)
    var toCheck = bp

    while (true) {
        var updated = false

        if (toCheck.isSolved()) return toCheck

        loop@
        for (rule in rules) {
            println("Currently trying ${rule.name}")
            val currentBp = rule(toCheck)

            if (currentBp != null) {
                toCheck = currentBp
                updated = true
                break@loop
            }
        }
        if (!updated) break
    }
    return toCheck
}



