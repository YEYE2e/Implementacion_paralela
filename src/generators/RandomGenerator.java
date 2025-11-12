package generators;

import java.util.Random;

public class RandomGenerator implements DataGenerator {
    private Random random = new Random();

    @Override
    public int[] generate(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(size * 10);
        }
        return array;
    }

    @Override
    public String getType() {
        return "Random";
    }
}
