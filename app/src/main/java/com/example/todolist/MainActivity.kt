package com.example.todolist

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var helper = MyDBHelper(applicationContext)
        var db = helper.readableDatabase
        var rs = db.rawQuery("SELECT * FROM USERS",null)

         var itemlist = arrayListOf<String>()
         var adapter = ArrayAdapter<String> (this, android.R.layout.simple_list_item_multiple_choice, itemlist)
         var listView = findViewById<ListView>(R.id.listView)
         listView.adapter = adapter
        while(rs.moveToNext()) itemlist.add(rs.getString(1))
         val button = findViewById<FloatingActionButton>(R.id.button1)

         button.setOnClickListener{
         val builder = AlertDialog.Builder(this).create()
         val view = layoutInflater.inflate(R.layout.popup,null)
         val  buttonAdd = view.findViewById<Button>(R.id.button2)
         builder.setView(view)
             var editText = view.findViewById<EditText>(R.id.editText)

         buttonAdd.setOnClickListener {
             var cv = ContentValues()
             cv.put("TASK",editText.text.toString())
             db.insert("USERS",null,cv)
             if(editText.text.toString()!=null && editText.text.toString().isNotBlank() && !itemlist.contains(editText.text.toString())) {
                 itemlist.add(editText.text.toString())
                 adapter.notifyDataSetChanged()

                 //listView.adapter = adapter


                 // This is because every time when you add the item the input space or the edit text space will be cleared
                 editText.text.clear()
                 builder.cancel()
             }
             else{
                 editText.setError("Task already exists!")
             }

         }
         builder.setCanceledOnTouchOutside(true)
         builder.show()
     }
    }
}