package com.dav1337d.catalog.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RoomGameDao {
    @Query("SELECT * FROM roomGame")
    fun getAll(): LiveData<List<RoomGame>>

    //  @Query("SELECT * FROM roomSeriesMovie WHERE uid IN (:seriesIds)")
    //  fun loadAllByIds(seriesIds: IntArray): List<RoomSeriesMovie>

    @Query("SELECT * FROM roomGame WHERE name LIKE :name LIMIT 1")
    suspend fun findByName(name: String): RoomGame?

    @Query("SELECT COUNT() FROM roomGame WHERE igdb_id LIKE :id")
    suspend fun count(id: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg roomGame: RoomGame)

    @Delete
    fun delete(roomGame: RoomGame)
}