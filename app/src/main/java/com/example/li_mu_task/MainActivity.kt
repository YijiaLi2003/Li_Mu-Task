package com.example.li_mu_task

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskCompletionAppTheme {
                TaskCompletionApp()
            }
        }
    }
}

val Blue200 = Color(0xFF90CAF9)
val Blue500 = Color(0xFF2196F3)
val Blue700 = Color(0xFF1976D2)
val Teal200 = Color(0xFF03DAC5)
val White = Color(0xFFFFFFFF)
val Black = Color(0xFF000000)
val LightGray = Color(0xFFEEEEEE)
val DarkGray = Color(0xFF333333)
val DarkerGray = Color(0xFF121212)

val Typography = Typography(
    titleLarge = androidx.compose.ui.text.TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    ),
    bodyLarge = androidx.compose.ui.text.TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp
    ),
    bodyMedium = androidx.compose.ui.text.TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
)


private val LightColors = lightColorScheme(
    primary = Blue500,
    onPrimary = White,
    primaryContainer = Blue700,
    onPrimaryContainer = White,
    secondary = Teal200,
    onSecondary = Black,
    background = White,
    onBackground = Black,
    surface = White,
    onSurface = Black,
    surfaceVariant = LightGray,
    onSurfaceVariant = DarkGray,
)

private val DarkColors = darkColorScheme(
    primary = Blue200,
    onPrimary = Black,
    primaryContainer = Blue700,
    onPrimaryContainer = White,
    secondary = Teal200,
    onSecondary = Black,
    background = Black,
    onBackground = White,
    surface = DarkGray,
    onSurface = White,
    surfaceVariant = DarkerGray,
    onSurfaceVariant = LightGray,
)

@Composable
fun TaskCompletionAppTheme(
    useDarkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (useDarkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}

data class Task(
    val description: String,
    var isCompleted: Boolean = false
)

@Composable
fun TaskCompletionApp(modifier: Modifier = Modifier) {
    var taskDescription by remember { mutableStateOf("") }
    var taskList by remember { mutableStateOf(listOf<Task>()) }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Text(
            text = "Task Completion App",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = taskDescription,
            onValueChange = { taskDescription = it },
            label = { Text("Enter task description") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (taskDescription.isNotBlank()) {
                    val newTask = Task(description = taskDescription)
                    taskList = taskList + newTask
                    taskDescription = ""
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Add Task")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (taskList.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                itemsIndexed(taskList) { index, task ->
                    TaskItem(
                        task = task,
                        onCheckedChange = { isChecked ->
                            val updatedTask = task.copy(isCompleted = isChecked)
                            taskList = taskList.toMutableList().also {
                                it[index] = updatedTask
                            }
                        }
                    )
                    HorizontalDivider()
                }
            }
        } else {
            Text(
                text = "No tasks added.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        if (taskList.any { it.isCompleted }) {
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    taskList = taskList.filter { !it.isCompleted }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Clear Completed")
            }
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Checkbox(
            checked = task.isCompleted,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.primary
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = task.description,
            style = MaterialTheme.typography.bodyLarge.copy(
                textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                color = if (task.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
            )
        )
    }
}


