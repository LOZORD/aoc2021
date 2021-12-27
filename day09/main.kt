import java.util.Scanner

data class Position(val row: Int, val col: Int)

fun findLowPoints(grid: Array<IntArray>): Set<Position> {
    val lowPoints = HashSet<Position>()

    fun visit(i: Int, j: Int) {
        if (getNeighbors(grid, i, j).all { grid[i][j] < grid[it.row][it.col] }) {
            lowPoints.add(Position(row=i,col=j))
        }
    }

    for ((i, row) in grid.withIndex()) {
        for ((j, _) in row.withIndex()) {
            visit(i, j)
        }
    }

    return lowPoints
}

fun lavaFloodBasinSize(grid: Array<IntArray>, lowPoint: Position): Int {
    // Share the same instance of the HashMap so that each level of recursion
    // has the same view of visited positions.
    return recurseFlood(grid, lowPoint, hashSetOf())
}

fun recurseFlood(grid: Array<IntArray>, p: Position, visited: HashSet<Position>): Int {
    if (grid[p.row][p.col] == 9) {
        return 0 // Base case: maximum height of basin.
    }
    if (visited.contains(p)) {
        return 0 // Base case: already visited this position.
    }

    visited.add(p)

    return 1 + getNeighbors(grid, p)
        .map { recurseFlood(grid, it, visited) }
        .sum()
}

fun getNeighbors(grid: Array<IntArray>, p: Position): Set<Position> {
    return getNeighbors(grid, p.row, p.col)
}

fun getNeighbors(grid: Array<IntArray>, i: Int, j: Int): Set<Position> {
    val neighbors = HashSet<Position>(4)
    if (i - 1 >= 0) {
        neighbors.add(Position(row=i-1, col=j))
    }
    if (i + 1 < grid.size) {
        neighbors.add(Position(row=i+1, col=j))
    }
    if (j - 1 >= 0) {
        neighbors.add(Position(row=i, col=j-1))
    }
    if (j + 1 < grid[0].size) {
        neighbors.add(Position(row=i, col=j+1))
    }
    return neighbors
}

fun main(args: Array<String>) {
    val rows = ArrayList<IntArray>()
    val input = Scanner(System.`in`)

    while (input.hasNextLine()) {
        val scanned = input.nextLine()
        rows.add(scanned
            .trim()
            .toCharArray()
            .map { it.digitToInt() }
            .toIntArray())
    }

    val grid = rows.toTypedArray()

    val lowPoints = findLowPoints(grid)

    if (args.contains("--part1")) {
        val risk = lowPoints
            .map { grid[it.row][it.col] + 1 }
            .sum()

        println("RISK: $risk")
    
        return
    }

    val product = lowPoints
        .map { lavaFloodBasinSize(grid, it) }
        .sortedDescending()
        .take(3)
        .reduce { acc, n -> acc * n }

    println("PRODUCT: $product")
}