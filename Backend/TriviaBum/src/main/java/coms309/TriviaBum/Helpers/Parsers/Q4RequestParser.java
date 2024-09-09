package coms309.TriviaBum.Helpers.Parsers;

public class Q4RequestParser {
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private String question;

    private String plus = "+";

    /**
     * Constructor for parser that works with questions with only 4 answers (DOES NOT HANDLE ID)
     * @param s string parsed in specific format containing the correct answer, incorrect answers and question string
     *          in that order. The different answers/questions need to be seperated with "%3A". There should
     *          be 1 correct answer given, 3 incorrect answers and 1 question in this string.
     */
    public Q4RequestParser(String s){
        String[] tokens = s.split("%3A");
        answer1 = tokens[0].replace(plus," ");
        answer2 = tokens[1].replace(plus," ");
        answer3 = tokens[2].replace(plus," ");
        answer4 = tokens[3].replace(plus," ");
        question = tokens[4].replace(plus," ");
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
}
