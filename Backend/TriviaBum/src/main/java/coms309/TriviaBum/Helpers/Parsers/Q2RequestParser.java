package coms309.TriviaBum.Helpers.Parsers;

public class Q2RequestParser {

    private String answer1;
    private String answer2;
    private String question;

    private String plus = "+";

    /**
     * Constructor for parser that works with questions with only 2 answers (DOES NOT HANDLE ID)
     * @param s string parsed in specific format containing the correct answer, incorrect answer and question string
     *          in that order. The different answers/questions need to be seperated with "%3A"
     */
    public Q2RequestParser(String s){
        String[] tokens = s.split("%3A");
        answer1 = tokens[0].replace(plus," ");
        answer2 = tokens[1].replace(plus," ");
        question = tokens[2].replace(plus," ");
    }

    /**
     * Method to get the correct answer
     * @return A string containing the correct answer
     */
    public String getAnswer1() {
        return answer1;
    }

    /**
     * Method to get the incorrect answer
     * @return A string containing the incorrect answer
     */
    public String getAnswer2() {
        return answer2;
    }


    /**
     * Method to get the question
     * @return A string that contains the question
     */
    public String getQuestion() {
        return question;
    }

}
