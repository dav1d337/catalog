package com.dav1337d.catalog.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RoomSeriesDao {
    @Query("SELECT * FROM roomSeriesMovie")
    fun getAll(): LiveData<List<RoomSeriesMovie>>

    //  @Query("SELECT * FROM roomSeriesMovie WHERE uid IN (:seriesIds)")
    //  fun loadAllByIds(seriesIds: IntArray): List<RoomSeriesMovie>

    @Query("SELECT * FROM roomSeriesMovie WHERE original_name LIKE :name LIMIT 1")
    suspend fun findByName(name: String): RoomSeriesMovie?

    @Query("SELECT COUNT() FROM roomSeriesMovie WHERE id_tmdb LIKE :id")
    suspend fun count(id: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg roomSeryMovies: RoomSeriesMovie)

    @Delete
    fun delete(roomSeriesMovie: RoomSeriesMovie)
}