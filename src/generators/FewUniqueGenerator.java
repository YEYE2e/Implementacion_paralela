package generators;

import java.util.Random;

public class FewUniqueGenerator implements DataGenerator {
    private Random random = new Random();
    private static final int CATEGORIES = 5;

    @Override
    public int[] generate(int size) {
        int[] array = new int[size];
        int[] uniqueValues = new int[CATEGORIES];

        for (int i = 0; i < CATEGORIES; i++) {
            uniqueValues[i] = random.nextInt(1000);
        }

        for (int i = 0; i < size; i++) {
            array[i] = uniqueValues[random.nextInt(CATEGORIES)];
        }
        return array;
    }

    @Override
    public String getType() {
        return "FewUnique";
    }
}
