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

# Assignment #3. The Calculator   
Задание — создать свое первое приложение для Android.   

Базовая структура приложения уже присутствует в репозитории.   

Вам необходимо реализовать ViewModel.   

Связь между ViewModel и Layout реализована с помощью DataBinding.   

Функциональная часть модели представления описана в интерфейсе Калькулятора. 

#  Assignment #4. The Calculator (Jetpack Compose)   

Implement Calculator interface with Jetpack Compose:  


#  Assignment #5. TODO List. Interface    

Создайте простое приложение списка дел с данными-заглушками.    

Используйте recyclerView и адаптеры для заполнения списка.    

Другие макеты не требуются. Данные можно хранить в локальной переменной. Добавление и удаление новых элементов не требуется.    

# Assignment #6 TODO List. 2 Navigation    

Add NavigationController to the app.    

1.Add an "Add item" button.   
2.Add a new item screen.   
3.Add a detail info to each item.   
4.Add a detail screen to show detailed information.    

Requirements:      

Three screens:   
1.todo list, 
2.an item details screen,   
3.add a new item screen.   

Navigation Graph / Navigation Host    