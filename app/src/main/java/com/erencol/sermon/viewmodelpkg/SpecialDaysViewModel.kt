package com.erencol.sermon.viewmodelpkg

import androidx.lifecycle.MutableLiveData
import com.erencol.sermon.Data.service.Host
import com.erencol.sermon.Data.service.SermonClient
import com.erencol.sermon.model.ReligiousDays
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers


class SpecialDaysViewModel() {
    var busy: MutableLiveData<Int> = MutableLiveData(0)
    private var compositeDisposable: CompositeDisposable? = CompositeDisposable()
    var religiousDaysLivedata: MutableLiveData<ReligiousDays> = MutableLiveData<ReligiousDays>()

    fun getReligiousDays() {
        busy.value = 0
        val religiousService = SermonClient.createReligiousClient()
        val disposable = religiousService.getReligiousDays(Host.getReligious())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(Consumer { religiousDays: ReligiousDays? ->
                busy.value = 8
                if(religiousDays != null) {
                    religiousDaysLivedata.value = religiousDays
                }
            }, Consumer { obj: Throwable? -> obj!!.printStackTrace() })
        compositeDisposable!!.add(disposable)
    }
}