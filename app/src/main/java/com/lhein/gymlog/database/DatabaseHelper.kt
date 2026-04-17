package com.lhein.gymlog.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "gymlog.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_WORKOUTS = "workouts"
        const val COLUMN_ID = "id"
        const val COLUMN_EXERCISE_NAME = "exercise_name"
        const val COLUMN_SETS = "sets"
        const val COLUMN_REPS = "reps"
        const val COLUMN_WEIGHT = "weight"
        const val COLUMN_DATE = "date_created"
        const val COLUMN_GROUP = "muscle_group"
        const val COLUMN_DAY_OF_WEEK = "day_of_week"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val sql = """
            CREATE TABLE IF NOT EXISTS $TABLE_WORKOUTS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_EXERCISE_NAME TEXT NOT NULL,
                $COLUMN_SETS INTEGER NOT NULL,
                $COLUMN_REPS INTEGER NOT NULL,
                $COLUMN_WEIGHT REAL,
                $COLUMN_DATE DATETIME DEFAULT CURRENT_TIMESTAMP,
                $COLUMN_GROUP TEXT,
                $COLUMN_DAY_OF_WEEK TEXT
            );
        """.trimIndent()

        try {
            db?.execSQL(sql)
            Log.i("info_db", "Tabela de treinos criada com sucesso")
        } catch (e: Exception) {
            Log.e("info_db", "Erro ao criar tabela: ${e.message}")
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
}