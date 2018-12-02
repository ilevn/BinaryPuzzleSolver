package main

import main.methods.backtrack
import main.methods.solve
import main.methods.solver

fun main(args: Array<String>) {
    print("Select puzzle to solve [examples/very_easy.bp]: ")
    val file = readLine() ?: "examples/very_easy.bp"
    val puzzle = BinaryPuzzle(file)

    val strategy = when (args.getOrNull(0)) {
        "backtrack" -> ::backtrack
        "rules" -> ::solve
        else -> ::solver
    }

    val start = System.currentTimeMillis()
    val solve = strategy(puzzle)
    val end = System.currentTimeMillis()

    println("${solve.toString()}\nTook ${end - start}ms (${(end - start) / 1000}s) ")
}