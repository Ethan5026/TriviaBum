package coms309.TriviaBum.Questions.MultipleChoice;

import java.util.List;
import java.util.Objects;

import coms309.TriviaBum.Helpers.Parsers.Q4RequestParser;
import coms309.TriviaBum.Helpers.Parsers.Q4RequestParserID;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;

@RestController
//@Api(tags = "MultipleChoice Controller", description = "Controller for multiple choice question objects")
public class MultipleChoiceController{
    @Autowired
    MultipleChoiceRepository MCquestionRepository;
    private final String success = "{\"message\":\"success\"}";
    private final String failure = "{\"message\":\"failure\"}";


    /**
     * Method to get all questions
     * @return an array of all of the multiplechoice question objects
     */
    @Operation(summary = "Method to get all questions")
    @GetMapping(path = "/multiplechoicequestions")
    public List<MultipleChoice> getAllQuestions(){
        return MCquestionRepository.findAll();
    }

    /**
     * Method to get a certain multiplechoice question object
     * @param id The ID of the question you are trying to find
     * @return the multiplechoice object associated with the ID given
     */
    @Operation(summary = "Method to get a certain multiplechoice question object")
    @GetMapping(path = "/multiplechoicequestion/{id}")
    MultipleChoice findById(@PathVariable Long id){
        return MCquestionRepository.getReferenceById(id);
    }



    /**
     * This is a method used to get the correct answer of a multiplechoice question object
     * @param id the ID of the question you are trying to search about
     * @return returns the string of the correct answer
     */
    @Operation(summary = "Method used to get the correct answer of a multiplechoice question object")
    @GetMapping(path = "/multiplechoicequestion/getanswer1/{id}")
    String getAnswer1(@PathVariable Long id){
        MultipleChoice temp = MCquestionRepository.getReferenceById(id);
         return temp.answer1;
    }

    /**
     * This is a method used to get the first incorrect answer of a multiplechoice question object
     * @param id the ID of the question you are trying to search about
     * @return returns the string of the first incorrect answer
     */
    @Operation(summary = "Method used to get the first incorrect answer of a multiplechoice question object")
    @GetMapping(path = "/multiplechoicequestion/getanswer2/{id}")
    String getAnswer2(@PathVariable Long id){
        MultipleChoice temp = MCquestionRepository.getReferenceById(id);
        return temp.answer2;
    }

    /**
     * This is a method used to get the second incorrect answer of a multiplechoice question object
     * @param id the ID of the question you are trying to search about
     * @return returns the string of the second answer
     */
    @Operation(summary = "Method used to get the second incorrect answer of a multiplechoice question object")
    @GetMapping(path = "/multiplechoicequestion/getanswer3/{id}")
    String getAnswer3(@PathVariable Long id){
        MultipleChoice temp = MCquestionRepository.getReferenceById(id);
        return temp.answer3;
    }

    /**
     * This is a method used to get the first third answer of a multiplechoice question object
     * @param id the ID of the question you are trying to find
     * @return returns the string of the third incorrect answer
     */
    @Operation(summary = "Method used to get the third incorrect answer of a multiplechoice question object")
    @GetMapping(path = "/multiplechoicequestion/getanswer4/{id}")
    String getAnswer4(@PathVariable Long id){
        MultipleChoice temp = MCquestionRepository.getReferenceById(id);
        return temp.answer4;
    }

    /**
     * this is a method used to get the question of a multiplechoice question object
     * @param id the ID of the question you are trying to search about
     * @return returns the string of the question
     */
    @Operation(summary = "Method to get the question of a multiplechoice question object")
    @GetMapping(path = "/multiplechoicequestion/getquestion/{id}")
    String getQuestion(@PathVariable Long id){
        MultipleChoice temp = MCquestionRepository.getReferenceById(id);
        return temp.question;
    }

    /**
     * this is a method used to delete a multiplechoice question object from its respective repository
     * @param id the ID of the question you are trying to search about
     * @return a string based that reads either success or failure depending on if the delete task worked
     */
    @Operation(summary = "Method used to delete a multiplechoice question object from its respective repository")
    @DeleteMapping(path = "/multiplechoicequestion/{id}")
    String deleteMultipleChoice(@PathVariable Long id){
        MCquestionRepository.deleteById(id);
        return success;
    }

//    //method to create from path variables    (this is shit don't use this)
//    @PostMapping(path = "/multiplechoicequestion/create/{answer1}/{answer2}/{answer3}/{answer4}/{question}")
//    String createMultipleChoice(@PathVariable String answer1, @PathVariable String answer2,@PathVariable String answer3,
//                                @PathVariable String answer4,@PathVariable String question){
//        if (answer1 == null | answer2 == null | answer3 == null | answer4 == null | question == null){
//            return failure;
//        }
//        MultipleChoice multipleChoice = new MultipleChoice(answer1, answer2, answer3, answer4, question);
//        MCquestionRepository.save(multipleChoice);
//        return success;
//    }



    /**
     * This is a method used to create a new multiplechoice question object
     * @param question requests a parsed string that contains the question, correct answer, and 3 incorrect answers
     *                 this is used with the Q4parser get the separate strings
     * @return a string based on if the task was able to be completed. Should expect to receive either success or failure
     */
    @Parameter(description = "The field must contain the correct answer, 3 inncorrect answers and the question in that order seperated by '%3A'")
    @Operation(summary = "Method used to create a new multiplechoice question object")
    @PostMapping(path = "/multiplechoicequestion/create")
    String createMultipleChoiceRB(@RequestBody String question){
        Q4RequestParser temp = new Q4RequestParser(question);
        MultipleChoice multipleChoice = new MultipleChoice(temp.getAnswer1(), temp.getAnswer2(), temp.getAnswer3(),
                temp.getAnswer4(), temp.getQuestion());
        MCquestionRepository.save(multipleChoice);
        return success;
    }


    /**
     * This is a method used to edit an already existing multiplechoice question object
     * @param question requests a parsed string that contains the question, correct answer, and 3 incorrect answers
     *      *                 this is used with the Q4parser get the separate strings
     * @return a string based on if the task was able to be completed. Should expect to receive either success or failure
     */

    @Parameter(description = "The field must contain the question id, correct answer, 3 inncorrect answers and the question in that order seperated by '%3A'")
    @Operation(summary = "Method used to edit an already existing multiplechoice question object")
    @PutMapping(path = "multiplechoicequestion/edit")
    String editMultipleChoice(@RequestBody String question){
        try {
            Q4RequestParserID temp = new Q4RequestParserID(question);
            MultipleChoice old = MCquestionRepository.getReferenceById((long)temp.getID());
            if(temp.getAnswer1() != null){
                old.setAnswer1(temp.getAnswer1());
            }
            if(!(temp.getAnswer2().isEmpty())){
                old.setAnswer2(temp.getAnswer2());

            }
            if(!(temp.getAnswer3().isEmpty())){
                old.setAnswer3(temp.getAnswer4());

            }
            if(!(temp.getAnswer4().isEmpty())){
                old.setAnswer4(temp.getAnswer4());

            }
            if(!(temp.getQuestion().isEmpty())) {
                old.setQuestion(temp.getQuestion());
            }
            MCquestionRepository.save(old);
        }catch(Exception e){
            return failure;
        }


        return success;
    }





}
