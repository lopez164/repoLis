package chart.barChart;

import java.awt.Color;

public class BarModelLegend {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public BarModelLegend(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public BarModelLegend() {
    }

    private String name;
    private Color color;
}
