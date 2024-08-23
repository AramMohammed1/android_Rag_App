package com.example.myapplication

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.ui.screens.ChatScreen
import com.example.myapplication.viewModel.FileUploadViewModel
import com.example.myapplication.viewModel.RagAppViewModel

@Composable
fun RagApp(

){
    val ragAppViewModel: RagAppViewModel = viewModel(factory = RagAppViewModel.Factory)
    val fileUploadViewModel: FileUploadViewModel = viewModel(factory = FileUploadViewModel.Factory)
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        ChatScreen(ragAppViewModel = ragAppViewModel,fileUploadViewModel=fileUploadViewModel,modifier = Modifier.padding(innerPadding))
    }
}