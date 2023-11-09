package ru.rustamov.rustamov_2_16

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AppOpsManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.content.ContentValues

class MainActivity : AppCompatActivity() {

    lateinit var listView: ListView
    lateinit var title: TextView
    lateinit var addButton: FloatingActionButton
    lateinit var changeLang: FloatingActionButton

    lateinit var adapter: ArrayAdapter<String>

    private val todos: ArrayList<String> = ArrayList()
    private val CHANNEL_ID = "channel_id_01"
    private val notificationId = 101

    var isLangChanged: Boolean = false
    val app = MyApp()

    //lateinit var databaseHelper: DatabaseHelper
    //lateinit var db: SQLiteDatabase



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //databaseHelper = DatabaseHelper(this)
        //db = databaseHelper.writableDatabase

        listView = findViewById(R.id.listView)
        title = findViewById(R.id.textView)
        addButton = findViewById(R.id.floatingActionButton2)
        changeLang = findViewById(R.id.floatingActionButton)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,todos)
        listView.adapter = adapter






        createNotificationChannel()
        ChangeLanguage()
        addButton.setOnClickListener{
            val addItemIntent = Intent(this,SecondActivity::class.java)
            startActivityForResult(addItemIntent, 1)
        }



    }

    fun ChangeLanguage(){
        changeLang.setOnClickListener{
            if(!isLangChanged) {
                title.setText("ToDo List")
                isLangChanged = true
                MyApp.isLangChanged = true
            }
            else{
                title.setText("Список дел")
                isLangChanged = false
                MyApp.isLangChanged = false
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            val titleDb = data.getStringExtra("titleDb") ?: ""
            val descriptionDb = data.getStringExtra("descriptionDb") ?: ""
            val timeDb = data.getStringExtra("timeDb")?: ""
            val dateDb = data.getStringExtra("dateDb")?: ""

            addItem(titleDb,descriptionDb,timeDb,dateDb)
            /*newItem?.let {
                addItem(it)
            }*/
        }
    }


    fun addItem(title: String, description: String, time: String, date: String) {

        adapter.notifyDataSetChanged()
        if(isLangChanged) {
            Toast.makeText(this, "Added item", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(this, "Элемент добавлен", Toast.LENGTH_SHORT).show()
        }

        sendNotification()

    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Notification title"
            val descriptionText = "Added item"
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(CHANNEL_ID,name,importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

        }
    }

    @SuppressLint("MissingPermission")
    private fun sendNotification(){

        if(isLangChanged) {
            var builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.addfileinterfacesymbolofpapersheetwithtextlinesandplussign_79821)
                .setContentTitle("Daily schedule")
                .setContentText("Added item")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            with(NotificationManagerCompat.from(this)) {
                notify(notificationId, builder.build())
            }
        }else {
            var builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.addfileinterfacesymbolofpapersheetwithtextlinesandplussign_79821)
                .setContentTitle("Распорядок дня")
                .setContentText("Элемент добавлен")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            with(NotificationManagerCompat.from(this)) {
                notify(notificationId, builder.build())
            }
        }

    }

}