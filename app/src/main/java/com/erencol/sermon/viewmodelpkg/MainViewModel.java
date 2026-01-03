package com.erencol.sermon.viewmodelpkg;

import androidx.lifecycle.MutableLiveData;

import com.erencol.sermon.Data.service.Host;
import com.erencol.sermon.Data.service.ISermons;
import com.erencol.sermon.Data.service.SermonClient;
import com.erencol.sermon.model.Sermon;
import java.util.List;
import java.util.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainViewModel extends Observable {
    public MutableLiveData<Integer> busy;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private MutableLiveData<List<Sermon>> sermonListMutableLiveData;
    public MutableLiveData<List<Sermon>> getSermonList(){
        if(sermonListMutableLiveData == null)
            sermonListMutableLiveData = new MutableLiveData<>();
        return sermonListMutableLiveData;
    }


    public MutableLiveData<Integer> getBusy(){
        if(busy == null){
            busy = new MutableLiveData<>();
            busy.setValue(8);
        }
        return busy;
    }

    public void getSermons(){
        getBusy().setValue(0);

        ISermons sermonsService = SermonClient.createSermonClient();
        Disposable disposable = sermonsService.getSermons(Host.getSermons)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sermons -> {
                    getSermonList().setValue(sermons);
                    setChanged();
                    notifyObservers();
                    getBusy().setValue(8);
                }, Throwable::printStackTrace);
        compositeDisposable.add(disposable);
    }


    private void unSubscribeFromObservable() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    public void reset() {
        unSubscribeFromObservable();
        compositeDisposable = null;
    }

}
