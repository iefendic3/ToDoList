package com.example.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var itemlist = arrayListOf<String>()
        var adapter = ArrayAdapter<String> (this, android.R.layout.simple_list_item_multiple_choice, itemlist)

     val button = findViewById<Button>(R.id.button1)
     button.setOnClickListener{
         val builder = AlertDialog.Builder(this).create()
         val view = layoutInflater.inflate(R.layout.popup,null)
         val  buttonAdd = view.findViewById<Button>(R.id.button2)
         builder.setView(view)
         var editText = findViewById<EditText>(R.id.editText)
         val listView = findViewById<ListView>(R.id.listView)
         listView.adapter = adapter
         buttonAdd.setOnClickListener {

             itemlist.add(editText.text.toString())
             adapter.notifyDataSetChanged()
             // This is because every time when you add the item the input space or the edit text space will be cleared
             editText.text.clear()

         }
         builder.setCanceledOnTouchOutside(true)
         builder.show()
     }
    }
}