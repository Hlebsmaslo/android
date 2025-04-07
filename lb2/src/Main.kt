//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import kotlin.random.Random
import kotlin.system.measureTimeMillis

class TooEarlyException(message: String) : Exception(message)
fun main() {
    println("Умовні вирази, if, when. №1 Визначити, чи є число парним.")
    println("Введіть число:")
    val number = readLine()!!.toInt()
    if (number % 2 == 0) {
        println("Число $number парне.")
    } else {
        println("Число $number непарне.")
    }
    println("Цикли та ітерації. №13 Знайти суму чисел від 1 до n.")
    println("Введіть число n:")
    val n = readLine()!!.toInt()
    // Обчислення суми чисел від 1 до n
    val sum = (1..n).sum()
    println("Сума чисел від 1 до $n дорівнює: $sum")
    println("Масиви, списки №18 Знайти найбільше і найменше число у масиві.")
    val numbers = IntArray(4)
    for (i in 0 until 4) {
        println("Введіть число ${i + 1}:")
        numbers[i] = readLine()!!.toInt()
    }
    val maxNumber = numbers.maxOrNull()
    val minNumber = numbers.minOrNull()

    // Виведення результату
    println("Найбільше число: $maxNumber")
    println("Найменше число: $minNumber")
    println("Null safety №22 Прийняти ім’я користувача (String?), вивести Привіт, [ім’я], або Привіт, Anonymous якщо null/порожнє.")
        // Запит на введення імені користувача
        println("Введіть ваше ім'я:")
        val name: String? = readLine()

        // Перевірка, чи ім'я не null і не порожнє
        if (name.isNullOrBlank()) {
            println("Привіт, Anonymous")
        } else {
            println("Привіт, $name")
        }
    println("Обробка винятків №27 Прочитати ціле число з консолі — з перевіркою NumberFormatException.")
    println("Введіть ціле число:")
    val input = readLine()
    try {
        val number1 = input?.toInt() ?: throw NumberFormatException()
        println("Ви ввели число: $number1")
    } catch (e: NumberFormatException) {
        println("Помилка: введено не ціле число.")
    }
    println("ЗАДАЧА ВАРІАНТ 9")
    val attempts = mutableListOf<Long>()
    val totalAttempts = 5
    println("Ви повинні натискати Enter після випадкової затримки.")
    repeat(totalAttempts) { attemptNumber ->
        println("\nСпроба ${attemptNumber + 1}:")
        val delayTime = Random.nextLong(1000, 5000)
        Thread.sleep(delayTime)
        println("Натисніть Enter зараз!")
        val startTime = System.currentTimeMillis()
        val input = readLine()
        if (input == null || input.isEmpty()) {
            val reactionTime = System.currentTimeMillis() - startTime
            attempts.add(reactionTime)
            println("Ваш час реакції: ${reactionTime / 1000.0} секунд.")
        } else {
            throw TooEarlyException("Ви натиснули клавішу до сигналу!")
        }
    }
    val bestTime = attempts.minOrNull() ?: 0L
    val worstTime = attempts.maxOrNull() ?: 0L
    val averageTime = attempts.average()
    println("\nРезультати тесту:")
    println("Найкращий час: ${bestTime / 1000.0} секунд")
    println("Найгірший час: ${worstTime / 1000.0} секунд")
    println("Середній час: ${averageTime / 1000.0} секунд")
    }

