package ru.rustamov.rustamov_2_16

import DbHelper
import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {

    lateinit var listView: ListView
    lateinit var title: TextView
    lateinit var addButton: FloatingActionButton
    lateinit var changeLang: FloatingActionButton
    lateinit var deleteButton: FloatingActionButton
    lateinit var editButton: FloatingActionButton

    //lateinit var adapter: ArrayAdapter<String>
    private lateinit var adapter: TaskAdapter

    private val todos: ArrayList<Task> = ArrayList()
    private val CHANNEL_ID = "channel_id_01"
    private val notificationId = 101

    private var selectedTask: Int? = null

    var isLangChanged: Boolean = false
    val app = MyApp()

    lateinit var dbHelper: DbHelper



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dbHelper = DbHelper(this)
        listView = findViewById(R.id.listView)


        title = findViewById(R.id.textView)
        addButton = findViewById(R.id.floatingActionButton2)
        deleteButton = findViewById(R.id.floatingActionButton4)
        editButton = findViewById(R.id.floatingActionButton3)
        changeLang = findViewById(R.id.floatingActionButton)

        adapter = TaskAdapter(this, todos)
        listView.adapter = adapter

        showAllTasks()
        createNotificationChannel()
        ChangeLanguage()
        addButton.setOnClickListener{
            val addItemIntent = Intent(this,SecondActivity::class.java)
            startActivityForResult(addItemIntent, 1)
        }

        editButton.setOnClickListener{
            val editItemIntent = Intent(this, EditActivity::class.java)
            editItemIntent.putExtra("taskId", selectedTask!!)
            startActivityForResult(editItemIntent, 2)
        }

        listView.setOnItemLongClickListener { parent, view, position, id ->
            selectedTask = adapter.getItem(position).id
            true
        }



        deleteButton.setOnClickListener {
            selectedTask?.let { task ->
                dbHelper.deleteTaskById(selectedTask!!)
                selectedTask = null
                showAllTasks()
            }
        }

    }

    //Отображение всех задач
    private fun showAllTasks() {
        todos.clear()

        val allTasks = dbHelper.getAllTasks()
        for (task in allTasks) {
            todos.add(task)
        }
        adapter.notifyDataSetChanged()
    }

    //Смена языка
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

    //Событие, которое получает введённые значение от пользователя со второго макета
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            val titleDb = data.getStringExtra("title") ?: ""
            val descriptionDb = data.getStringExtra("description") ?: ""
            val timeDb = data.getStringExtra("time")?: ""
            val dateDb = data.getStringExtra("date")?: ""

            addItem(titleDb,descriptionDb,timeDb,dateDb)
            /*newItem?.let {
                addItem(it)
            }*/
        }else if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            // Редактирование задачи
            val taskId = selectedTask!!
            if (taskId != -1) {
                val newTitle = data.getStringExtra("title") ?: ""
                val newDescription = data.getStringExtra("description") ?: ""
                val newTime = data.getStringExtra("time") ?: ""
                val newDate = data.getStringExtra("date") ?: ""
                editTask(taskId, newTitle, newDescription, newTime, newDate)
            }
        }
    }

    fun editTask(taskId: Int, newTitle: String, newDescription: String, newTime: String, newDate: String) {
        dbHelper = DbHelper(this)
        val updatedTask = Task(newTitle, newDescription, newTime, newDate)
        updatedTask.id = taskId
        dbHelper.updateTask(updatedTask)
        showAllTasks()
        selectedTask = null
    }

    //Добавление задачи в базу данных
    fun addItem(title: String, description: String, time: String, date: String) {
        val task = Task(title,description,time,date)
        val db = DbHelper(this)
        db.addTask(task)
        showAllTasks()
        adapter.notifyDataSetChanged()
        if(isLangChanged) {
            Toast.makeText(this, "Added item", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(this, "Элемент добавлен", Toast.LENGTH_SHORT).show()
        }

        sendNotification()

    }

    //Создание уведомления о добавлении данных в таблицу
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

    //Отправка сообщения о добавления данных в таблицу
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