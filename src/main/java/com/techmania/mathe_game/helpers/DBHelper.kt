package com.techmania.mathe_game.helpers

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DBHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) { //create DB
        val createTableSql = ("CREATE TABLE " + TABLE_HIGHSCORE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_SCORE + " INTEGER,"
                + COLUMN_DATE + " INTEGER" //"YYYY-MM-DD HH:MM:SS.SSS"
                + ");")
        db.execSQL(createTableSql)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_HIGHSCORE")
        onCreate(db)
    }

    fun addHighScore(highscore: Int) {
        /*
        gets highscore (as int) and lives (as int) and stores them with the current time in the DB
         */
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_SCORE, highscore)
        values.put(COLUMN_DATE, System.currentTimeMillis())

        db.insert(TABLE_HIGHSCORE, null, values)
        db.close()
    }

    fun getAllScores(): ArrayList<HashMap<String, Int>> {
        /*
        Returns an ArrayList with Hashmaps containing keys (as Strings) and values (as Integers)
         */
        val scoresList: ArrayList<HashMap<String, Int>> = ArrayList()
        val selectQuery =
            "SELECT $COLUMN_ID, $COLUMN_SCORE, $COLUMN_DATE FROM $TABLE_HIGHSCORE"
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                //read data from DB and store it in the list
                val hashmap: HashMap<String, Int> = HashMap()
                val id: Int = cursor.getInt(0)
                val score: Int = cursor.getInt(1)
                val date: Int = cursor.getInt(2)
                hashmap["score"] = score
                hashmap["id"] = id
                hashmap["date"] = date
                scoresList.add(hashmap)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return scoresList
    }

    companion object {
        private const val DATABASE_VERSION = 2
        private const val DATABASE_NAME = "highscore.db"
        private const val TABLE_HIGHSCORE = "highscore"
        private const val COLUMN_ID = "id"
        private const val COLUMN_SCORE = "score"
        private const val COLUMN_DATE = "date"
    }
}