package MPI;

import java.util.HashMap;
import java.util.Map;

public class Colors {
    private final int size;
    private final Map<Integer,String> colors;

    public Colors(int n) {
        this.size = n;

        colors = new HashMap<>();
        for (int i = 0; i < n ; i++) {
            colors.put(i, "");
        }
    }

    public void add(int index, String color) {
        colors.put(index, color);
    }

    public Map<Integer, String> getColorsForIndices(int[] indices) {
        Map<Integer,String> result = new HashMap<>();

        for (int i = 0; i < indices.length; i++) {
            String color = colors.get(indices[i]);
            result.put(i, color);
        }
        return result;
    }

    public int size() { return size; }

    @Override
    public String toString() {
        return "Colors { " +
                "size = " + size +
                ", colors = " + colors +
                " }";
    }
}