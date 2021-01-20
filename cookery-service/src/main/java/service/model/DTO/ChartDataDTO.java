package service.model.DTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChartDataDTO {
    private String title;
    private List<Object> xAxis;
//    private List<Object> yAxis;
    private List<Integer> yAxis;

    public ChartDataDTO() {
    }

    public ChartDataDTO(String title) {
        this.title = title;
        xAxis = new ArrayList<>();
        yAxis = new ArrayList<>();
    }

    public ChartDataDTO(String title, List<Object> xAxis, List<Integer> yAxis) {
        this.title = title;
        this.xAxis = xAxis;
        this.yAxis = yAxis;
    }


    public String getTitle() {
        System.out.println("Get title " + title);
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void addX(Object x) {
        this.xAxis.add(x);
    }

    public void addY(Integer y) {
        this.yAxis.add(y);
    }

    public List<Object> getxAxis() {
        System.out.println("get x " + xAxis);
        return xAxis;
    }

    public List<Integer> getyAxis() {
        System.out.println("get y " + yAxis);

        return yAxis;
    }

    public void setxAxis(List<Object> xAxis) {
        this.xAxis = xAxis;
    }

    public void setyAxis(List<Integer> yAxis) {
        this.yAxis = yAxis;
    }

    @Override
    public String toString() {
        return "ChartDataDTO{" +
                "title='" + title + '\'' +
                ", xAxis=" + xAxis +
                ", yAxis=" + yAxis +
                '}';
    }


    @Override
    public boolean equals(Object o) {

        System.out.println("this " + this);
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChartDataDTO that = (ChartDataDTO) o;
        System.out.println("that " + that);

        return Objects.equals(title, that.title) &&
                Objects.equals(xAxis, that.xAxis) &&
                Objects.equals(yAxis, that.yAxis);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, xAxis, yAxis);
    }
}
