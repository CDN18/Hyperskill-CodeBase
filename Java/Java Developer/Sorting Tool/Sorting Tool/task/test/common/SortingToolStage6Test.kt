package common

import org.hyperskill.hstest.stage.StageTest
import org.hyperskill.hstest.testcase.CheckResult
import org.hyperskill.hstest.testcase.TestCase
import org.hyperskill.hstest.testing.Settings
import java.io.File
import java.io.FileNotFoundException
import java.util.*

class SortingToolStage6Test : StageTest<SortingToolClue>() {

    init {
        Settings.allowOutOfInput = true
    }

    override fun generate(): List<TestCase<SortingToolClue>> {
        return stage4Tests() + stage5Tests() + stage6Tests()
    }

    override fun check(reply: String, clue: SortingToolClue): CheckResult {
        @Suppress("NAME_SHADOWING") var reply = reply

        val fileNameArgIdx = clue.args.indexOf("-outputFile")

        if (fileNameArgIdx != -1) {
            val fileName = clue.args[fileNameArgIdx + 1]

            try {
                reply = File(fileName).readText()
            } catch (ex: FileNotFoundException) {
                return CheckResult.wrong(
                    "There is no output file $fileName"
                )
            }

        }

        return when {
            badArgs(clue.args) -> CheckResult(true)  // TODO: test something here
            "byCount" in clue.args -> checkByCount(reply, clue)
            else -> checkNatural(reply, clue)
        }
    }

    private fun checkByCount(reply: String, clue: SortingToolClue): CheckResult {
        return when {
            "long" in clue.args -> checkByCount(parseLongTokens(clue.consoleInput), { it.toInt() }, clue, reply)
            "word" in clue.args -> checkByCount(parseWordTokens(clue.consoleInput), { it }, clue, reply)
            "line" in clue.args -> checkByCount(parseLineTokens(clue.consoleInput), { it }, clue, reply)

            else -> throw IllegalArgumentException("Bad test: no data type found")
        }
    }

    private fun checkNatural(reply: String, clue: SortingToolClue): CheckResult {
        return when {
            "long" in clue.args -> checkNatural(parseLongTokens(clue.consoleInput), ::parseLongTokens, clue, reply)
            "word" in clue.args -> checkNatural(parseWordTokens(clue.consoleInput), ::parseWordTokens, clue, reply)
            "line" in clue.args -> checkNatural(parseLineTokens(clue.consoleInput), ::parseLineTokens, clue, reply)

            else -> throw IllegalArgumentException("Bad test: no data type found")
        }
    }

    private fun badArgs(args: List<String>): Boolean {
        val unknownArgs =
                args.toSet() - setOf("-dataType", "-sortingType", "long", "word", "line", "natural", "byCount")

        if (unknownArgs.isNotEmpty()) {
            return true
        }

        if (args.last() == "-dataType" || args.last() == "-sortingType") {
            return true
        }

        return false
    }
}

private fun fileTestCase(
        input: String,
        revealTest: Boolean,
        file: String,
        vararg args: String = arrayOf("-dataType", "long")
): TestCase<SortingToolClue> {
    return TestCase<SortingToolClue>()
            .setAttach(SortingToolClue(input, revealTest, args.toList()))
            .addArguments(*args)
            .addFile(file, input)
}

fun stage4Tests(): List<TestCase<SortingToolClue>> {
    return listOf(
            createTest(
                    """
                |1 -2   333 4
                |42
                |1                 1
                """.trimMargin(),
                    true,
                    "-dataType", "long", "-sortingType", "natural"
            ),
            createTest(
                    """
                |1 -2   333 4
                |42
                |1                 1
                """.trimMargin(),
                    true,
                    "-dataType", "long"
            ),
            createTest(
                    """
                |1 -2   333 4
                |42
                |1                 1
                """.trimMargin(),
                    true,
                    "-sortingType", "byCount", "-dataType", "long"
            ),
            createTest(
                    """
                |1 -2   333 4
                |42
                |1                 1
                """.trimMargin(),
                    true,
                    "-sortingType", "byCount", "-dataType", "word"
            ),
            createTest(
                    """
                |1 -2   333 4
                |42
                |42
                |1                 1
                """.trimMargin(),
                    true,
                    "-sortingType", "byCount", "-dataType", "line"
            ),
            createTest(
                    """
                |1111 1111
                |22222
                |3
                |44
                """.trimMargin(),
                    false,
                    "-sortingType", "byCount", "-dataType", "line"
            ),
            createTest(
                    """
                |1111 1111
                |22222
                |3
                |44
                """.trimMargin(),
                    false,
                    "-sortingType", "byCount", "-dataType", "word"
            ),
            createTest(
                    """
                |1111 1111
                |22222
                |3
                |44
                """.trimMargin(),
                    false,
                    "-sortingType", "byCount", "-dataType", "long"
            )
    )
}

