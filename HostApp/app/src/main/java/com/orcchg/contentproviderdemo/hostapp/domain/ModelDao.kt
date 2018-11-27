package com.orcchg.contentproviderdemo.hostapp.domain

import android.arch.persistence.room.*
import android.database.Cursor
import io.reactivex.Single

@Dao
interface ModelDao {

    @Query("SELECT * FROM ${Model.TABLE_NAME} WHERE ${Model.COLUMN_ID} = :id")
    fun modelCursor(id: String): Cursor

    @Query("SELECT * FROM ${Model.TABLE_NAME}")
    fun models(): Single<List<Model>>

    @Query("SELECT * FROM ${Model.TABLE_NAME}")
    fun modelsCursor(): Cursor

    @Query("SELECT COUNT(*) FROM ${Model.TABLE_NAME}")
    fun totalModels(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(model: Model): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(models: Collection<Model>)

    @Query("DELETE FROM ${Model.TABLE_NAME} WHERE ${Model.COLUMN_ID} = :id")
    fun delete(id: String): Int

    @Update
    fun update(model: Model): Int
}
