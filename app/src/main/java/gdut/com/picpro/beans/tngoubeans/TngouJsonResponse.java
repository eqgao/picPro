package gdut.com.picpro.beans.tngoubeans;

import java.util.List;

/**
 * Created by helloworld on 2016/10/18.
 */

public class TngouJsonResponse {
    private String status;
    private String total;
    private List<Tngous> tngou;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Tngous> getTngou() {
        return tngou;
    }

    public void setTngou(List<Tngous> tngou) {
        this.tngou = tngou;
    }
}
