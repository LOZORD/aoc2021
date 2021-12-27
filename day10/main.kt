import java.util.Scanner

enum class LineStatus {
    VALID,
    INCOMPLETE,
    CORRUPTED
}

data class StatusWithData(
    val status: LineStatus,
    val corruption: Char? = null,
    val remaining: ArrayDeque<Char>? = null,
)

fun main(args: Array<String>) {
    val input = Scanner(System.`in`)
    val statuses = ArrayList<StatusWithData>()
    while (input.hasNextLine()) {
        val scanned = input.nextLine()
        statuses.add(classifyLine(scanned.trim()))
    }

    if (args.contains("--part1")) {
        val score = statuses
            .filter { it.status == LineStatus.CORRUPTED }
            .map { scoreCorruptedChar(it.corruption!!) }
            .sum()

        println("SCORE: $score")
        return
    }

    val scores = statuses
        .filter { it.status == LineStatus.INCOMPLETE }
        .map { scoreIncompleteChars(it.remaining!!) }

    val median = scores.sorted().get(scores.size / 2)

    println("MEDIAN_SCORE: $median")
}

fun classifyLine(line: String): StatusWithData {
    val stack = ArrayDeque<Char>()

    for (char in line.toCharArray()) {
        when (char) {
            '(', '[', '{', '<' -> stack.addLast(char)
            ')', ']', '}', '>' -> {
                if (stack.isEmpty()) {
                    return StatusWithData(LineStatus.CORRUPTED, char)
                }
                val popped = stack.removeLast()
                val opener = getOpener(char)
                if (popped != opener) {
                    return StatusWithData(LineStatus.CORRUPTED, char)
                }
            }
            else -> throw IllegalArgumentException("character $char is not valid")
        }
    }

    if (stack.isNotEmpty()) {
        return StatusWithData(LineStatus.INCOMPLETE, remaining=stack)
    }

    return StatusWithData(LineStatus.VALID)
}

fun getOpener(c: Char): Char? {
    when (c) {
        ')' -> return '('
        ']' -> return '['
        '}' -> return '{'
        '>' -> return '<'
        else -> return null
    }
}

fun getCloser(c: Char): Char? {
    when (c) {
        '(' -> return ')'
        '[' -> return ']'
        '{' -> return '}'
        '<' -> return '>'
        else -> return null
    }
}

fun scoreCorruptedChar(c: Char): Int {
    when (c) {
        ')' -> return 3
        ']' -> return 57
        '}' -> return 1197
        '>' -> return 25137
        else -> throw IllegalArgumentException("can't score character $c")
    }
}

fun scoreIncompleteChars(remaining: ArrayDeque<Char>): Long {
    fun score(c: Char): Long {
        val closer = getCloser(c)
        when (closer) {
            ')' -> return 1
            ']' -> return 2
            '}' -> return 3
            '>' -> return 4
            else -> throw IllegalArgumentException("can't score closer char $c")
        }
    }

    var acc = 0L
    for (c in remaining.reversed()) {
        acc = (acc * 5L) + score(c) 
    }
    return acc
}