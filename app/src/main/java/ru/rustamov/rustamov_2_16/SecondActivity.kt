package ru.rustamov.rustamov_2_16

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import ru.rustamov.rustamov_2_16.R.id.titleBox

class SecondActivity : AppCompatActivity() {

    lateinit var titleOfActivity: TextView

    lateinit var title: EditText
    lateinit var description:EditText
    lateinit var time:EditText
    lateinit var date:EditText

    lateinit var saveButton:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        titleOfActivity = findViewById(R.id.textView3)
        title = findViewById(R.id.titleBox)
        description = findViewById(R.id.descriptionBox)
        time = findViewById(R.id.timeBox)
        date = findViewById(R.id.dateBox)
        saveButton = findViewById(R.id.saveButton)

        ChangeLanguage()

        saveButton.setOnClickListener{
            val result: String
            result = title.text.toString() + "|" + description.text.toString() + "|" + time.text.toString() + "|" + date.text.toString()
            val titleDb: String = title.text.toString()
            val descriptionDb:String = description.text.toString()
            val timeDb:String = time.text.toString()
            val dateDb:String = date.text.toString()

            val addItemIntent = Intent()
            addItemIntent.putExtra("title", titleDb)
            addItemIntent.putExtra("description", descriptionDb)
            addItemIntent.putExtra("timeDb", timeDb)
            addItemIntent.putExtra("dateDb", dateDb)

            setResult(Activity.RESULT_OK, addItemIntent)
            finish()
        }

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_main)
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_main)
        }

        // Обновите ссылки на ваши элементы интерфейса, если необходимо
        titleOfActivity = findViewById(R.id.textView3)
        title = findViewById(R.id.titleBox)
        description = findViewById(R.id.descriptionBox)
        time = findViewById(R.id.timeBox)
        date = findViewById(R.id.dateBox)
        saveButton = findViewById(R.id.saveButton)
    }

    fun ChangeLanguage(){
        if(MyApp.isLangChanged){
            titleOfActivity.setText("Adding item")
            title.setHint("Enter title")
            description.setHint("Enter description")
            time.setHint("Enter time")
            date.setHint("Enter date")
            saveButton.setText("Save")
        }
        else{
            titleOfActivity.setText("Добавление записи")
            title.setHint("Введите название")
            description.setHint("Введите описание")
            time.setHint("Введите время")
            date.setHint("Введите дату")
            saveButton.setText("Сохранить")
        }
    }
}