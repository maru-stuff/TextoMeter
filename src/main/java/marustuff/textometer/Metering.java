package marustuff.textometer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@AllArgsConstructor
public class Metering {
    @Id
    String word;

    int score;
    public void incScore(){
        score++;
    }

    public Metering(){
        this.word="none";
        this.score=0;
    }

    public Metering(String word){
        this.word=word;
        this.score=0;

    }

    public Metering (Metering...meterings){

        for(Metering element : meterings){
            this.setScore(this.getScore()+element.getScore());
            if(this.getWord() != element.getWord()){
                this.setWord(element.getWord());
            }
        }


    }

    public void AddMetering(Metering metering){
        this.score=this.score+ metering.getScore();
    }
}
