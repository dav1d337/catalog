package kinf.koch.catalog.ui.tv

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import kinf.koch.catalog.db.RoomSeries
import kinf.koch.catalog.model.tv.Series
import kinf.koch.catalog.model.tv.TVRepository

class TVViewModel constructor(private val tvRepository: TVRepository): ViewModel() {

    val allSeries: LiveData<List<RoomSeries>> = tvRepository.allSeries

}
