import java.util.Scanner

fun main1() {
    val input = Scanner(System.`in`)
    var count = 0
    var prev = input.nextInt()
    var next: Int

    while (input.hasNextInt()) {
        next = input.nextInt()
        if (next > prev) {
            count++
        }
        prev = next
    }

    println("COUNT: $count")
}

fun main2() {
    var input = Scanner(System.`in`)
    var count = 0
    // buffer acts as a 3-element ring buffer.
    var buffer = ArrayDeque<Int>(3)
    while (input.hasNextInt()) {
        val scanned = input.nextInt()
        // If our buffer isn't full yet, add this element.
        if (buffer.size < 3) {
            buffer.addLast(scanned)
            continue
        }

        // Once the buffer is full, we have a full window to operate on.
        val prev = buffer.sum()
        
        // "Do the ring buffer thing."
        buffer.removeFirst()
        buffer.addLast(scanned)

        // Get the new buffer sum with the new element.
        val next = buffer.sum()

        if (prev < next) {
            count++
        }
    }

    println("COUNT: $count")
}

main2()
