package com.own.smartsubtitle.domain.usecases

import android.content.Context
import android.net.Uri
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class ReadFileUseCase(private val context: Context) {
    @Throws(IOException::class)
    operator fun invoke(uri: Uri): String {
        val stringBuilder = StringBuilder()
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                var line: String? = reader.readLine()
                while (line != null) {
                    stringBuilder.append(line + "\n")
                    line = reader.readLine()
                }
            }
        }
        return stringBuilder.toString()
    }
}