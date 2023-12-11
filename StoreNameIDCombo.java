/**
 * Project 5: StoreNameIDCombo
 * <p>
 * Returns the id number and name of a store.
 *
 * @author Rahul Siddharth, Lab Section L20
 * @version November 13, 2023
 */

public class StoreNameIDCombo {
    private String storeName;
    private int sellerId;

    public StoreNameIDCombo(String storeName, int sellerId) {
        this.storeName = storeName;
        this.sellerId = sellerId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getSellerID() {
        return sellerId;
    }

    public void setSellerID(int sellerId) {
        this.sellerId = sellerId;
    }
}
