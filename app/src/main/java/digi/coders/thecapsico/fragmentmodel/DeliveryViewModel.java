package digi.coders.thecapsico.fragmentmodel;

import androidx.lifecycle.ViewModel;

import digi.coders.thecapsico.model.Merchant;

public class DeliveryViewModel extends ViewModel {
    private Merchant merchant;

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }
}
