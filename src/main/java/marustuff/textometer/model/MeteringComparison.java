package marustuff.textometer.model;

import lombok.Data;

@Data
public class MeteringComparison {
    private static final String EMPTY_WORD = "";
    private String word1;
    private String word2;
    private int percentage;

    public MeteringComparison() {
        this.word1 = EMPTY_WORD;
        this.word2 = EMPTY_WORD;
        this.percentage = 100;
    }

    public MeteringComparison(Metering metOne, Metering metTwo) {
        if (metOne.getScore() > metTwo.getScore()) {
            this.calculatePercentage(metOne, metTwo);
            this.word1 = metOne.getWord();
            this.word2 = metTwo.getWord();
        } else {
            this.calculatePercentage(metTwo, metOne);
            this.word1 = metTwo.getWord();
            this.word2 = metOne.getWord();
        }
    }

    public void calculatePercentage(Metering metone, Metering mettwo) {
        if (mettwo.getScore() == 0) {
            this.percentage = 100;
        } else {
            this.percentage = (int) (((double) metone.getScore() / ((double) metone.getScore() + (double) mettwo.getScore())) * 100);
        }
    }
}
