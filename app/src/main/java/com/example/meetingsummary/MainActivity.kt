package com.example.meetingsummary

import android.media.MediaRecorder
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.example.meetingsummary.ui.theme.MeetingsummaryTheme
import java.io.IOException

//import androidx.appcompat.app.AppCompatActivity


class MainActivity : ComponentActivity() {
    private val audioRecorder = AudioRecorder()  // 使用 remember 創建 Composable 可訪問的錄音對象
    private lateinit var transcribeButton: Button
    private lateinit var transcribeResult: TextView
    val YOUR_KEY = ""
    val URL = "https://api.openai.com/v1/completions"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transcribeButton = findViewById(R.id.transcribeButton)
        transcribeResult = findViewById(R.id.transcribeResult)

        transcribeButton.setOnClickListener {
            // 在這裡添加語音轉錄代碼，並使用API Key
            val transcribedText = transcribeAudio(apiKey)
            transcribeResult.text = transcribedText
        }

        private fun transcribeAudio(apiKey: String): String {
            // 在這裡添加語音轉錄代碼，並使用API Key
            // 調用OpenAI API 執行語音轉錄
            // 返回轉錄的文本
            return "這是語音轉錄的文本"
        }

        setContent {
            MeetingsummaryTheme {
                val navController = rememberNavController()

                // Create a NavHost to navigate between pages
                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        HomePage(navController = navController)
                    }
                    composable("recordpage") {
                        RecordPage(navController = navController, audioRecorder = audioRecorder)
                    }
                    composable("uploadpage") {
                        UploadPage(navController = navController)
                    }
                    composable("transcribepage") {
                        TranscribePage(navController = navController)
                    }
                    composable("summarypage") {
                        SummaryPage(navController = navController)
                    }
                }
            }
        }
    }
}

// AudioRecorder
class AudioRecorder {
    private var mediaRecorder: MediaRecorder? = null
    private var isRecording = false

    // 準備錄音
    fun startRecording(outputFilePath: String) {
        if (isRecording) {
            stopRecording()
        }

        mediaRecorder = MediaRecorder()
        mediaRecorder?.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(outputFilePath)

            try {
                prepare()
                start()
                isRecording = true
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    // 停止錄音
    fun stopRecording() {
        mediaRecorder?.apply {
            if (isRecording) {
                try {
                    stop()
                } catch (e: IllegalStateException) {
                    e.printStackTrace()
                }
                release()
                isRecording = false
            }
        }
        mediaRecorder = null
    }
}

@Composable
fun HomePage(navController: NavController) {
    val buttonModifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .height(IntrinsicSize.Max) // 平分剩餘空白區域

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top // 垂直方向上等距分布
    ) {
        // Top space
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Sidebar launcher
            IconButton(
                onClick = { /* Handle sidebar button click here */ },
                modifier = Modifier
                    .size(50.dp) // 設置IconButton的大小
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = null
                )
            }
            // House Icon Button to navigate to the home page
            IconButton(
                onClick = { navController.navigate("home") },
                modifier = Modifier
                    .size(50.dp) // 設置House Icon的大小
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = null
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween // 垂直方向上等距分布
        ) {

            Spacer(modifier = Modifier.height(16.dp))
            // Buttons
            Button(
                onClick = { navController.navigate("recordpage") },
                modifier = buttonModifier,
                contentPadding = PaddingValues(16.dp)
            ) {
                Text("錄音", fontSize = 30.sp) // 增加字體大小
            }
            //Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate("uploadpage") },
                modifier = buttonModifier,
                contentPadding = PaddingValues(16.dp)
            ) {
                Text("上傳", fontSize = 30.sp) // 增加字體大小
            }
            //Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate("transcribepage") },
                modifier = buttonModifier,
                contentPadding = PaddingValues(16.dp)
            ) {
                Text("轉譯", fontSize = 30.sp) // 增加字體大小
            }
            //Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate("summarypage") },
                modifier = buttonModifier,
                contentPadding = PaddingValues(16.dp)
            ) {
                Text("摘要", fontSize = 30.sp) // 增加字體大小
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun RecordPage(navController: NavController, audioRecorder: AudioRecorder) {

    val buttonModifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .height(IntrinsicSize.Max) // 平分剩餘空白區域

    var isRecording by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top // 垂直方向上等距分布
    ) {
        // Top space
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Sidebar launcher
            IconButton(
                onClick = { /* Handle sidebar button click here */ },
                modifier = Modifier
                    .size(50.dp) // 設置IconButton的大小
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = null
                )
            }
            // House Icon Button to navigate to the home page
            IconButton(
                onClick = { navController.navigate("home") },
                modifier = Modifier
                    .size(50.dp) // 設置House Icon的大小
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = null
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween // 垂直方向上等距分布
        ) {
            Spacer(modifier = Modifier.height(200.dp))

            Button(
                onClick = {
                    isRecording = !isRecording
                    if (isRecording) {
                        // Start recording
                        audioRecorder.startRecording("your_output_file_path")
                    } else {
                        // Stop recording
                        audioRecorder.stopRecording()
                    }
                },
                modifier = buttonModifier,
                contentPadding = PaddingValues(16.dp)
            ) {
                Text(if (isRecording) "停止錄音" else "開始錄音", fontSize = 30.sp)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }

    }
}

@Composable
fun UploadPage(navController: NavController) {
    val buttonModifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .height(IntrinsicSize.Max) // 平分剩餘空白區域

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top // 垂直方向上等距分布
    ) {
        // Top space
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Sidebar launcher
            IconButton(
                onClick = { /* Handle sidebar button click here */ },
                modifier = Modifier
                    .size(50.dp) // 設置IconButton的大小
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = null
                )
            }
            // House Icon Button to navigate to the home page
            IconButton(
                onClick = { navController.navigate("home") },
                modifier = Modifier
                    .size(50.dp) // 設置House Icon的大小
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = null
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween // 垂直方向上等距分布
        ) {
            Spacer(modifier = Modifier.height(200.dp))
        }

    }
}

@Composable
fun TranscribePage(navController: NavController) {
    val buttonModifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .height(IntrinsicSize.Max) // 平分剩餘空白區域

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top // 垂直方向上等距分布
    ) {
        // Top space
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Sidebar launcher
            IconButton(
                onClick = { /* Handle sidebar button click here */ },
                modifier = Modifier
                    .size(50.dp) // 設置IconButton的大小
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = null
                )
            }
            // House Icon Button to navigate to the home page
            IconButton(
                onClick = { navController.navigate("home") },
                modifier = Modifier
                    .size(50.dp) // 設置House Icon的大小
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = null
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween // 垂直方向上等距分布
        ) {
            Spacer(modifier = Modifier.height(200.dp))
        }

    }
}

@Composable
fun SummaryPage(navController: NavController) {
    val buttonModifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .height(IntrinsicSize.Max) // 平分剩餘空白區域

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top // 垂直方向上等距分布
    ) {
        // Top space
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Sidebar launcher
            IconButton(
                onClick = { /* Handle sidebar button click here */ },
                modifier = Modifier
                    .size(50.dp) // 設置IconButton的大小
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = null
                )
            }
            // House Icon Button to navigate to the home page
            IconButton(
                onClick = { navController.navigate("home") },
                modifier = Modifier
                    .size(50.dp) // 設置House Icon的大小
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = null
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween // 垂直方向上等距分布
        ) {
            Spacer(modifier = Modifier.height(200.dp))
        }

    }
}