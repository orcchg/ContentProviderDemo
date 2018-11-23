package com.orcchg.contentproviderdemo.hostapp.domain

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = Model.TABLE_NAME)
data class Model(@PrimaryKey val id: String, val name: String, val description: String, val temperature: Double, val season: String) {

    companion object {
        const val TABLE_NAME = "models"
    }
}

data class ListModels(val models: List<Model>)