fun stage5Tests(): List<TestCase<SortingToolClue>> {
    return listOf(
            createTest(
                    """
                |1 -2   333 4
                |42
                |1                 1
                """.trimMargin(),
                    true,
                    "-sortingType"
            ),
            createTest(
                    """
                |1 -2   333 4
                |42
                |1                 1
                """.trimMargin(),
                    true,
                    "-sortingType", "byCount", "-dataType", "long", "-abc"
            ),
            createTest(
                    """
                |1111 1111
                |22222
                |3
                |44
                """.trimMargin(),
                    false,
                    "-sortingType", "byCount", "-dataType", "line", "-cde"
            ),
            createTest(
                    """
                |1111 1111
                |22222
                |3
                |44
                """.trimMargin(),
                    false,
                    "-dataType", "line", "-sortingType"
            )
    )
}

fun stage6Tests(): List<TestCase<SortingToolClue>> {
    return listOf(
            fileTestCase(
                    """
                |1 -2   333 4
                |42
                |1                 1
                """.trimMargin(),
                    true,
                    "input.txt",
                    "-sortingType", "byCount", "-inputFile", "input.txt"
            ),
            fileTestCase(
                    """
                |1 -2   333 4
                |42
                |1                 1
                """.trimMargin(),
                    true,
                    "data.dat",
                    "-sortingType", "byCount", "-inputFile", "data.dat", "-outputFile", "out.txt"
            ),
            fileTestCase(
                    """
                |1 -2   333 4
                |42
                |1                 1
                """.trimMargin(),
                    false,
                    "input.txt",
                    "-sortingType", "natural", "-inputFile", "input.txt"
            ),
            fileTestCase(
                    """
                |1 -2   333 4
                |42
                |1                 1
                """.trimMargin(),
                    false,
                    "data.dat",
                    "-sortingType", "natural", "-inputFile", "data.dat", "-outputFile", "out.txt"
            )
    )
}


fun revealRawTest(clue: SortingToolClue, reply: String): String {
    return with(clue) { "Args:\n${args.joinToString(" ")}\nInput:\n$consoleInput\nYour output:\n$reply\n\n" }
}

class SortingToolClue(val consoleInput: String, val revealTest: Boolean, val args: List<String>)

fun createTest(
        consoleInput: String,
        revealTest: Boolean,
        vararg args: String = arrayOf("-dataType", "long")
): TestCase<SortingToolClue> {
    return TestCase<SortingToolClue>()
            .setAttach(SortingToolClue(consoleInput, revealTest, args.toList()))
            .setInput(consoleInput)
            .addArguments(*args)
}

fun parseLongTokens(input: String): List<Int> {
    val scanner = Scanner(input)

    val longTokens = mutableListOf<Int>()

    while (scanner.hasNextInt()) {
        longTokens.add(scanner.nextInt())
    }

    return longTokens
}

fun parseWordTokens(input: String): List<String> {
    val scanner = Scanner(input)

    val wordTokens = mutableListOf<String>()

    while (scanner.hasNext()) {
        wordTokens.add(scanner.next())
    }

    return wordTokens
}


fun parseLineTokens(input: String): List<String> {
    return input.lines()
}


