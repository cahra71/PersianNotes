package com.mihan.persiannotes.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val notebookId: Long,
    val imagePath: String,
    val recognizedText: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
