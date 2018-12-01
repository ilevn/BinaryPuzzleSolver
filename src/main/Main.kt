package main

import main.methods.solve

fun main(args: Array<String>) {
    val puzzle = BinaryPuzzle("puzzles/very-hard/001.takuzu")

    val start = System.currentTimeMillis()
    val solve = solve(puzzle)
    val end = System.currentTimeMillis()

    println("${solve.toString()}\nTook ${end - start}ms")
}