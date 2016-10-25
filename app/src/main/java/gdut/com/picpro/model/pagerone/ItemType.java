package gdut.com.picpro.model.pagerone;

/**
 * Created by lchua on 2016/10/24.
 * <p>
 * 描述：
 */

public enum ItemType {
    CYCLEIMG(0), REARCH(1), ITEMPHOTO(2);

    public int getValue() {
        return value;
    }

    private int value;

    ItemType(int value) {
        this.value = value;
    }


}
