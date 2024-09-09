package coms309.TriviaBum.Questions.TrueFalse;
import coms309.TriviaBum.Questions.QuestionType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class TrueFalse{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    QuestionType questionType;
    String correctAnswer;
    String incorrectAnswer;
    String question;


    /**
     * Constructor for the TrueFalse question object
     * @param correctAnswer String containing the correct answer
     * @param incorrectAnswer String containing the incorrect answer
     * @param question String containing the question
     */
    public TrueFalse(String correctAnswer, String incorrectAnswer, String question){
        this.correctAnswer = correctAnswer;
        this.incorrectAnswer = incorrectAnswer;
        this.question = question;
        this.questionType = QuestionType.TRUEFALSE;

    }

    public TrueFalse() {

    }

    //GETTERS AND SETTERS BELOW

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getIncorrectAnswer() {
        return incorrectAnswer;
    }

    public void setIncorrectAnswer(String incorrectAnswer) {
        this.incorrectAnswer = incorrectAnswer;
    }

    public String getQuestion(){
        return question;
    }

    public void setQuestion(String question){
        this.question = question;
    }

}
