import java.util.ArrayList;

/**
 * Project 5: DoubleArrayList
 *
 * Returns all available tickets from a seller and returns a list of the name of all stores.
 *
 * @author Rahul Siddharth, Lab Section L20
 * @version November 13, 2023
 */

public class DoubleArrayList {
    private ArrayList<String> stuffInStores;
    private ArrayList<String> storeList;

    public DoubleArrayList(ArrayList<String> stuffInStores, ArrayList<String> storeList) {
        this.stuffInStores = stuffInStores;
        this.storeList = storeList;
    }

    public ArrayList<String> getStuffInStores() {
        return stuffInStores;
    }

    public ArrayList<String> getStoreList() {
        return storeList;
    }

    public void setStuffInStores(ArrayList<String> stuffInStores) {
        this.stuffInStores = stuffInStores;
    }

    public void setStoreList(ArrayList<String> storeList) {
        this.storeList = storeList;
    }

}
