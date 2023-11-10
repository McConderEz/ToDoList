import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ru.rustamov.rustamov_2_16.Task

class DbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "app.db"
        private const val DATABASE_VERSION = 1

        // Определение структуры таблицы
        private const val TABLE_NAME = "tasks"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_TIME = "time"
        private const val COLUMN_DATE = "date"

        // Запрос на создание таблицы
        private const val CREATE_TABLE_QUERY = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_TITLE TEXT, $COLUMN_DESCRIPTION TEXT, $COLUMN_TIME TEXT, $COLUMN_DATE TEXT)"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Добавление задачи в базу данных
    fun addTask(task: Task) {
        val values = ContentValues()
        values.put(COLUMN_TITLE, task.title)
        values.put(COLUMN_DESCRIPTION, task.description)
        values.put(COLUMN_TIME, task.time)
        values.put(COLUMN_DATE, task.date)

        val db = writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    //Удаление задачи из БД
    fun deleteTaskById(id: Int): Int {
        val db = this.writableDatabase
        val selection = "$COLUMN_ID = ?"
        val selectionArgs = arrayOf(id.toString())
        val deletedRows = db.delete(TABLE_NAME, selection, selectionArgs)
        db.close()
        return deletedRows
    }
    fun deleteTask(title: String): Int {
        val db = this.writableDatabase
        val selection = "$COLUMN_TITLE = ?"
        val selectionArgs = arrayOf(title)
        val deletedRows = db.delete(TABLE_NAME, selection, selectionArgs)
        db.close()
        return deletedRows
    }

    @SuppressLint("Range")
    fun getAllTasks(): ArrayList<Task> {
        val tasks = ArrayList<Task>()
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
                val title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE))
                val description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION))
                val date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE))
                val time = cursor.getString(cursor.getColumnIndex(COLUMN_TIME))

                val task = Task(title, description, date, time)
                task.id = id
                tasks.add(task)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return tasks
    }
}