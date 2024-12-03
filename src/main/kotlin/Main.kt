package org.example

import java.io.BufferedReader
import java.io.File
import kotlin.math.absoluteValue

fun main() {
    val day03Input: String = File("day3.txt").readDay03()
    //solveDay03Task01(day03Input)
    solveDay03Task02(day03Input)

    //val day02Input: List<Day02Report> = File("day2.txt").readDay02()
    //solveDay02Task01(day02Input)
    //solveDay02Task02(day02Input)

    //val day01Input: Day01Input = File("day1.txt").readDay01()
    //solveDay01Task01(day01Input)
    //solveDay01Task02(day01Input)
}

fun File.readDay03(): String {
    val result: String? = BufferedReader(this.reader()).readLine()
    return result!!
}

fun solveDay03Task01(input: String) {
    val mulRegex = Regex("mul\\((?<val1>\\d{1,3}),(?<val2>\\d{1,3})\\)")
    val result = mulRegex.findAll(input).map {
        it.groups["val1"]?.value!!.toInt() *
                it.groups["val2"]?.value!!.toInt()
    }.sum()

    println(result)
}

fun solveDay03Task02(input: String) {
    val mulRegex = Regex("mul\\((?<val1>\\d{1,3}),(?<val2>\\d{1,3})\\)")
    val doRegex = Regex("do\\(\\)")
    val dontRegex = Regex("don't\\(\\)")

    // state
    var mulEnabled = true
    var total = 0

    var i: Int = 0
    while (true) {
        val nextDo = doRegex.find(input, i)
        val nextDont = dontRegex.find(input, i)
        val nextMul = mulRegex.find(input, i)

        if (nextDo != null
            && (nextDont == null || nextDo.range.first < nextDont.range.first)
            && (nextMul == null || nextDo.range.first < nextMul.range.first)
        ) {
            //println("found next do")
            mulEnabled = true
            i = nextDo.range.last + 1
        } else if (nextDont != null
            && (nextDo == null || nextDont.range.first < nextDo.range.first)
            && (nextMul == null || nextDont.range.first < nextMul.range.first)
        ) {
            //println("found next dont")
            mulEnabled = false
            i = nextDont.range.last + 1
        } else if (nextMul != null
            && (nextDo == null || nextMul.range.first < nextDo.range.first)
            && (nextDont == null || nextMul.range.first < nextDont.range.first)
        ) {
            //println("found next mul")
            if (mulEnabled) {
                val result: Int =
                    nextMul.groups["val1"]?.value!!.toInt() *
                            nextMul.groups["val2"]?.value!!.toInt()
                total += result
            }
            i = nextMul.range.last + 1
        } else {
            break
        }
    }

    println(total)
}

@JvmInline
value class Day02Report(internal val l: List<Int>)

fun File.readDay02(): List<Day02Report> {
    val result: MutableList<Day02Report> = mutableListOf()
    BufferedReader(this.reader()).forEachLine { it ->
        val values: List<Int> = it.split(" ").map(String::toInt)
        result.add(Day02Report(values))
    }
    return result
}

fun Day02Report.isSafe(): Boolean {
    val isAscending = this.l.windowed(2).all { (first, second) ->
        first - second <= 0
    }
    val isDescending = this.l.windowed(2).all { (first, second) ->
        first - second >= 0
    }
    if (!isAscending && !isDescending) {
        return false
    }
    val checkDistances = this.l.windowed(2).all { (first, second) ->
        val difference = (first - second).absoluteValue
        difference in 1..3
    }

    return checkDistances
}

fun Day02Report.combinationsByRemovingOne(): List<Day02Report> {
    return this.l.indices.map { index ->
        Day02Report(this.l.toMutableList().apply { removeAt(index) })
    }
}

fun solveDay02Task01(input: List<Day02Report>) {
    println(input.count(Day02Report::isSafe))
}

fun solveDay02Task02(input: List<Day02Report>) {
    println(input.count() {
        it.combinationsByRemovingOne().any(Day02Report::isSafe)
    })
}

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

fun solveDay01Task01(input: Day01Input) {
    val listA: MutableList<Int> = input.listA.toMutableList()
    val listB: MutableList<Int> = input.listB.toMutableList()
    listA.sort()
    listB.sort()

    val result: Int = listA.zip(listB).sumOf { (a, b) -> (a - b).absoluteValue }
    println(result)
}

fun solveDay01Task02(input: Day01Input) {
    val listBCount: Map<Int, Int> = input.listB.groupingBy { it }.eachCount()
    val result: Int = input.listA.sumOf { it * listBCount.getOrDefault(it, 0) }
    println(result)
}
