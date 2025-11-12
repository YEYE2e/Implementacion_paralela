package generators;

import java.util.Random;

public class NearlySortedGenerator implements DataGenerator {
    private Random random = new Random();

    @Override
    public int[] generate(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = i;
        }

        // Alterar 20% de los datos
        int swaps = size / 5;
        for (int i = 0; i < swaps; i++) {
            int idx1 = random.nextInt(size);
            int idx2 = random.nextInt(size);
            int temp = array[idx1];
            array[idx1] = array[idx2];
            array[idx2] = temp;
        }
        return array;
    }

    @Override
    public String getType() {
        return "NearlySorted";
    }
}
