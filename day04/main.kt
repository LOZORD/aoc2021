import java.util.Scanner

fun main() {
    val isPart1 = false // true => Part 1; false => Part 2

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

        /** Attempts to stamp the spot with number n (if it's on the board).
         * If stamping results in a win, the score of the board of is returned.
         * Otherwise -1 is returned.
         * 
         * The score is n * the sum of unstamped numbers.
         */
        fun stamp(n: Int) : Int  {
            val p = positionMap.get(n)
            if (p == null) {
                return -1 // Not on the board.
            }
            p.stamped = true
            rowStamps[p.row]--
            colStamps[p.col]--
            if (rowStamps[p.row] == 0 || colStamps[p.col] == 0) {
                // Bingo!
                return n * positionMap.filterValues { !it.stamped }.keys.sum()
            }
            return -1 // No Bingo.
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

    var optimalSteps = if (isPart1) Int.MAX_VALUE else Int.MIN_VALUE
    var optimalScore = -1

    while(input.hasNextLine()) {
        if (chosenNumbers.isEmpty()) {
            chosenNumbers.addAll(input.nextLine().split(',').map { s -> Integer.parseInt(s) })
            continue
        }

        // Build up a Bingo grid.
        var grid = Array<IntArray>(5, { _ -> IntArray(5) })
        var gridIndex = 0
        while(input.hasNextLine()) {
            val scanned = input.nextLine()
            if (scanned.isBlank()) {
                break;
            }

            val split = scanned.trim().split(whitespace)
            grid[gridIndex++] = split.map { s -> Integer.parseInt(s) }.toIntArray()
        }

        // Play Bingo!
        var board = BingoBoard(grid)
        for ((i, n) in chosenNumbers.withIndex()) {
            val score = board.stamp(n)
            if (score < 0) {
                // No Bingo :(
                continue
            }

            // Bingo!
            val shouldUpdate = (isPart1 && optimalSteps > i)
                || (!isPart1 && optimalSteps < i)

            if (shouldUpdate) {
                optimalSteps = i
                optimalScore = score
            }
            
            // On to the next board.
            break
        }
    }

    println("SCORE: $optimalScore")
}