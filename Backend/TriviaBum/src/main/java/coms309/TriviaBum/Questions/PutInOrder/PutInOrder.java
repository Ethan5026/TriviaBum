package coms309.TriviaBum.Questions.PutInOrder;


import coms309.TriviaBum.Questions.QuestionType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class PutInOrder{

    //answer 1 will be the answer that should be 1st in the answer order (most correct)
    //answer 4 should be the answer that is selected last (least correct)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    QuestionType questionType;
    String answer1;
    String answer2;
    String answer3;
    String answer4;
    String question;

    /**
     * Constructor for a PutInOrder question object
     * @param answer1 String of the most correct answer (1st option in correct answer)
     * @param answer2 String of the 2nd most correct answer (2nd option)
     * @param answer3 String of the 3rd most correct answer (3rd option)
     * @param answer4 String of the 4th most correct answer (least correct, last in sorting order)
     * @param question String of the question
     */
    public PutInOrder(String answer1, String answer2, String answer3, String answer4, String question){
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        this.questionType = QuestionType.PUTINORDER;
    }

    public PutInOrder() {
    }

    // GETTERS AND SETTERS BELOW

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }

    public String getAnswer4() {
        return answer4;
    }

    public void setAnswer4(String answer4) {
        this.answer4 = answer4;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
