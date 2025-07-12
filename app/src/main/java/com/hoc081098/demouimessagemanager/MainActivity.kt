package com.hoc081098.demouimessagemanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hoc081098.demouimessagemanager.ui.theme.DemoUiMessageManagerTheme
import kotlin.uuid.ExperimentalUuidApi

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            DemoUiMessageManagerTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = {
                                Text(
                                    text = "Demo UI Message Manager",
                                )
                            }
                        )
                    },
                ) { innerPadding ->
                    MainContent(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalUuidApi::class)
@Composable
private fun MainContent(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel(),
) {
    var lastEvent by remember { mutableStateOf(null as MainEvent?) }

    CollectUiMessageFlowEffect(uiMessageFlow = viewModel.uiMessageFlow) { message ->
        when (val event = message.message) {
            is MainEvent.Success -> {
                lastEvent = event
                viewModel.clearMessage(id = message.id)
            }

            is MainEvent.Error -> {
                lastEvent = event
                viewModel.clearMessage(id = message.id)
            }
        }
    }

    Surface(modifier = modifier) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Event: $lastEvent",
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = viewModel::performLogic
                ) {
                    Text(
                        text = "Perform Logic",
                    )
                }
            }
        }
    }
}
