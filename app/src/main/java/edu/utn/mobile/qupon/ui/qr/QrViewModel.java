package edu.utn.mobile.qupon.ui.qr;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class QrViewModel extends ViewModel {


        private MutableLiveData<String> mText;

        public QrViewModel() {
            mText = new MutableLiveData<>();
            mText.setValue("This is Qr fragment");
        }

        public LiveData<String> getText() {
            return mText;
        }

}
