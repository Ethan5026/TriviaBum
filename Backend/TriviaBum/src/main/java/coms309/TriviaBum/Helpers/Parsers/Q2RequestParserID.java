package coms309.TriviaBum.Helpers.Parsers;

public class Q2RequestParserID {

    private String answer1; //correct answer
    private String answer2; //incorrect answer
    private String question; //question

    private String ID;

    private String plus = "+";

    /**
     * Constructor for parser that works with questions with only 2 answers and can handle the ID
     * @param s string parsed in specific format containing the question ID, correct answer, incorrect answer and question string
     *          in that order. The ID,answers, and questions need to be seperated with "%3A"
     */
    public Q2RequestParserID(String s){
        String[] tokens = s.split("%3A");
        ID = tokens[0].replace(plus," ");
        answer1 = tokens[1].replace(plus," ");
        answer2 = tokens[2].replace(plus," ");
        question = tokens[3].replace(plus," ");

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
     * @return A string containing the question
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Method to get the ID
     * @return An int that has the ID value
     */
    public int getID() {
        return Integer.parseInt(ID);
    }

}
