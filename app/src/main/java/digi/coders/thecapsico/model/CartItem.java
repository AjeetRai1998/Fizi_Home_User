package digi.coders.thecapsico.model;

public class CartItem {

    private String totalprice;
    private int totalcart;
    private String TaxesandPackingCharge;
    private Merchant[] merchant;

    public Merchant[] getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant[] merchant) {
        this.merchant = merchant;
    }

    public String getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }

    public int getTotalcart() {
        return totalcart;
    }

    public void setTotalcart(int totalcart) {
        this.totalcart = totalcart;
    }

    public String getTaxesandPackingCharge() {
        return TaxesandPackingCharge;
    }

    public void setTaxesandPackingCharge(String taxesandPackingCharge) {
        TaxesandPackingCharge = taxesandPackingCharge;
    }
}