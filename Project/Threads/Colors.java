package Threads;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Colors {
    private int size;
    private Map<Integer, String> colors;

    public Colors(int n) {
        this.size = n;

        colors = new HashMap<>();
        for (int i = 0; i < n; i++) {
            colors.put(i, "");
        }
    }

    public void add(int index, String color) {
        colors.put(index, color);
    }

    public Map<Integer, String> getColorsForIndices(List<Integer> indices) {
        Map<Integer, String> result = new HashMap<>();

        for (int i = 0; i < indices.size(); i++) {
            String color = colors.get(indices.get(i));
            result.put(i, color);
        }
        return result;
    }

    public int size() {
        return size;
    }

    @Override
    public String toString() {
        return "Colors { " +
                "size = " + size +
                ", colors = " + colors +
                " }";
    }
}