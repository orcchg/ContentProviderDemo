package com.orcchg.contentproviderdemo.hostapp.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.orcchg.contentproviderdemo.hostapp.domain.Model
import com.orcchg.contentproviderdemo.hostapp.domain.ModelDao

@Database(entities = [Model::class], version = 1)
abstract class ModelDatabase : RoomDatabase() {

    companion object {
        const val DB_NAME = "ModelsDatabase.db"

        private var instance: ModelDatabase? = null

        fun getInstance(context: Context): ModelDatabase {
            if (instance == null) {
                synchronized(ModelDatabase::class) {
                    if (instance == null) {
                        instance = buildDatabase(context)
                    }
                }
            }
            return instance!!
        }

        private fun buildDatabase(context: Context): ModelDatabase =
                Room.databaseBuilder(context, ModelDatabase::class.java, DB_NAME).build()
    }

    abstract fun modelDao(): ModelDao
}
