package main

import main.methods.solve

fun main(args: Array<String>) {
    val file = readLine() ?: "examples/very_hard.bp"
    val puzzle = BinaryPuzzle(file)

    val start = System.currentTimeMillis()
    val solve = solve(puzzle)
    val end = System.currentTimeMillis()

    println("${solve.toString()}\nTook ${end - start}ms")
}