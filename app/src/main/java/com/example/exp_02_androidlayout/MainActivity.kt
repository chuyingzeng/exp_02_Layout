package com.example.exp_02_androidlayout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class StudyTask(
    val id: Int,
    val title: String,
    val isCompleted: Boolean
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val transparent = android.graphics.Color.TRANSPARENT
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(transparent, transparent),
            navigationBarStyle = SystemBarStyle.light(transparent, 0x66000000)
        )
        setContent {
            MaterialTheme {
                StudyTaskScreen()
            }
        }
    }
}

@Composable
fun StudyTaskScreen() {
    val accentColor = Color(0xFFB91F29)
    val titleColor = Color(0xFF8D1B20)
    val backgroundColor = Color(0xFFF5F9FC)
    val textColor = Color(0xFF252525)
    val secondaryTextColor = Color(0xFF616569)
    val controlShape = RoundedCornerShape(8.dp)
    val focusManager = LocalFocusManager.current

    val taskInput = remember { TextFieldState() }
    val inputFocusRequester = remember { FocusRequester() }
    val listState = rememberLazyListState()
    var showBlankInputError by remember { mutableStateOf(false) }
    var nextTaskId by remember { mutableIntStateOf(4) }

    val tasks = remember {
        mutableStateListOf(
            StudyTask(1, "学习 Column 和 Row", true),
            StudyTask(2, "学习状态管理", false),
            StudyTask(3, "完成 Compose 实验", false)
        )
    }

    val completedCount = tasks.count { it.isCompleted }
    val hasInputError = showBlankInputError && taskInput.text.isBlank()

    fun addTask() {
        val title = taskInput.text.trim().toString()
        if (title.isEmpty()) {
            showBlankInputError = true
            inputFocusRequester.requestFocus()
            return
        }

        tasks.add(StudyTask(id = nextTaskId++, title = title, isCompleted = false))
        showBlankInputError = false
        focusManager.clearFocus()
        taskInput.clearText()
        listState.requestScrollToItem(tasks.lastIndex)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .safeDrawingPadding()
            .padding(start = 18.dp, top = 42.dp, end = 18.dp, bottom = 8.dp)
    ) {
        Text(
            text = "课程学习任务",
            fontSize = 34.sp,
            lineHeight = 44.sp,
            fontWeight = FontWeight.Bold,
            color = titleColor
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                state = taskInput,
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 56.dp)
                    .focusRequester(inputFocusRequester),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 16.sp,
                    lineHeight = 22.sp
                ),
                placeholder = {
                    Text(
                        text = "请输入学习任务",
                        fontSize = 16.sp,
                        lineHeight = 22.sp
                    )
                },
                isError = hasInputError,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                onKeyboardAction = { addTask() },
                lineLimits = TextFieldLineLimits.SingleLine,
                shape = controlShape,
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = textColor,
                    unfocusedTextColor = textColor,
                    focusedPlaceholderColor = secondaryTextColor,
                    unfocusedPlaceholderColor = secondaryTextColor,
                    focusedBorderColor = accentColor,
                    unfocusedBorderColor = Color(0xFFB9BEC3),
                    cursorColor = accentColor,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Button(
                onClick = ::addTask,
                modifier = Modifier.heightIn(min = 56.dp),
                shape = controlShape,
                contentPadding = PaddingValues(horizontal = 28.dp, vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = accentColor,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "添加",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 32.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            if (hasInputError) {
                Text(
                    text = "请输入任务名称后再添加",
                    modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
                    fontSize = 14.sp,
                    color = accentColor
                )
            }
        }

        Text(
            text = "已完成：$completedCount/${tasks.size}",
            fontSize = 16.sp,
            lineHeight = 22.sp,
            color = secondaryTextColor
        )

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = tasks,
                key = { it.id }
            ) { task ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 80.dp)
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(start = 6.dp, top = 8.dp, end = 4.dp, bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = task.isCompleted,
                        onCheckedChange = { checked ->
                            val index = tasks.indexOfFirst { it.id == task.id }
                            if (index != -1) {
                                tasks[index] = task.copy(isCompleted = checked)
                            }
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = accentColor,
                            uncheckedColor = secondaryTextColor,
                            checkmarkColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = task.title,
                        modifier = Modifier.weight(1f),
                        fontSize = 16.sp,
                        lineHeight = 22.sp,
                        color = if (task.isCompleted) secondaryTextColor else textColor,
                        textDecoration = if (task.isCompleted) {
                            TextDecoration.LineThrough
                        } else {
                            TextDecoration.None
                        }
                    )

                    TextButton(
                        onClick = {
                            val index = tasks.indexOfFirst { it.id == task.id }
                            if (index != -1) {
                                tasks.removeAt(index)
                            }
                        },
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "删除",
                            color = accentColor,
                            fontSize = 14.sp,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}
