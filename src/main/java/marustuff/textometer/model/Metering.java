package marustuff.textometer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;

@Data
@Entity
@AllArgsConstructor
public class Metering {
    private static final String emptyWord = "none";
    @CreationTimestamp
    @Column(updatable = false)
    public Instant createdAt;
    @Id
    private String word;
    @Column
    private int score;

    public Metering() {
        this.word = emptyWord;
        this.score = 0;
    }

    public Metering(String word) {
        this.word = word;
        this.score = 0;
    }

    public void incScore() {
        score++;
    }

    public void AddMetering(Metering metering) {
        this.score = this.score + metering.getScore();
    }
}
