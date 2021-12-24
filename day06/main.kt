import java.util.Scanner

fun main() {
    // Use longs since the numbers get really big!
    fun iterateArray(inArr: LongArray): LongArray {
        val outArr = LongArray(9)
        for ((i, n) in inArr.withIndex()) {
            if (i == 0) {
                outArr[6] += n // Reset fish.
                outArr[8] += n // Create that many new fish.
            } else {
                outArr[i-1] += n // Age the fish one day.
            }
        }
        return outArr
    }

    var fish = LongArray(9)
    val input = Scanner(System.`in`)
    input
        .nextLine()
        .split(',')
        .groupingBy { it }
        .eachCount()
        .mapKeys { Integer.parseInt(it.key) }
        .forEach({ fish[it.key] = it.value.toLong() })

    val isPart1 = false // Toggle between the two parts.
    val endDay = if (isPart1) 80 else 256
    for (i in 1..endDay) {
        fish = iterateArray(fish)
    }

    println("COUNT: %d".format(fish.sum()))
}