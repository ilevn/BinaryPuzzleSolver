package main.helpers

enum class Symbol(val sym: Char) {
    One('1'),
    Zero('0'),
    Unset('.');

    companion object {
        fun from(c: Char): Symbol {
            return values().first { v -> v.sym == c }
        }
    }

    fun invert(): Symbol {
        if (this == One) {
            return Zero
        }
        return One
    }

    fun map(other: Symbol?): Symbol {
        // If the tile is unset, return the other one, otherwise return this one.
        // By providing a null Symbol we signify that we don't have a parent, so
        // in that case just return this (Unset) anyway.
        if (this == Unset) {
            return other ?: this
        }

        return this
    }
}