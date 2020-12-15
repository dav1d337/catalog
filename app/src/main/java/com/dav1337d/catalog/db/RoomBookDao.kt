package com.dav1337d.catalog.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RoomBookDao {
    @Query("SELECT * FROM roomBook")
    fun getAll(): LiveData<List<RoomBook>>

  //  @Query("SELECT * FROM roomSeriesMovie WHERE uid IN (:seriesIds)")
  //  fun loadAllByIds(seriesIds: IntArray): List<RoomSeriesMovie>

//    @Query("SELECT * FROM roomBook WHERE book LIKE :name LIMIT 1")
//    suspend fun findByName(name: String): RoomSeriesMovie?
//

    @Query("SELECT COUNT() FROM roomBook WHERE googleId LIKE :id")
    suspend fun count(id: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg roomBook: RoomBook)

    @Delete
    fun delete(roomBook: RoomBook)
}