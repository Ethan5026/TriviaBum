package coms309.TriviaBum.Questions.FillBlank;

import java.util.List;
import java.util.Objects;

import coms309.TriviaBum.Helpers.Parsers.Q1RequestParser;
import coms309.TriviaBum.Helpers.Parsers.Q1RequestParserID;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;

@RestController
//@Api(tags = "fillblank Controller", description = "Controller for multiple choice question objects")
public class FillBlankController{
    @Autowired
    FillBlankRepository FillBlankRepository;
    private final String success = "{\"message\":\"success\"}";
    private final String failure = "{\"message\":\"failure\"}";


    /**
     * Method to get all questions
     * @return an array of all of the fillblank question objects
     */
    @Operation(summary = "Method to get all questions")
    @GetMapping(path = "/fillblanks")
    List<FillBlank> getAllQuestions(){
        return FillBlankRepository.findAll();
    }

    /**
     * Method to get a certain fillblank question object
     * @param id The ID of the question you are trying to find
     * @return the fillblank object associated with the ID given
     */
    @Operation(summary = "Method to get a certain fillblank question object")
    @GetMapping(path = "/fillblank/{id}")
    FillBlank findById(@PathVariable Long id){
        return FillBlankRepository.getReferenceById(id);
    }



    /**
     * This is a method used to get the correct answer of a fillblank question object
     * @param id the ID of the question you are trying to search about
     * @return returns the string of the correct answer
     */
    @Operation(summary = "Method used to get the correct answer of a fillblank question object")
    @GetMapping(path = "/fillblank/getanswer1/{id}")
    String getAnswer1(@PathVariable Long id){
        FillBlank temp = FillBlankRepository.getReferenceById(id);
        return temp.answer1;
    }

    
    /**
     * this is a method used to get the question of a fillblank question object
     * @param id the ID of the question you are trying to search about
     * @return returns the string of the question
     */
    @Operation(summary = "Method to get the question of a fillblank question object")
    @GetMapping(path = "/fillblank/getquestion/{id}")
    String getQuestion(@PathVariable Long id){
        FillBlank temp = FillBlankRepository.getReferenceById(id);
        return temp.question;
    }

    /**
     * this is a method used to delete a fillblank question object from its respective repository
     * @param id the ID of the question you are trying to search about
     * @return a string based that reads either success or failure depending on if the delete task worked
     */
    @Operation(summary = "Method used to delete a fillblank question object from its respective repository")
    @DeleteMapping(path = "/fillblank/{id}")
    String deletefillblank(@PathVariable Long id){
        FillBlankRepository.deleteById(id);
        return success;
    }


    /**
     * This is a method used to create a new fillblank question object
     * @param question requests a parsed string that contains the question, correct answer, and 3 incorrect answers
     *                 this is used with the Q4parser get the separate strings
     * @return a string based on if the task was able to be completed. Should expect to receive either success or failure
     */
    @Parameter(description = "The field must contain the correct answer, and the question (in this order) seperated by '%3A'")
    @Operation(summary = "Method used to create a new fillblank question object")
    @PostMapping(path = "/fillblank/create")
    String createfillblankRB(@RequestBody String question){
        Q1RequestParser temp = new Q1RequestParser(question);
        FillBlank fillblank = new FillBlank(temp.getAnswer1(), temp.getQuestion());
        FillBlankRepository.save(fillblank);
        return success;
    }


    /**
     * This is a method used to edit an already existing fillblank question object
     * @param question requests a parsed string that contains the question, correct answer, and 3 incorrect answers
     *      *                 this is used with the Q4parser get the separate strings
     * @return a string based on if the task was able to be completed. Should expect to receive either success or failure
     */

    @Parameter(description = "The field must contain the question id, correct answer, 3 inncorrect answers and the question in that order seperated by '%3A'")
    @Operation(summary = "Method used to edit an already existing fillblank question object")
    @PutMapping(path = "fillblank/edit")
    String editfillblank(@RequestBody String question){
        try {
            Q1RequestParserID temp = new Q1RequestParserID(question);
            FillBlank old = FillBlankRepository.getReferenceById((long)temp.getID());
            if(temp.getAnswer1() != null){
                old.setAnswer1(temp.getAnswer1());
            }
            if(!(temp.getQuestion().isEmpty())) {
                old.setQuestion(temp.getQuestion());
            }
            FillBlankRepository.save(old);
        }catch(Exception e){
            return failure;
        }


        return success;
    }

    
}