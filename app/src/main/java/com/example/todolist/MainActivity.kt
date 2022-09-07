package com.example.todolist

import CustomAdapter
import android.content.ContentValues
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var helper = MyDBHelper(applicationContext)
        var db = helper.readableDatabase
        var rs = db.rawQuery("SELECT * FROM USERS",null)

         var itemlist = arrayListOf<String>()
         var adapter = CustomAdapter(itemlist,this)
         var recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

       // recyclerView.layoutManager = GridLayoutManager(this,1)
         recyclerView.adapter = adapter

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

        // on below line we are creating a method to create item touch helper
        // method for adding swipe to delete functionality.
        // in this we are specifying drag direction and position to right
        // on below line we are creating a method to create item touch helper
        // method for adding swipe to delete functionality.
        // in this we are specifying drag direction and position to right
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                // this method is called
                // when the item is moved.
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // this method is called when we swipe our item to right direction.
                // on below line we are getting the item at a particular position.
                val deletedCourse: String =
                    itemlist.get(viewHolder.adapterPosition)

                db.delete("USERS","TASK=?", arrayOf(deletedCourse))

                // below line is to get the position
                // of the item at that position.
                val position = viewHolder.adapterPosition

                // this method is called when item is swiped.
                // below line is to remove item from our array list.
                itemlist.removeAt(viewHolder.adapterPosition)

                // below line is to notify our item is removed from adapter.
                adapter.notifyItemRemoved(viewHolder.adapterPosition)

                // below line is to display our snackbar with action.
                // below line is to display our snackbar with action.
                // below line is to display our snackbar with action.
                Snackbar.make(recyclerView, "Deleted " + deletedCourse, Snackbar.LENGTH_LONG)
                    .setAction(
                        "Undo",
                        View.OnClickListener {
                            // adding on click listener to our action of snack bar.
                            // below line is to add our item to array list with a position.
                            itemlist.add(position, deletedCourse)
                            var cv = ContentValues()
                            cv.put("TASK",deletedCourse)
                            db.insert("USERS",null,cv)
                            // below line is to notify item is
                            // added to our adapter class.
                            adapter.notifyItemInserted(position)
                        }).show()
            }
            // at last we are adding this
            // to our recycler view.
        }).attachToRecyclerView(recyclerView)
    }
}