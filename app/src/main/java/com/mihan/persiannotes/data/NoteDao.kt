package com.mihan.persiannotes.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NoteDao {
    @Insert
    suspend fun insert(note: Note): Long

    @Query("SELECT * FROM Note WHERE notebookId = :nbId ORDER BY createdAt DESC")
    suspend fun getForNotebook(nbId: Long): List<Note>
}
