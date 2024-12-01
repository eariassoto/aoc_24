package org.example

import java.io.BufferedReader
import java.io.File
import kotlin.math.absoluteValue

data class Day01Input(val listA: List<Int>, val listB: List<Int>)

fun File.readDay01(): Day01Input {
    val listA: MutableList<Int> = mutableListOf()
    val listB: MutableList<Int> = mutableListOf()
    BufferedReader(this.reader()).forEachLine { it ->
        val values: List<Int> = it.split("   ").map(String::toInt)
        listA.add(values.first())
        listB.add(values.last())
    }
   return Day01Input(listA, listB)
}

fun solveDay01Task01(input: Day01Input)
{
    val listA: MutableList<Int> = input.listA.toMutableList()
    val listB: MutableList<Int> = input.listB.toMutableList()
    listA.sort()
    listB.sort()

    val result: Int = listA.zip(listB).sumOf { (a, b) -> (a - b).absoluteValue }
    println(result)
}

fun solveDay01Task02(input: Day01Input)
{
    val listBCount: Map<Int, Int> = input.listB.groupingBy { it }.eachCount()
    val result: Int = input.listA.sumOf { it * listBCount.getOrDefault(it, 0) }
    println(result)
}

fun main() {
    val day01Input: Day01Input = File("day1.txt").readDay01()
    solveDay01Task01(day01Input)
    solveDay01Task02(day01Input)
}