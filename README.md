# OpenSystemsMobileDevelopment  
# Assignment №1. Playing with Kotlin Implement mechanics of the board game of your choice:   
1.Chess   
2.Draughts  
3.Nard   
4.Rummy  
5.Durak ✔   
6.Monopoly   
...   


# Assignment №2. The Matrix  
Реализовать класс матрицы из файла Matrix.kt  

Решить с помощью него задачи из файла Tasks.kt    

Немного теории:   
При реализации обобщенного типа нельзя использовать Array<T>. Т.к. обобщения в Kotlin компилируются, и Array<T> должен быть скомпелирован в T[] JVM и тип T должен быть известен компилятору. Для таких случаев можно использовать модификатор типа reified.   

В данной реализации удобнее использовать List<T>, т.к. он не связан с базовым типом Java и реализует интерфейс массива.   
