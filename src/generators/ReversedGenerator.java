package generators;

public class ReversedGenerator implements DataGenerator {
    @Override
    public int[] generate(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = size - i;
        }
        return array;
    }

    @Override
    public String getType() {
        return "Reversed";
    }
}
