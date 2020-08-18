package kinf.koch.catalog.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*

@Dao
interface RoomSeriesDao {
    @Query("SELECT * FROM roomSeriesMovie")
    fun getAll(): LiveData<List<RoomSeriesMovie>>

  //  @Query("SELECT * FROM roomSeriesMovie WHERE uid IN (:seriesIds)")
  //  fun loadAllByIds(seriesIds: IntArray): List<RoomSeriesMovie>

    @Query("SELECT * FROM roomSeriesMovie WHERE original_name LIKE :name LIMIT 1")
    fun findByName(name: String): RoomSeriesMovie

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg roomSeryMovies: RoomSeriesMovie)

    @Delete
    fun delete(roomSeriesMovie: RoomSeriesMovie)
}