package kinf.koch.catalog.ui.tv

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.volley.Response
import kinf.koch.catalog.model.tv.EitherMovieOrSeries
import kinf.koch.catalog.model.tv.TVRepository
import kotlinx.coroutines.launch
import org.koin.standalone.KoinComponent

class SearchViewModel constructor(private val tvRepository: TVRepository): ViewModel(),
    KoinComponent {

    val results: MutableLiveData<List<EitherMovieOrSeries>> by lazy {
        MutableLiveData<List<EitherMovieOrSeries>>()
    }

    fun search(query: String?) {
        viewModelScope.launch {

            val listener = Response.Listener<String> {
                results.value = tvRepository.handleResponse(it)
                results.value!!.forEachIndexed { index, eitherMovieOrSeries ->
                    val listenerImages = Response.Listener<Bitmap> {img ->
                        if (index < results.value!!.size) {
                            results.value?.get(index).let { tv ->
                                tv?.let {
                                    it.poster = img
                                    results.postValue(results.value)
                                }
                            }
                        }
                    }
                    tvRepository.getPosterImage(eitherMovieOrSeries.poster_path, listenerImages)
                }
            }
            tvRepository.searchTMB(query?:"test", listener)
        }
    }
}
