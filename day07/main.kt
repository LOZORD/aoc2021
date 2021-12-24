import java.util.Scanner
fun main(args: Array<String>) {
    val initialPositions = Scanner(System.`in`)
        .nextLine()
        .split(',')
        .map { Integer.parseInt(it) }
        .toIntArray()

    check(initialPositions.isNotEmpty())

    if (args.contains("--part1")) {
        val median = initialPositions
            .sorted()
            .get(initialPositions.size / 2)

        val fuel = initialPositions
            .map { Math.abs(it - median) }
            .sum()

        println("FUEL: $fuel; POSITION: $median")

        return
    }

    val mean = Math.floor(initialPositions.sum().toDouble() / initialPositions.size.toDouble()).toInt()

    val fuel = initialPositions
        .map { Math.abs(it - mean) }    // Distance to the mean.
        .map { (it * (it+1)) / 2 }      // Gauss sum formula for 1->it for new fuel rule.
        .sum()
    
    println("FUEL: $fuel; POSITION: $mean")
}