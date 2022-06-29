package digi.coders.thecapsico.fragmentmodel;

import androidx.lifecycle.ViewModel;

import digi.coders.thecapsico.model.MerchantCategory;

public class HomeViewModel extends ViewModel {

    private MerchantCategory category;
    private int staus;

    public int getStaus() {
        return staus;
    }

    public void setStaus(int staus) {
        this.staus = staus;
    }

    public MerchantCategory getCategory() {
        return category;
    }

    public void setCategory(MerchantCategory category) {
        this.category = category;
    }
}
