package coms309.TriviaBum.Questions.MultipleChoice;
import coms309.TriviaBum.Questions.QuestionType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class MultipleChoice{

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
     * constructor for multiplechoice question object
     *
     * @param correctAnswer the correct answer string
     * @param incorrectAnswer1 one of the incorrect answer strings
     * @param incorrectAnswer2 one of the incorrect answer strings
     * @param incorrectAnswer3 one of the incorrect answer strings
     * @param question the question string
     */
    public MultipleChoice(String correctAnswer, String incorrectAnswer1, String incorrectAnswer2, String incorrectAnswer3, String question){
        this.answer1 = correctAnswer;
        this.answer2 = incorrectAnswer1;
        this.answer3 = incorrectAnswer2;
        this.answer4 = incorrectAnswer3;
        this.question = question;
        this.questionType = QuestionType.MULTIPLECHOICE;
    }

    public MultipleChoice() {
    }

// GETTERS AND SETTERS BELOW

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

    /**
     * Get the 1st incorrect answer
     * @return A string containing the 1st incorrect answer
     */
    public String getAnswer2() {
        return answer2;
    }

    /**
     * Method to update incorrect answer 1
     * @param answer2 the string you wish to update incorrect answer 1 with
     */
    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    /**
     * Get the 2nd incorrect answer
     * @return A string containing the 2nd incorrect answer
     */
    public String getAnswer3() {
        return answer3;
    }

    /**
     * Method to update incorrect answer 2
     * @param answer3 the string you wish to update incorrect answer 2 with
     */
    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }

    /**
     * Get the 3rd incorrect answer
     * @return A string containing the 3rd incorrect answer
     */
    public String getAnswer4() {
        return answer4;
    }

    /**
     * Method to update incorrect answer 3
     * @param answer4 the string you wish to update incorrect answer 3 with
     */
    public void setAnswer4(String answer4) {
        this.answer4 = answer4;
    }

    /**
     * Method to get the question
     * @return A string containing the question
     */
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
