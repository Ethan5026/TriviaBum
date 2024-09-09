package coms309.TriviaBum.Questions.TrueFalse;

import java.util.List;

import coms309.TriviaBum.Helpers.Parsers.Q1RequestParser;
import coms309.TriviaBum.Helpers.Parsers.Q2RequestParser;
import coms309.TriviaBum.Helpers.Parsers.Q2RequestParserID;
import coms309.TriviaBum.Helpers.Parsers.Q4RequestParser;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class TrueFalseController {
    @Autowired
    coms309.TriviaBum.Questions.TrueFalse.TrueFalseRepository TrueFalseRepository;
    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";


    /**
     * method to get all of the truefalse question object
     * @return An array of type TrueFalse containing all of the truefalse objects
     */

    @Operation(summary = "Method to get all truefalse questions")
    @GetMapping(path = "/truefalse")
    List<TrueFalse> getAllQuestions(){
        return TrueFalseRepository.findAll();
    }



    /**
     * Method to get a truefalse question from ID tag
     * @param id the ID of the truefalse object you are trying to find
     * @return the truefalse object with the given ID tag
     */

    @Operation(summary = "Method to get a truefalse question from ID tag")
    @GetMapping(path = "/truefalse/{id}")
    TrueFalse findById(@PathVariable int id){
        return TrueFalseRepository.findById(id);
    }


    /**
     * Method to get TrueFalse question object from "question" string
     * @param question parsed string with proper format, used by Q4parser to get individual strings for each field,
     *                 the string needs to contain the correct answer, incorrect answer, and question
     * @return a string that reads success or failure depending if the task was
     */

    @Operation(summary = "Method to get truefalse question object from question string")
    @PostMapping(path = "/truefalse/create")
    String createMultipleChoiceRB(@RequestBody String question){
        Q2RequestParser temp = new Q2RequestParser(question);
        TrueFalse truefalse = new TrueFalse(temp.getAnswer1(), temp.getAnswer2(), temp.getQuestion());
        TrueFalseRepository.save(truefalse);
        return success;
    }



    /**
     * method to delete true false question by using its ID tag
     * @param id the associated id of the question you want to delete
     * @return A string that reads either success of failure depending on if deletion was successful or not
     */

    @Transactional
    @Operation(summary = "Method to delete a truefalse question object from the ID tag")
    @DeleteMapping(path = "/truefalse/{id}")
    String deleteTrueFalse(@PathVariable int id){
        TrueFalseRepository.deleteById(id);
        return success;
    }



    /**
     * This is a method used to edit an already existing truefalse question object
     * @param question requests a parsed string that contains the question, correct answer, and 3 incorrect answers
     *      *                 this is used with the Q4parser get the separate strings
     * @return a string based on if the task was able to be completed. Should expect to receive either success or failure
     */

    @Operation(summary = "Method used to edit an already existing truefalse question object")
    @PutMapping(path = "truefalse/edit")
    String editMultipleChoice(@RequestBody String question){
        try {
            Q2RequestParserID temp = new Q2RequestParserID(question);
            TrueFalse old = TrueFalseRepository.getReferenceById((long)temp.getID());
            if(!(temp.getAnswer1().isEmpty())){
                old.setCorrectAnswer(temp.getAnswer1());
            }
            if(!(temp.getAnswer2().isEmpty())){
                old.setIncorrectAnswer(temp.getAnswer2());

            }
            if(!(temp.getQuestion().isEmpty())){
                old.setQuestion(temp.getQuestion());

            }
            TrueFalseRepository.save(old);
        }catch(Exception e){
            return failure;
        }


        return success;
    }

}
