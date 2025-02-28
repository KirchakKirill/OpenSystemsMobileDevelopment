@file:Suppress("UNUSED_PARAMETER")
package mmcs.assignment2


/**
 * Ячейка матрицы: row = ряд, column = колонка
 */
data class Cell(val row: Int, val column: Int)

/**
 * Интерфейс, описывающий возможности матрицы. E = тип элемента матрицы
 */
interface Matrix<E> {
    /** Высота */
    val height: Int

    /** Ширина */
    val width: Int

    /**
     * Доступ к ячейке.
     * Методы могут бросить исключение, если ячейка не существует или пуста
     */
    operator fun get(row: Int, column: Int): E

    operator fun get(cell: Cell): E

    /**
     * Запись в ячейку.
     * Методы могут бросить исключение, если ячейка не существует
     */
    operator fun set(row: Int, column: Int, value: E)

    operator fun set(cell: Cell, value: E)
}

/**
 * Метод для создания матрицы, должен вернуть РЕАЛИЗАЦИЮ Matrix<E>.
 * height = высота, width = ширина, e = чем заполнить элементы.
 * Бросить исключение IllegalArgumentException, если height или width <= 0.
 */
fun <E> createMatrix(height: Int, width: Int, e: E): Matrix<E> {

    if(height <=0 || width <= 0){
        throw  IllegalArgumentException("height или width <= 0")
    }
    return MatrixImpl<E>(height,width,e)
}

/**
 * Реализация интерфейса "матрица"
 */

@Suppress("EqualsOrHashCode")
class MatrixImpl<E> constructor(override val height: Int, override val width: Int,defaultValue:E) : Matrix<E> {

    private val matrix:List<MutableList<E>> = List(height) { MutableList(width) { defaultValue } }


    override fun get(row: Int, column: Int): E {
        if (row !in 0..<height || column !in 0..<width) {
            throw IndexOutOfBoundsException("Invalid row or column index")
        }
        return this.matrix[row][column]
    }


    override fun get(cell: Cell): E = get(cell.row, cell.column)

    override fun set(row: Int, column: Int, value: E) {
        if (row !in 0..<height || column !in 0..<width) {
            throw IndexOutOfBoundsException("Invalid row or column index")
        }
        this.matrix[row][column] = value
    }

    override fun set(cell: Cell, value: E) = set(cell.row,cell.column,value)


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MatrixImpl<*>) return false
        if (height != other.height || width != other.width) return false
        return matrix == other.matrix
    }

    override fun hashCode(): Int {
        return matrix.hashCode()
    }

    override fun toString(): String {
        return matrix.joinToString(separator = "\n") { row ->
            row.joinToString(separator = " ") { it.toString() }
        }
    }
}

fun main(){
    val ww: Matrix<Int> = createMatrix<Int>(4,5,0)
    val ww1: Matrix<Int> = createMatrix<Int>(4,5,0)

    ww.set(3,3,11)
    ww1.set(3,3,12)

    println(ww.plus(ww1).toString())
}