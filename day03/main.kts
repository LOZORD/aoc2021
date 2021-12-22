import java.util.Scanner
import java.util.Collections

fun main1() {
    // ones collects the number of ones at each bit index for each input
    // binary number.
    var ones: IntArray? = null
    val input = Scanner(System.`in`)
    var total = 0

    while (input.hasNextLine()) {
        val scanned = input.nextLine()
        total++

        if (ones == null) {
            ones = IntArray(scanned.length)
        }

        for ((i, c) in scanned.withIndex()) {
            if (c == '1') {
                ones[i]++
            }
        }

        // println("ones is %s; total is %d; input was %s".format(
        //     ones.contentToString(), total, scanned))
    }

    var mostCommon = 0
    var leastCommon = 0
    for ((i, count) in ones!!.withIndex()) {
        val pct = (100.0 * count) / total
        if (pct > 50.0) {
            mostCommon = mostCommon or (1 shl (ones.size - i - 1))
        } else {
            leastCommon = leastCommon or (1 shl (ones.size - i - 1))
        }

        // println("MOST: %s; LEAST: %s".format(
        //  Integer.toBinaryString(mostCommon), Integer.toBinaryString(leastCommon)))
    }   

    println("GAMMA (MOST): %d; EPSILON (LEAST): %d; MULTIPLIED: %d".format(
        mostCommon, leastCommon, mostCommon * leastCommon))
}

fun main2() {
    val input = Scanner(System.`in`)
    val list = ArrayList<Int>()
    
    // Insight: the most common bits are the median number
    // when the list is sorted.
    // ^^ This is likely wrong, but we can do some clever binary searching if
    // the numbers are sorted since bits also appear sorted (all 0s before all
    // 1s in any given sorted collection of binary numbers).
    // NOTE: This algorithm (the binary search part) only works if the numbers
    // are all distinct/unique.

    var numBits = -1
    while (input.hasNextLine()) {
        val line = input.nextLine()
        if (numBits < 0) {
            numBits = line.length
        }
        list.add(Integer.parseInt(line, 2))
    }

    Collections.sort(list)

    // start: The start index of the sublist.
    //
    // end: Similar, for the end.
    //
    // searchAccumulation: The accumulated number that is part of the number
    // to which we binary search. It encodes (or its components are) the binary
    // digits of our search. I.e. the nth bit of searchAccumulation is 1 iff
    // 1 was the majority of the nth column (given the previously included
    // numbers).
    data class State(val start: Int, val end: Int, val searchAccumulation: Int)
    // stepIndex: which iteration of searching this stp is.
    //
    // searchIndex: the "actual index" of binary search (translating the
    // insertion point). Note that this searchIndex is over the entire list and
    // not just the sublist in "focus" (determined by start and end).
    //
    // oneIsMajority: True iff 1 is the majority bit for this stp.
    data class Step(val stepIndex: Int, val searchIndex: Int, val oneIsMajority: Boolean)

    fun doSearch(stpper: (Step, State) -> State): Int {
        var start = 0
        var end = list.size - 1
        var i = 0
        var searchAccumulation = 0
        while (start != end) {
            val size = end - start + 1
            val shifted = 1 shl (numBits - i - 1)
            val search = searchAccumulation + shifted
            val index = list.binarySearch(search, start, end + 1)
            val actualIndex =
                if (index >= 0) {
                    index
                } else {
                    -(index+1)
                }

            val normalizedIndex = actualIndex - start

            val newState = stpper(Step(
                stepIndex = i,
                searchIndex = actualIndex,
                oneIsMajority = normalizedIndex <= size / 2,
            ), State(
                start,
                end,
                searchAccumulation,
            ))
           
            start = newState.start
            end = newState.end
            searchAccumulation = newState.searchAccumulation

            i++
        }

        return list.get(start)
    }

    // var start = 0
    // var end = list.size - 1
    // var i = 0
    // var searchAccumulation = 0
    // while (start != end) {
    //     val size = end - start + 1
    //     println("start $start; end $end; i $i; size $size; searchAccumulation $searchAccumulation")
    //     val shifted = 1 shl (numBits - i - 1)
    //     val search = searchAccumulation + shifted
    //     val index = list.binarySearch(search, start, end + 1)
    //     val actualIndex =
    //         if (index >= 0) {
    //             index
    //         } else {
    //             -(index+1)
    //         }

    //     val normalizedIndex = actualIndex - start
    //     println("searched for $search; got actual index $actualIndex; NI $normalizedIndex")


    //     if (normalizedIndex > size / 2) {
    //         // 0 is the more common digit.
    //         searchAccumulation += 0
    //         start = start
    //         end = actualIndex - 1
    //         println("end is now $end; SA is $searchAccumulation")
    //     } else {
    //         // 1 is the more common digit (or tie).
    //         searchAccumulation += shifted
    //         start = actualIndex
    //         end = end
    //         println("start is now $start; SA is $searchAccumulation")
    //     }

    //     i++
    // }

    // var start = 0
    // var end = list.size - 1
    // var i = 0
    // while (i < numBits && start < end) {
    //     println("start $start; end $end; examining list: %s".format(list.subList(start, end+1)))
    //     var size = end - start + 1
    //     // Search the binary number 10...0 that is less that the number of
    //     // bits in the list.
    //     val search = 1 shl (numBits - i - 1)
    //     println("binary searching for %s among %s".format(Integer.toBinaryString(search), list.subList(start,end+1).map(Integer::toBinaryString)))
    //     val index = list.binarySearch(search, start, end)
    //     val actualIndex: Int
    //     if (index >= 0) {
    //         actualIndex = index
    //     } else {
    //         actualIndex = -(index+1) // Insertion point.
    //     }
    //     println("index $index; actualIndex $actualIndex")

    //     if (actualIndex > (size / 2)) {
    //         // 0 in majority.
    //         end = actualIndex - 1
    //     } else {
    //         // 1 in majority (or tie).
    //         start = actualIndex
    //     }
    //     i++
    // }

    // val mostCommon = list.get(start)


    val mostCommon = doSearch({ stp, state -> 
        if (stp.oneIsMajority) {
            state.copy(
                searchAccumulation = state.searchAccumulation + (1 shl numBits - stp.stepIndex - 1),
                start = stp.searchIndex,
            )
        } else {
            state.copy(
                end = stp.searchIndex - 1,
            )
        }
    })

    val leastCommon = doSearch({ stp, state ->
        if (stp.oneIsMajority) {
            state.copy(
                end = stp.searchIndex - 1,
            )
        } else {
            state.copy(
                searchAccumulation = state.searchAccumulation + (1 shl numBits - stp.stepIndex - 1),
                start = stp.searchIndex,
            )
        }
    })

    println("MOST COMMON: $mostCommon; LEAST COMMON: $leastCommon; MULTIPLIED: %d".format(mostCommon*leastCommon))
}

main2()