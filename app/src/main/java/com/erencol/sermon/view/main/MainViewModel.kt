package com.erencol.sermon.view.main

import androidx.lifecycle.MutableLiveData
import com.erencol.sermon.data.service.Host
import com.erencol.sermon.data.service.SermonClient
import com.erencol.sermon.model.Sermon
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.Observable

class MainViewModel : Observable() {

    var busy: MutableLiveData<Int> = MutableLiveData(8)
    private var compositeDisposable: CompositeDisposable? = CompositeDisposable()

    var sermonList: MutableLiveData<List<Sermon>> = MutableLiveData()
        private set

    fun getSermons() {
        busy.value = 0
        val sermonsService = SermonClient.createSermonClient()
        val disposable = sermonsService.getSermons(Host.sermonsEndpoint)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ sermons: List<Sermon>? ->
                sermonList.value = sermons
                setChanged()
                notifyObservers()
                busy.value = 8
            }) { obj: Throwable -> obj.printStackTrace() }
        compositeDisposable?.add(disposable)
    }

    private fun unSubscribeFromObservable() {
        if (compositeDisposable != null && !compositeDisposable!!.isDisposed) {
            compositeDisposable!!.dispose()
        }
    }

    fun reset() {
        unSubscribeFromObservable()
        compositeDisposable = null
    }


}