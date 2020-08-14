package kinf.koch.catalog.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RoomSeriesDao {
    @Query("SELECT * FROM roomSeries")
    fun getAll(): LiveData<List<RoomSeries>>

    @Query("SELECT * FROM roomSeries WHERE uid IN (:seriesIds)")
    fun loadAllByIds(seriesIds: IntArray): List<RoomSeries>

    @Query("SELECT * FROM roomSeries WHERE original_name LIKE :name LIMIT 1")
    fun findByName(name: String): RoomSeries

    @Insert
    fun insertAll(vararg roomSeries: RoomSeries)

    @Delete
    fun delete(roomSeries: RoomSeries)
}