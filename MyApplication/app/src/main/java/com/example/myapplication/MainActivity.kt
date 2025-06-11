@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                AppNavigator()
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        AppNavigator()
    }
}

@Composable
fun AppNavigator() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "todo_list") {
        composable("todo_list") {
            TodoListScreen(navController)
        }
        composable("add_item") {
            AddItemScreen(navController)
        }
        composable(
            "detail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val index = backStackEntry.arguments?.getInt("id") ?: -1
            DetailScreen(index)
        }
    }
}

@Composable
fun TodoListScreen(
    navController: NavController,
) {
    val context = LocalContext.current
    val dao = remember { AppDatabase.getDatabase(context).taskDao() }
    val tasks by dao.getAllTasksFlow().collectAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()

    val checkedCount = tasks.count { it.isChecked }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("To do list", color = MaterialTheme.colorScheme.background) },
                actions = {
                    Text("Total: ${tasks.size} - Checked: $checkedCount", color = MaterialTheme.colorScheme.background)
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(37, 147, 245))
            )
        },
        bottomBar = {
            BottomAppBar {
                Button(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    shape = RectangleShape,
                    onClick = { navController.navigate("add_item") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.tertiary
                    )
                ) {
                    Text("Add", fontSize = 30.sp)
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(tasks) { task ->
                TaskItem(
                    task = task,
                    onCheckedChange = { updatedTask ->
                        coroutineScope.launch { dao.updateTask(updatedTask) }
                    },
                    onClick = {
                        navController.navigate("detail/${task.id}")
                    },
                    onDelete = {
                        coroutineScope.launch { dao.deleteTask(task) }
                    }
                )
            }
        }
    }
}

@Composable
fun TaskItem(
    task: TaskEntity,
    onCheckedChange: (TaskEntity) -> Unit,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
            .shadow(1.dp)
            .clickable { onClick() },
        shape = RectangleShape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isChecked,
                onCheckedChange = {
                    onCheckedChange(task.copy(isChecked = it))
                }
            )
            Text(
                text = task.name,
                modifier = Modifier.fillMaxHeight(),
                textDecoration = if (task.isChecked) TextDecoration.LineThrough else TextDecoration.None,
                color = MaterialTheme.colorScheme.tertiary
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = onDelete) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete task")
            }
        }
    }
}

@Composable
fun DetailScreen(taskId: Int) {
    val context = LocalContext.current
    val dao = remember { AppDatabase.getDatabase(context).taskDao() }
    val tasks by dao.getAllTasksFlow().collectAsState(initial = emptyList())
    val task = tasks.find { it.id == taskId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Task Detail", color = MaterialTheme.colorScheme.background) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(37, 147, 245))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            if (task != null) {
                Text("Task Name:", style = MaterialTheme.typography.titleLarge)
                Text(
                    task.name,
                    style = MaterialTheme.typography.bodyLarge,
                    textDecoration = if (task.isChecked) TextDecoration.LineThrough else TextDecoration.None,
                )
                Spacer(modifier = Modifier.padding(8.dp))
                Text("Task Content:", style = MaterialTheme.typography.titleLarge)
                Text(task.content, style = MaterialTheme.typography.bodyLarge)
            } else {
                Text("Task not found", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Composable
fun AddItemScreen(navController: NavController) {
    val context = LocalContext.current
    val dao = remember { AppDatabase.getDatabase(context).taskDao() }
    val coroutineScope = rememberCoroutineScope()

    val newTaskName = remember { mutableStateOf("") }
    val newTaskContent = remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Item", color = MaterialTheme.colorScheme.background) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(37, 147, 245))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            TextField(
                value = newTaskName.value,
                onValueChange = { newTaskName.value = it },
                label = { Text("Task Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(8.dp))
            TextField(
                value = newTaskContent.value,
                onValueChange = { newTaskContent.value = it },
                label = { Text("Task Content") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.tertiary
                ),
                onClick = {
                    if (newTaskName.value.isNotBlank()) {
                        coroutineScope.launch {
                            dao.insertTask(
                                TaskEntity(
                                    name = newTaskName.value,
                                    content = newTaskContent.value
                                )
                            )
                            navController.popBackStack()
                        }
                    }
                }
            ) {
                Text("Add", fontSize = 30.sp)
            }
        }
    }
}

