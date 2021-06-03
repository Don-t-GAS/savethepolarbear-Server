package donot.gas.back.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
public class Quiz {

    @Id @GeneratedValue
    @Column(name = "quiz_id")
    private Long id;

    private String title;
    private boolean answer;
    private String description;

    public Quiz(String title, boolean answer, String description) {
        this.title = title;
        this.answer = answer;
        this.description = description;
    }
}
