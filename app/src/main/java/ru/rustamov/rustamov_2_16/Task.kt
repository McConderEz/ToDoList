package ru.rustamov.rustamov_2_16

class Task(val title: String,val description: String,val time: String,val date: String ) {


    override fun toString(): String {
        return title.toString() + " " + description.toString() + " " +
                time.toString() + " " + date.toString();
    }
}