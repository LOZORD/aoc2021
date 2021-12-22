import java.util.Scanner

fun main() {
    data class Position(val row: Int, val col: Int, var stamped: Boolean)
    class BingoBoard {
        val positionMap = HashMap<Int, Position>(25)
        val rowStamps = IntArray(5, { _ -> 5})
        val colStamps = IntArray(5, { _ -> 5})

        constructor(board: Array<IntArray>) {
            for ((i, row) in board.withIndex()) {
                for ((j, n) in row.withIndex()) {
                    positionMap.set(n, Position(row=i, col=j, stamped=false))
                }
            }
        }

        fun stamp(n: Int) : Int  {
            val p = positionMap.get(n)
            if (p == null) {
                return -1 // Not on the board.
            }
            p.stamped = true
            rowStamps[p.row]--
            colStamps[p.col]--
            if (rowStamps[p.row] == 0 || colStamps[p.col] == 0) {
                return sumUnstamped() // Row Bingo!
            }
            return -1; // No Bingo.
        }

        private fun sumUnstamped(): Int {
            return positionMap.filterValues { !it.stamped }.keys.sum()
        }
 
        override fun toString(): String {
            val grid = Array<IntArray>(5, { _ -> IntArray(5, { _ -> -1 })})
            val s = StringBuilder()
            for ((n, p) in positionMap) {
                grid[p.row][p.col] = n
            }
            for (row in grid) {
                for (n in row) {
                    s.append("$n ".padStart(2))
                }
                s.append("\n")
            }
            return s.toString()
        }
    }

    val input = Scanner(System.`in`)
    val chosenNumbers = ArrayList<Int>()
    val whitespace = Regex("\\s+")
    var minSteps = Int.MAX_VALUE
    var scoreForMinSteps = 0
    while(input.hasNextLine()) {
        if (chosenNumbers.isEmpty()) {
            chosenNumbers.addAll(input.nextLine().split(',').map { s -> Integer.parseInt(s) })
            continue
        }

        // Build up a Bingo grid.
        var grid = Array<IntArray>(5, { _ -> IntArray(0) })
        var gridIndex = 0
        while(input.hasNextLine()) {
            val scanned = input.nextLine()
            println("got scanned line: `$scanned`")
            if (scanned.isBlank()) {
                break;
            }

            val split = scanned.trim().split(whitespace)
            // println("split is `$split` with len %d".format(split.size))
            grid[gridIndex++] = split.map { s -> Integer.parseInt(s) }.toIntArray()
        }

        // println("playing bingo with board:\n$grid")

        // Play Bingo!
        var board = BingoBoard(grid)
        println(board.toString())
        for ((i, n) in chosenNumbers.withIndex()) {
            val sum = board.stamp(n)
            // println("board attempted to stamp $n with return value $sum")
            if (sum < 0) {
                continue;
            }

            if (minSteps > i) {
                minSteps = i
                scoreForMinSteps = sum * n
                break;
            }
        }
    }

    println("SCORE: $scoreForMinSteps")
}