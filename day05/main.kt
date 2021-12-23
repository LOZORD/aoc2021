import java.util.Scanner
import java.util.stream.IntStream
import java.util.stream.Stream
import java.util.TreeMap

fun main() {
    val isPart1 = false // Toggle between the two parts.

    data class Point(val row: Int, val col: Int)

    class Line(val from: Point, val to: Point) {
        fun isDiagonal(): Boolean {
            return from.row != to.row && from.col != to.col
        }

        fun isVeritcal(): Boolean {
            return from.col == to.col
        }

        fun isHorizontal(): Boolean {
            return from.row == to.row
        }

        fun iterator(): Iterator<Point> {
            if (isVeritcal()) {
                return IntStream
                    .rangeClosed(Math.min(from.row, to.row), Math.max(from.row, to.row))
                    .mapToObj({ Point(row=it, col=from.col) })
                    .iterator()
            } else if (isHorizontal()) {
                return IntStream
                    .rangeClosed(Math.min(from.col, to.col), Math.max(from.col, to.col))
                    .mapToObj({ Point(row=from.row, col=it) })
                    .iterator()
            } else {
                check(isDiagonal()) { "expected to be diagonal" }

                // Since we are guaranteed lines with 45 degree angles,
                // we know that the set of points we have to interpolate is
                // +/- 1 in each direction since the non-hypotenuse sides are
                // equal in length.
                // I have a feeling this is the generalized case of the ones above,
                // but we'll keep things as-is for now.
                val dist = Math.abs(from.row - to.row)
                check(dist == Math.abs(from.col - to.col)) {
                    "expected points to have equal component distances"
                }
                val rowSign = if (from.row < to.row) 1 else -1
                val colSign = if (from.col < to.col) 1 else -1
                return IntStream
                    .rangeClosed(0, dist)
                    .mapToObj({
                        Point(
                            row=from.row + (rowSign * it),
                            col=from.col + (colSign * it),
                        )
                    }).iterator()

            }
        }
    }

    val linePattern = Regex("(?<r1>\\d+),(?<c1>\\d+) -> (?<r2>\\d+),(?<c2>\\d+)").toPattern()
    fun parseLine(s: String): Line {
        val matcher = linePattern.matcher(s)
        check(matcher.matches()) { "expected $s to match $linePattern" }
        return Line(
            from=Point(
                row=matcher.group("r1").toInt(),
                col=matcher.group("c1").toInt()
            ),
            to=Point(
                row=matcher.group("r2").toInt(),
                col=matcher.group("c2").toInt()
            ),
        )
    }

    val input = Scanner(System.`in`)
    val visited = HashMap<Point, Int>()
    while(input.hasNextLine()) {
        val scanned = input.nextLine()
        val line = parseLine(scanned)

        if (isPart1 and line.isDiagonal()) {
            continue
        }

        for (point in line.iterator()) {
            visited.merge(point, 1) { oldValue, _ -> oldValue + 1}
        }
    }

    print("POINTS_WITH_OVERLAP: %d".format(
        visited.filterValues { it > 1 }.size
    ))
}