fun <TokenType : Comparable<TokenType>> checkNatural(
        actualTokens: List<TokenType>,
        sortedTokensParser: (String) -> List<TokenType>,
        clue: SortingToolClue,
        reply_: String
): CheckResult {
    val reply = reply_.trim()
    val lines = reply.lines()

    if (lines.size != 2) {
        return if (clue.revealTest) {
            CheckResult(
                    false,
                    "Can't parse your output: expected 2 lines.\n" +
                            revealRawTest(clue, reply)
            )
        } else {
            CheckResult(false, "Can't parse your output: expected 2 lines.")
        }
    }

    val totalRegex = """(\d+)""".toRegex()
    val totalMatchResult = totalRegex.find(lines[0])
    if (totalMatchResult == null) {
        return if (clue.revealTest) {
            CheckResult(
                    false,
                    "Can't find number in the first line of your output.\n" +
                            revealRawTest(clue, reply)
            )
        } else {
            CheckResult(false, "Can't find number in the first line of your output.")
        }
    }

    val totalTokens = totalMatchResult.groupValues[1].toInt()

    val actualTotal = actualTokens.size

    if (actualTotal != totalTokens) {
        return if (clue.revealTest) {
            CheckResult(
                    false,
                    "Total tokens ($totalTokens) are incorrect. Expected: $actualTotal.\n" +
                            revealRawTest(clue, reply)
            )
        } else {
            CheckResult(false, "Total tokens are incorrect.")
        }
    }

    val sortedActualTokens = actualTokens.sorted()

    val sortedTokens = sortedTokensParser(lines[1].substringAfter(":").dropWhile { it in setOf('\n', '\r') })

    val total = sortedTokens.size

    if (actualTotal != total) {
        return if (clue.revealTest) {
            CheckResult(
                    false,
                    "Total sorted tokens ($total) are incorrect. Expected: $actualTotal.\n" +
                            revealRawTest(clue, reply)
            )
        } else {
            CheckResult(false, "Total sorted tokens are incorrect.")
        }
    }

    if (sortedActualTokens != sortedTokens) {
        return if (clue.revealTest) {
            CheckResult(
                    false,
                    "Sorted tokens are incorrect.\n" +
                            revealRawTest(clue, reply)
            )
        } else {
            CheckResult(false, "Sorted tokens are incorrect.")
        }
    }

    return CheckResult(true)
}


fun <TokenType : Comparable<TokenType>> checkByCount(
        actualTokens: List<TokenType>,
        tokenParser: (String) -> TokenType,
        clue: SortingToolClue,
        reply_: String
): CheckResult {
    val reply = reply_.trim()
    val lines = reply.lines()

    val totalRegex = """(\d+)""".toRegex()
    val totalMatchResult = totalRegex.find(lines[0])
    if (totalMatchResult == null) {
        return if (clue.revealTest) {
            CheckResult(
                    false,
                    "Can't find number in the first line of your output.\n" +
                            revealRawTest(clue, reply)
            )
        } else {
            CheckResult(false, "Can't find number in the first line of your output.")
        }
    }

    val totalTokens = totalMatchResult.groupValues[1].toInt()

    val actualTotal = actualTokens.size

    if (actualTotal != totalTokens) {
        return if (clue.revealTest) {
            CheckResult(
                    false,
                    "Total tokens ($totalTokens) are incorrect. Expected: $actualTotal.\n" +
                            revealRawTest(clue, reply)
            )
        } else {
            CheckResult(false, "Total tokens are incorrect.")
        }
    }

    val tokenToCount = mutableMapOf<TokenType, Int>()

    for (token in actualTokens) {
        tokenToCount[token] = (tokenToCount[token] ?: 0) + 1
    }

    val actualSortedByCount = tokenToCount.toList().sortedWith(compareBy({ it.second }, { it.first }))
    val linesWithTokens = lines.drop(1)

    if (actualSortedByCount.size != linesWithTokens.size) {
        return if (clue.revealTest) {
            CheckResult(
                    false,
                    "Lines with tokens (${linesWithTokens.size}) are incorrect. Expected: ${actualSortedByCount.size}.\n" +
                            revealRawTest(clue, reply)
            )
        } else {
            CheckResult(false, "Lines with tokens are incorrect.")
        }
    }

    for ((lineId, line) in linesWithTokens.withIndex()) {
        val token = tokenParser(line.substringBefore(':'))
        val info = line.substringAfter(':')

        val (actualToken, actualTimes) = actualSortedByCount[lineId]

        if (token != actualToken) {
            return if (clue.revealTest) {
                CheckResult(
                        false,
                        "Token ($token) is incorrect. Expected: $actualToken.\n" +
                                revealRawTest(clue, reply)
                )
            } else {
                CheckResult(false, "Token is incorrect.")
            }
        }

        // TODO: check info (times and percentage)
    }

    return CheckResult(true)
}


