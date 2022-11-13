package com.interview.square.feature_position_history.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.interview.square.core.domain.model.TwoDimensionPosition
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PositionHistoryScreen(viewModel: IPositionHistoryViewModel = getViewModel<PositionHistoryViewModel>(), navigateUp: () -> Unit) {
    val records by viewModel.history.collectAsState()

    Scaffold(
        topBar = {
            Column {
                TopAppBar(title = {
                    Text(text = "Last movements")
                }, navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Navigate back"
                        )
                    }
                })
                Divider()
            }
        }
    ) { padding ->
        LazyColumn(Modifier.padding(padding)) {
            stickyHeader {
                Row(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.primaryContainer), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "Date", style = MaterialTheme.typography.titleLarge)
                    Text("X", style = MaterialTheme.typography.titleLarge)
                    Text(text = "Y", style = MaterialTheme.typography.titleLarge)
                }
                Divider()
            }
            items(items = records, key = {
                it.timestamp
            }) { record ->
                when(record.position) {
                    is TwoDimensionPosition -> {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = record.getFormattedDate())
                            Text(text = record.position.x.toString())
                            Text(text = record.position.y.toString())
                        }
                    }
                }
                Divider()
            }

        }
    }
}