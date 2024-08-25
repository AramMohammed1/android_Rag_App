package com.example.myapplication.viewModel
import android.app.Application
import android.net.Uri
import android.provider.OpenableColumns
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myapplication.RagApplication
import com.example.myapplication.database.RagResponseRepo
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

class FileUploadViewModel(application: Application, private val ragResponseRepo: RagResponseRepo) : AndroidViewModel(application) {
    val selectedFileUris = mutableStateOf<List<Uri>>(emptyList())
    val uploadStatus = mutableStateOf<String?>(null)
    val fileNames = mutableStateOf<List<String>>(emptyList())

    fun updateSelectedFileUris(uris: List<Uri>) {
        selectedFileUris.value = uris
        fileNames.value = uris.map { getFileNameFromUri(it)!! }
        uploadFiles(fileNames.value,uris) { success ->
        if (success) {uploadStatus.value ="Upload Successful"} else uploadStatus.value = "Upload Failed"
        }
    }

    fun uploadFiles(fileNames:List<String?>,uris: List<Uri>, onUploadComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                var files:List<File> = listOf()

                uris.map{
                    uri->
                    val file = getFileFromUri(uri)
                    files = files + file
                }
                files.let {

                    val fileParts = files.map { file ->
                        val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
                        MultipartBody.Part.createFormData("files", file.name, requestFile)
                    }

                    val response = ragResponseRepo.uploadFiles(fileParts)

                    onUploadComplete(response.isSuccessful)
                }
            } catch (e: Exception) {
                onUploadComplete(false)
            }
        }
    }

    private fun getFileFromUri(uri: Uri): File {
        val context = getApplication<Application>().applicationContext
        val fileName = getFileNameFromUri(uri)

        val file = File(context.cacheDir, fileName ?: "temp_file")
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()

        return file
    }

    private fun getFileNameFromUri(uri: Uri): String? {
        val context = getApplication<Application>().applicationContext
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.moveToFirst()
        val fileName = cursor?.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
        cursor?.close()
        return fileName

    }


    companion object{
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as RagApplication)
                val ragResponseRepo = application.container.ragResponseRepo
                FileUploadViewModel(ragResponseRepo = ragResponseRepo,application = application)
            }
        }
    }


}
