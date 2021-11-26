package marustuff.textometer.model;

import lombok.Data;
import marustuff.textometer.model.Metering;

@Data
public class Vs {
    private String word1;
    private String word2;
    private int percentage;
    public Vs(Metering metone, Metering mettwo){
        if(metone.getScore() > mettwo.getScore()) {
            this.calculatePercentage(metone,mettwo);
            this.word1=metone.getWord();
            this.word2=mettwo.getWord();

        } else {
            this.calculatePercentage(mettwo,metone);
            this.word1=mettwo.getWord();
            this.word2=metone.getWord();
        };

        }

    public void calculatePercentage(Metering metone, Metering mettwo) {
        if (mettwo.getScore() == 0) {
            this.percentage = 100;
        } else {
            int a = this.percentage = (int) (((double) metone.getScore() /((double) metone.getScore() + (double) mettwo.getScore()))* 100);

        }
    }
}
