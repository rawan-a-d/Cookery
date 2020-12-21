package service.model.DTO;

import java.util.ArrayList;
import java.util.List;

public class ChartDataDTO {
    private String title;
    private List<Object> xAxis;
    private List<Object> yAxis;

    public ChartDataDTO(String title, List<Object> xAxis, List<Object> yAxis) {
        this.title = title;
        this.xAxis = xAxis;
        this.yAxis = yAxis;
    }

    public ChartDataDTO(String title) {
        this.title = title;
        xAxis = new ArrayList<>();
        yAxis = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void addX(Object x) {
        this.xAxis.add(x);
    }

    public void addY(Object y) {
        this.yAxis.add(y);
    }

    public List<Object> getxAxis() {
        return xAxis;
    }

    public List<Object> getyAxis() {
        return yAxis;
    }

    @Override
    public String toString() {
        return "ChartDataDTO{" +
                "title='" + title + '\'' +
                ", xAxis=" + xAxis +
                ", yAxis=" + yAxis +
                '}';
    }
}
