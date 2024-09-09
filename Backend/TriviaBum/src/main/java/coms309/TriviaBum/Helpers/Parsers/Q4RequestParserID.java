package coms309.TriviaBum.Helpers.Parsers;

public class Q4RequestParserID {
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private String question;

    private String ID;

    private String plus = "+";

    /**
     * Constructor for parser that works with questions with only 4 answers while handing the question ID
     * @param s string parsed in specific format containing the question ID, correct answer, incorrect answers and question
     *          string in that order. The different answers/questions need to be seperated with "%3A". There should
     *          be the question ID, 1 correct answer, 3 incorrect answers and 1 question in this string.
     */
    public Q4RequestParserID(String s){
        String[] tokens = s.split("%3A");
        ID = tokens[0].replace(plus," ");
        answer1 = tokens[1].replace(plus," ");
        answer2 = tokens[2].replace(plus," ");
        answer3 = tokens[3].replace(plus," ");
        answer4 = tokens[4].replace(plus," ");
        question = tokens[5].replace(plus," ");

    }

    /**
     * Method to get the correct answer
     * @return A string containing the correct answer
     */
    public String getAnswer1() {
        return answer1;
    }

    /**
     * Method to get the first incorrect answer
     * @return A string containing the first incorrect answer
     */
    public String getAnswer2() {
        return answer2;
    }

    /**
     * Method to get the second incorrect answer
     * @return A string containing the second incorrect answer
     */
    public String getAnswer3() {
        return answer3;
    }

    /**
     * Method to get the third incorrect answer
     * @return A string containing the third incorrect answer
     */
    public String getAnswer4() {
        return answer4;
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
     * @return An int containing the ID
     */
    public int getID() {
        return Integer.parseInt(ID);
    }


}

