import java.util.Scanner

fun main(args: Array<String>) {
    val digitsToWires: Array<Set<Char>> = arrayOf(
        // 0
        setOf('a', 'b', 'c', 'e', 'f', 'g'),
        // 1
        setOf('c', 'f'),
        // 2
        setOf('a', 'c', 'd', 'e', 'g'),
        // 3
        setOf('a', 'c', 'd', 'f', 'g'),
        // 4
        setOf('b', 'c', 'd', 'f'),
        // 5
        setOf('a', 'b', 'd', 'f', 'g'),
        // 6
        setOf('a', 'b', 'd', 'e', 'f', 'g'),
        // 7
        setOf('a', 'c', 'f'),
        // 8
        setOf('a', 'b', 'c', 'd', 'e', 'f', 'g'),
        // 9
        setOf('a', 'b', 'c', 'd', 'f', 'g'),
    )

    // Map of length => Index(index=digit, value=character set).
    val uniqueDigitLengthsMap = digitsToWires
        .withIndex()
        .toList()
        .groupBy { it.value.size }
        .filterValues { it.size == 1 }
        .mapValues { it.value.get(0) }

    val uniqueLengthsSet = uniqueDigitLengthsMap.keys.toSet()

    if (args.contains("--part1")) {
        var count = 0
        val input = Scanner(System.`in`)
        while (input.hasNextLine()) {
            val scanned = input.nextLine()
            val signalsAndOutputs = scanned.split('|')
            check(signalsAndOutputs.size == 2)
            val split = signalsAndOutputs[1].split(Regex("\\s+"))
            count += split
                .filter { uniqueLengthsSet.contains(it.length) }
                .size
        }
        println("COUNT: $count")
    }

    fun getOnlyElement(s: Set<Char>): Char {
        check(s.size == 1) { "expected $s to have only one element" }
        return s.first()
    }

    fun findMissing(signals: Set<Set<Char>>, allButOne: Set<Char>): Char {
        val s = signals.find {
            (it.size == allButOne.size + 1) && (it.containsAll(allButOne))
        }
        // println("found missing signal: $s from argument=$allButOne")
        val signal = s!!
        val diff = signal.subtract(allButOne)
        // println("diff is $diff")
        return getOnlyElement(diff)
    }

    fun unscrambleWires(signals: Set<Set<Char>>): Map<Char, Char> {
        val solved = HashMap<Char, Char>(7)
        val digits = HashMap<Int, Set<Char>>(10)
        val all = digitsToWires[8]

        // Place all of the unique length digits in the digits map.
        signals
            .filter { uniqueLengthsSet.contains(it.size) }
            .forEach {
                val digit = uniqueDigitLengthsMap.get(it.size)!!.index
                digits.set(digit, it)
            }

        // Find `a`: `7` - `1`.
        solved.put('a',
            getOnlyElement(digits.get(7)!!.subtract(digits.get(1)!!)))

        // Find `g`: `9` - (`4` U `a`).
        solved.put('g',
            findMissing(signals,
                digits.get(4)!!.union(setOf(solved.get('a')!!))))

        // Find `d`: `3` - (`7` U `g`).
        solved.put('d',
            findMissing(signals,
                digits.get(7)!!.union(setOf(solved.get('g')!!))))

        // Find `e`: `8` - `9` = `8` - (`4` U `a` U `g`)
        solved.put('e', getOnlyElement(
            all.subtract(digits.get(4)!!.union(setOf(solved.get('a')!!, solved.get('g')!!)))))

        // Find `b`: `8` - (`3` U `e`) = `8` - (`7` U `d` U `g` U `e`).
        solved.put('b', getOnlyElement(
            all.subtract(digits.get(7)!!.union(setOf(
                solved.get('d')!!,
                solved.get('g')!!,
                solved.get('e')!!,
            )))))

        // Find `f`: find the signal is just one wire larger than the
        // translation of `abde[f=?]g` (based on what we know).
        solved.put('f', findMissing(signals,
            setOf(
                solved.get('a')!!,
                solved.get('b')!!,
                solved.get('d')!!,
                solved.get('e')!!,
                solved.get('g')!!,
            )))

        // Find `c`: the remaining wire.
        solved.put('c', getOnlyElement(all.filter{!solved.containsValue(it)}.toSet()))

        return solved
    }

    var sum = 0
    val input = Scanner(System.`in`)
    while (input.hasNextLine()) {
        val scanned = input.nextLine()
        val signalsAndOutputs = scanned.split('|')
        check(signalsAndOutputs.size == 2)
        val signals = signalsAndOutputs[0].trim().split(' ').map { it.toCharArray().toSet() }.toSet()
        val output = signalsAndOutputs[1].trim().split(' ').map { it.toCharArray().toSet() }
        val wireMapping = unscrambleWires(signals)

        // println(wireMapping)
        // for (entry in wireMapping) {
        //  println("UNSCRAMBLED %c <=> SCRAMBLED %c".format(entry.key, entry.value))
        // }

        // var outputNumber = output
        //     .map { wires: Set<Char> -> wires.map { wireMapping.get(it)!! }.toSet() }
        //     .map { digitsToWires.indexOf(it) }
        //     .reversed()
        //     .reduceIndexed { i, cur, acc -> acc + (i * 10 * cur) }

        // println(wireMapping)
        // println(output)

        // https://stackoverflow.com/a/45380326
        var reversed = wireMapping.entries.associateBy({ it.value }) { it.key }

        fun getDigit(wires: Iterable<Char>): Int {
            val n = digitsToWires.indexOf(wires.toSet())
            check(n >= 0)
            return n
        }

        var outputNumber = 0
        for ((i, scrambled) in output.reversed().withIndex()) {
            var digit = getDigit(scrambled.map { reversed.get(it)!! })
            outputNumber += digit * Math.pow(10.0, i.toDouble()).toInt()
        }

        // println(outputNumber)

        sum += outputNumber
    }
    print("SUM: $sum")
    
}