package com.orcchg.contentproviderdemo.hostapp.domain

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.content.ContentValues

@Entity(tableName = Model.TABLE_NAME)
data class Model(@PrimaryKey @ColumnInfo(name = Model.COLUMN_ID) val id: String,
                 @ColumnInfo(name = Model.COLUMN_NAME) val name: String,
                 @ColumnInfo(name = Model.COLUMN_DESCRIPTION) val description: String,
                 @ColumnInfo(name = Model.COLUMN_TEMPERATURE) val temperature: Double,
                 @ColumnInfo(name = Model.COLUMN_SEASON) val season: String) {

    companion object {
        const val TABLE_NAME = "models"

        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_TEMPERATURE = "temperature"
        const val COLUMN_SEASON = "season"

        fun from(values: ContentValues?): Model? =
            values?.let {
                val id = it.getAsString(COLUMN_ID) ?: "null_id"
                val name = it.getAsString(COLUMN_NAME) ?: ""
                val description = it.getAsString(COLUMN_DESCRIPTION) ?: ""
                val temperature = it.getAsDouble(COLUMN_TEMPERATURE) ?: 0.0
                val season = it.getAsString(COLUMN_SEASON) ?: ""
                Model(id = id, name = name, description = description, temperature = temperature, season = season)
            }
    }
}

data class ListModels(val models: List<Model>)
