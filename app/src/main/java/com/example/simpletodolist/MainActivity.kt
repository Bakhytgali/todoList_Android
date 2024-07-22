package com.example.simpletodolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simpletodolist.model.TodoModel
import com.example.simpletodolist.ui.theme.SimpleToDoListTheme
import com.example.simpletodolist.ui.theme.buttonColor
import com.example.simpletodolist.ui.theme.cardBackground
import com.example.simpletodolist.ui.theme.globalBackgroundColor
import com.example.simpletodolist.ui.theme.primaryTextColor
import com.example.simpletodolist.ui.theme.zain

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimpleToDoListTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    TodoList()
                }
            }
        }
    }
}

@Composable
fun TodoList(modifier: Modifier = Modifier) {
    val todoListArray = remember {
        mutableStateListOf<TodoModel>()
    }
    val addTodo: (newTodo: TodoModel) -> Unit = {newTodo ->
        todoListArray.add(newTodo)
    }

    var createNewTodoIsVisible by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(globalBackgroundColor)
    ) {
        if(todoListArray.isNotEmpty()) {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = modifier.fillMaxSize()
            ) {
                items(todoListArray) { todo ->
                    TodoListItem(
                        todo = todo,
                        isDoneChanged = { updatedTodo: TodoModel ->
                            val index = todoListArray.indexOf(todo)
                            todoListArray[index] = updatedTodo
                        }
                    )
                }
            }
        } else {
            Text(
                "To-Do List Is Empty",
                color = primaryTextColor,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp,
                fontFamily = zain,
                modifier = modifier.align(Alignment.Center)
            )
        }

        if(createNewTodoIsVisible) {
            CreateNewTodo(
                addTodo,
                onDismiss = {
                    createNewTodoIsVisible = false
                }
            )
        }

        FloatingActionButton(
            onClick = {
                createNewTodoIsVisible = true
            },
            modifier = modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = buttonColor,
            contentColor = globalBackgroundColor
            ) {
            Icon(Icons.Filled.Add, "Add Todo Item")
        }
    }
}

@Composable
fun CreateNewTodo(
    addNewTodo: (todo: TodoModel) -> Unit,
    onDismiss: () -> Unit
) {
    var newTodoTitle by remember {
        mutableStateOf("")
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Create New",
                fontSize = 28.sp,
                fontFamily = zain,
                color = primaryTextColor,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            TextField(
                value = newTodoTitle,
                onValueChange = { newValue: String ->
                    newTodoTitle = newValue
                },
                colors = TextFieldDefaults.colors(
                    disabledContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = primaryTextColor
                ),
                label = {
                    Text("Enter new item")
                }
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    val newTodo = TodoModel(newTodoTitle, false)
                    addNewTodo(newTodo)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = buttonColor,
                    containerColor = Color.Transparent
                )
            ) {
                Text(
                    "Add",
                    fontFamily = zain,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
        },
        dismissButton = {
            Button(
                onClick = { onDismiss() },
                colors = ButtonDefaults.buttonColors(
                    contentColor = primaryTextColor,
                    containerColor = Color.Transparent
                )
            ) {
                Text(
                    "Cancel",
                    fontFamily = zain,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
        },
        containerColor = cardBackground
    )
}

@Composable
fun TodoListItem(
    todo: TodoModel,
    modifier: Modifier = Modifier,
    isDoneChanged: (TodoModel) -> Unit
) {
    Card(
        modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(2.dp))
            .padding(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardBackground
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.padding(vertical = 10.dp)
        ) {
            Text(
                todo.title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = zain,
                letterSpacing = 1.1.sp,
                modifier = modifier.padding(start = 15.dp),
                color = primaryTextColor
            )

            Spacer(modifier = modifier.weight(1f))

            Checkbox(
                checked = todo.isDone,
                onCheckedChange = { isChecked ->
                    isDoneChanged(todo.copy(isDone = isChecked))
                },
                colors = CheckboxDefaults.colors(
                    uncheckedColor = buttonColor,
                    checkmarkColor = buttonColor
                )
            )
        }
    }
}

@Preview
@Composable
private fun PreviewTodoList() {
    TodoList()
}
