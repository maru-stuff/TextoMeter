package marustuff.textometer.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;

@Data
@Entity
@AllArgsConstructor
public class Metering {
    @Id
    private String word;

    private int score;
    public Instant createdAt;
    public void incScore(){
        score++;
    }

    {
        this.createdAt = Instant.now();
    }

    public Metering(){
        // "none" do stałej
        this.word="none";
        this.score=0;

    }

    public Metering(String word){
        this.word=word;
        this.score=0;


    }

    //nieużywany konstruktor
    public Metering (Metering...meterings){

        for(Metering element : meterings){
            this.setScore(this.getScore()+element.getScore());
            // Stringów, jako że są to obiekty nie należy porównywać znakami == !=, intelliJ podpowiada użycie equals. Oprócz tego nie mamy pewności, że te obiekty nie są null'ami
            if(this.getWord() != element.getWord()){
                this.setWord(element.getWord());
            }
        }


    }

    public void AddMetering(Metering metering){
        this.score=this.score+ metering.getScore();
    }
}
//formatownaie kodu