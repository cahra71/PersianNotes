package com.mihan.persiannotes

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mihan.persiannotes.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recycler.layoutManager = LinearLayoutManager(this)
        val notebooks = listOf(NotebookDisplay(1, "دفترچهٔ نمونه", "2025/10/08"))
        binding.recycler.adapter = com.mihan.persiannotes.adapters.NotebookAdapter(notebooks) { nb ->
            val i = Intent(this, EditorActivity::class.java)
            i.putExtra("notebookId", nb.id)
            startActivity(i)
        }

        binding.fabAdd.setOnClickListener { startActivity(Intent(this, NotebookActivity::class.java)) }
        binding.btnAbout.setOnClickListener { startActivity(Intent(this, AboutActivity::class.java)) }
    }
}

data class NotebookDisplay(val id: Long, val title: String, val createdAt: String)
