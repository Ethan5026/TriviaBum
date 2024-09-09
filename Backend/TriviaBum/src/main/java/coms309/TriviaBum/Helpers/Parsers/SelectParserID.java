package coms309.TriviaBum.Helpers.Parsers;

import coms309.TriviaBum.Questions.QuestionType;

public class SelectParserID {

    QuestionType questionType;
    String ID;
    String answer1;
    String answer2;
    String answer3;
    String answer4;
    String answer1correct;
    String answer2correct;
    String answer3correct;
    String answer4correct;
    String question;

    private String plus = "+";

    public SelectParserID(String s){
        String[] tokens = s.split("%3A");
        ID = tokens[0].replace(plus," ");
        answer1 = tokens[1].replace(plus," ");
        answer1correct = (tokens[2].replace(plus," "));
        answer2 = tokens[3].replace(plus," ");
        answer2correct = (tokens[4].replace(plus," "));
        answer3 = tokens[5].replace(plus," ");
        answer3correct = (tokens[6].replace(plus," "));
        answer4 = tokens[7].replace(plus," ");
        answer4correct = (tokens[8].replace(plus," "));
        question = tokens[9].replace(plus," ");
    }

    public int getID() {
        return Integer.parseInt(ID);
    }
    public String getAnswer1() {
        return answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public String getAnswer4() {
        return answer4;
    }

    public String getAnswer1correct() {
        return answer1correct;
    }

    public String getAnswer2correct() {
        return answer2correct;
    }

    public String getAnswer3correct() {
        return answer3correct;
    }

    public String getAnswer4correct() {
        return answer4correct;
    }

    public String getQuestion() {
        return question;
    }
}
