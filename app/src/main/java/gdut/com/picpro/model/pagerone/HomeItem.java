package gdut.com.picpro.model.pagerone;

/**
 * Created by lchua on 2016/10/24.
 * <p>
 * 描述：
 */

public class HomeItem {
    ItemType type;

    int resd[];

    String edtext;

    int imgResrouce;
    String name;

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public int[] getResd() {
        return resd;
    }

    public void setResd(int[] resd) {
        this.resd = resd;
    }

    public String getEdtext() {
        return edtext;
    }

    public void setEdtext(String edtext) {
        this.edtext = edtext;
    }

    public int getImgResrouce() {
        return imgResrouce;
    }

    public void setImgResrouce(int imgResrouce) {
        this.imgResrouce = imgResrouce;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
