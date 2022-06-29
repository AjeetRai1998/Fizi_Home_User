package digi.coders.thecapsico.fragmentmodel;

import androidx.lifecycle.ViewModel;

import digi.coders.thecapsico.model.Category;

public class ProductViewModel extends ViewModel {

    private Category category;

    public Category getCategory()   {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
