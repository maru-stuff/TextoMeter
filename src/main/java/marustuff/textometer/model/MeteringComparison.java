package marustuff.textometer.model;

import lombok.Data;
import marustuff.textometer.model.Metering;

@Data
public class MeteringComparison {
    private final String emptyWord = "none";
    private String word1;
    private String word2;
    private int percentage;

    public MeteringComparison() {
        this.word1 = emptyWord;
        this.word2 = emptyWord;
        this.percentage = 100;
    }

    public MeteringComparison(Metering metone, Metering mettwo) {
        if (metone.getScore() > mettwo.getScore()) {
            this.calculatePercentage(metone, mettwo);
            this.word1 = metone.getWord();
            this.word2 = mettwo.getWord();

        } else {
            this.calculatePercentage(mettwo, metone);
            this.word1 = mettwo.getWord();
            this.word2 = metone.getWord();
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
