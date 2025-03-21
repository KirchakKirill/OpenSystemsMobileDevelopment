@file:Suppress("UNUSED_PARAMETER")
package mmcs.assignment2
/**
 * Пример
 *
 * Транспонировать заданную матрицу matrix.
 */
fun <E> transpose(matrix: Matrix<E>): Matrix<E> {
    if (matrix.width < 1 || matrix.height < 1) return matrix
    val result = createMatrix(height = matrix.width, width = matrix.height, e = matrix[0, 0])
    for (i in 0..<matrix.width) {
        for (j in 0..<matrix.height) {
            result[i, j] = matrix[j, i]
        }
    }
    return result
}

fun <E> rotate(matrix: Matrix<E>): Matrix<E> {
    if (matrix.width < 1 || matrix.height < 1) return matrix
    val result = createMatrix(height = matrix.width, width = matrix.height, e = matrix[0, 0])

    for (i in 0..<matrix.height) {
        for (j in 0..<matrix.width) {
            result[j, matrix.height - 1 - i] = matrix[i, j]
        }
    }
    return result
}

/**
 * Сложить две заданные матрицы друг с другом.
 * Складывать можно только матрицы совпадающего размера -- в противном случае бросить IllegalArgumentException.
 * При сложении попарно складываются соответствующие элементы матриц
 */
operator fun Matrix<Int>.plus(other: Matrix<Int>): Matrix<Int>
{
    if (this.height==other.height && this.width == other.width)
        for ( i  in  0 ..<this.height)
            for (j in 0 ..<this.width)
                this[i, j] += other[i, j]
    else
        throw IllegalArgumentException(" Матрицы  не совпадающего размера")



    return this
}

/**
 * Инвертировать заданную матрицу.
 * При инвертировании знак каждого элемента матрицы следует заменить на обратный
 */
operator fun Matrix<Int>.unaryMinus(): Matrix<Int>
{
    for ( i  in  0 ..<this.height)
        for (j in 0 ..<this.width)
            this[i, j]  = -1*this[i,j]

    return this
}


/**
 * Перемножить две заданные матрицы друг с другом.
 * Матрицы можно умножать, только если ширина первой матрицы совпадает с высотой второй матрицы.
 * В противном случае бросить IllegalArgumentException.
 * Подробно про порядок умножения см. статью Википедии "Умножение матриц".
 */
operator fun Matrix<Int>.times(other: Matrix<Int>): Matrix<Int> {

    if (this.width == other.height)
    {
        for ( i  in  0 ..<this.height)
            for (j in 0 ..<this.width)
                this[i,j]*=other[j,i]
        return this
    }
    else
    {
        throw IllegalArgumentException("Некорректная размерность матриц")
    }

}


/**
 * Целочисленная матрица matrix состоит из "дырок" (на их месте стоит 0) и "кирпичей" (на их месте стоит 1).
 * Найти в этой матрице все ряды и колонки, целиком состоящие из "дырок".
 * Результат вернуть в виде Holes(rows = список дырчатых рядов, columns = список дырчатых колонок).
 * Ряды и колонки нумеруются с нуля. Любой из спискоов rows / columns может оказаться пустым.
 *
 * Пример для матрицы 5 х 4:
 * 1 0 1 0
 * 0 0 1 0
 * 1 0 0 0 ==> результат: Holes(rows = listOf(4), columns = listOf(1, 3)): 4-й ряд, 1-я и 3-я колонки
 * 0 0 1 0
 * 0 0 0 0
 */
fun findHoles(matrix: Matrix<Int>): Holes
{
    val rows:MutableList<Int> =  mutableListOf()
    val cols:MutableList<Int> =  mutableListOf()
    for ( i  in  0 ..<matrix.height) {
        var COUNT_ROWS = 0

        for (j in 0..<matrix.width) {
            COUNT_ROWS+=matrix[i,j]
        }

        if (COUNT_ROWS==0 || COUNT_ROWS==matrix.width)
            rows.add(i)
    }

    for ( i  in  0 ..<matrix.width) {
        var COUNT_COLS = 0

        for (j in 0..<matrix.height) {
            COUNT_COLS+=matrix[j,i]
        }

        if (COUNT_COLS==0 || COUNT_COLS==matrix.height)
            cols.add(i)

    }

    return Holes(rows,cols)
}

/**
 * Класс для описания местонахождения "дырок" в матрице
 */
data class Holes(val rows: List<Int>, val columns: List<Int>)

/**
 * Даны мозаичные изображения замочной скважины и ключа. Пройдет ли ключ в скважину?
 * То есть даны две матрицы key и lock, key.height <= lock.height, key.width <= lock.width, состоящие из нулей и единиц.
 *
 * Проверить, можно ли наложить матрицу key на матрицу lock (без поворота, разрешается только сдвиг) так,
 * чтобы каждой единице в матрице lock (штырь) соответствовал ноль в матрице key (прорезь),
 * а каждому нулю в матрице lock (дырка) соответствовала, наоборот, единица в матрице key (штырь).
 * Ключ при сдвиге не может выходить за пределы замка.
 *
 * Пример: ключ подойдёт, если его сдвинуть на 1 по ширине
 * lock    key
 * 1 0 1   1 0
 * 0 1 0   0 1
 * 1 1 1
 *
 * Вернуть тройку (Triple) -- (да/нет, требуемый сдвиг по высоте, требуемый сдвиг по ширине).
 * Если наложение невозможно, то первый элемент тройки "нет" и сдвиги могут быть любыми.
 */
fun canOpenLock(key: Matrix<Int>, lock: Matrix<Int>): Triple<Boolean, Int, Int> {
    val keyHeight = key.height
    val keyWidth = key.width
    val lockHeight = lock.height
    val lockWidth = lock.width

    for (shiftY in 0..lockHeight - keyHeight) {
        for (shiftX in 0..lockWidth - keyWidth) {
            var isMatch = true

            for (i in 0..<keyHeight) {
                for (j in 0..<keyWidth) {
                    if (lock[shiftY + i,shiftX + j] == key[i,j]) {
                        isMatch = false
                        break
                    }
                }
                if (!isMatch) break
            }

            if (isMatch) {
                return Triple(true, shiftY, shiftX)
            }
        }
    }

    return Triple(false, -1, -1)
}