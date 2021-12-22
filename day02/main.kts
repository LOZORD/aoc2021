import java.util.Scanner

fun main1() {
    var horiz = 0
    var depth = 0
    val input = Scanner(System.`in`)

    while (input.hasNextLine()) {
        val line = input.nextLine()
        val split = line.split(' ')
        var dir = split[0]
        var amt = split[1].toInt()

        when (dir) {
            "forward" -> horiz += amt
            "up" -> depth -= amt
            "down" -> depth += amt
            else -> throw AssertionError("bad direction: $dir")
        }
    }

    val multp = horiz * depth

    println("HORIZ: $horiz; DEPTH: $depth; MULTP: $multp")
}

fun main2() {
    var horiz = 0
    var depth = 0
    var aim   = 0
    val input = Scanner(System.`in`)

     while (input.hasNextLine()) {
        val line = input.nextLine()
        val split = line.split(' ')
        var dir = split[0]
        var amt = split[1].toInt()

        when (dir) {
            "forward" -> {
                horiz += amt
                depth += (aim * amt)
            }
            "up" -> aim -= amt
            "down" -> aim += amt
            else -> throw AssertionError("bad direction: $dir")
        }
    }

    val multp = horiz * depth

    println("HORIZ: $horiz; DEPTH: $depth; MULTP: $multp")
}