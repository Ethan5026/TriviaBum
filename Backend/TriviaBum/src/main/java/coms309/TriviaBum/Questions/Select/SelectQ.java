package coms309.TriviaBum.Questions.Select;

import coms309.TriviaBum.Questions.QuestionType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class SelectQ{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    QuestionType questionType;
    String answer1;
    String answer2;
    String answer3;
    String answer4;
    String answer1correct;
    String answer2correct;
    String answer3correct;
    String answer4correct;
    String question;

    public SelectQ(String answer1, String answer2, String answer3, String answer4, String question,
                  String answer1correct, String answer2correct, String answer3correct, String answer4correct){
        this.answer1 = answer1;
        this.answer1correct = answer1correct;
        this.answer2 = answer2;
        this.answer2correct = answer2correct;
        this.answer3 = answer3;
        this.answer3correct = answer3correct;
        this.answer4 = answer4;
        this.answer4correct = answer4correct;
        this.question = question;
        this.questionType = QuestionType.SELECT;
    }

    public SelectQ() {

    }

    public int getId(){
        return this.id;
    }

    public QuestionType getQuestionType() {
        return questionType;
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

    public String getAnswer1correct() {
        return answer1correct;
    }

    public void setAnswer1correct(String answer1correct) {
        this.answer1correct = answer1correct;
    }

    public String getAnswer2correct() {
        return answer2correct;
    }

    public void setAnswer2correct(String answer2correct) {
        this.answer2correct = answer2correct;
    }

    public String getAnswer3correct() {
        return answer3correct;
    }

    public void setAnswer3correct(String answer3correct) {
        this.answer3correct = answer3correct;
    }

    public String getAnswer4correct() {
        return answer4correct;
    }

    public void setAnswer4correct(String answer4correct) {
        this.answer4correct = answer4correct;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
