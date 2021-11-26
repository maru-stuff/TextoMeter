package marustuff.textometer.model;

import lombok.Data;
import marustuff.textometer.model.Metering;

@Data
//czym jest Vs?
public class Vs {
    private String word1;
    private String word2;
    private int percentage;

    public Vs(){
        // "none" powinno być wyniesione jako stała, ogólnie takie stringi w kodzie to bardzo riski rzecz, lepiej zawsze mieć je jako stała
        this.word1="none";
        this.word2="none";
        this.percentage=100;
    }
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
            //po co to przypisanie do zmiennej?
            int a = this.percentage = (int) (((double) metone.getScore() /((double) metone.getScore() + (double) mettwo.getScore()))* 100);

        }
    }
}
