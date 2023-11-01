package com.github.bowlbird.sig_appdev_firebase

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.bowlbird.sig_appdev_firebase.ui.theme.FirebaseTheme
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.database
import com.google.firebase.initialize

class FirebaseApplication : Application() {
    lateinit var firebaseRepository: FirebaseRepository

    override fun onCreate() {
        super.onCreate()
        firebaseRepository = FirebaseRepository(
            FirebaseDataSource(applicationContext)
        )
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FirebaseTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Layout()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Layout(modifier: Modifier = Modifier) = Column {
    val viewModel: FirebaseViewModel = viewModel(factory = FirebaseViewModel.Factory)
    val state = viewModel.firebaseUiState.collectAsState().value

    TopBar()

    LazyColumn(Modifier.padding(10.dp).shadow(2.dp).fillMaxSize()) {
        state.messages.forEach {
            item {
                Text(modifier = Modifier.padding(10.dp), text = it, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(modifier: Modifier = Modifier) = Column(modifier) {
    val viewModel: FirebaseViewModel = viewModel(factory = FirebaseViewModel.Factory)

    var text by remember { mutableStateOf("") }
    TextField(modifier = Modifier.fillMaxWidth(), value = text, onValueChange = {
        text = it
    })
    Button(modifier = Modifier.fillMaxWidth(), onClick = {viewModel.pushMessage(text)}, shape = RectangleShape) {
        Text("Submit")
    }
}