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
    private final String emptyWord="none";
    {
        this.createdAt = Instant.now();
    }

    public void incScore(){
        score++;
    }

    public Metering(){
        this.word=emptyWord;
        this.score=0;
    }

    public Metering(String word){
        this.word=word;
        this.score=0;
    }

    public void AddMetering(Metering metering){
        this.score=this.score+ metering.getScore();
    }
}
//formatownaie kodu