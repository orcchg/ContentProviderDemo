package com.orcchg.contentproviderdemo.hostapp.domain

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Single

@Dao
interface ModelDao {

    @Query("SELECT * FROM ${Model.TABLE_NAME}")
    fun models(): Single<List<Model>>

    @Query("SELECT COUNT(*) FROM ${Model.TABLE_NAME}")
    fun totalModels(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertModels(models: Collection<Model>)
}
