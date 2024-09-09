package coms309.TriviaBum.Questions.FillBlank;


import coms309.TriviaBum.Questions.QuestionType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class FillBlank{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    QuestionType questionType;
    String answer1;
    String question;

    public FillBlank(String correctAnswer, String question){
        this.answer1 = correctAnswer;
        this.question = question;
        this.questionType = QuestionType.FILLBLANK;
    }

    public FillBlank() {

    }

    /**
     * sets the ID of the multiplechoice question object
     * @param id the ID you want to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get the Id
     * @return the ID of the multiplechoice question object
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the type of question
     * @return MULTIPLECHOICE
     */
    public QuestionType getQuestionType() {
        return questionType;
    }

    /**
     * Get the correct answer
     * @return A string containing the correct answer
     */
    public String getAnswer1() {
        return answer1;
    }

    /**
     * Method to update the correct answer
     * @param answer1 the string you wish to update the correct answer with
     */
    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }


    public String getQuestion() {
        return question;
    }

    /**
     * Method to update the question
     * @param question String to be used as this objects new question
     */
    public void setQuestion(String question) {
        this.question = question;
    }
}
