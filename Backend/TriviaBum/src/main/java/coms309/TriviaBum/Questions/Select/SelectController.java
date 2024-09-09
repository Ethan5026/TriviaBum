package coms309.TriviaBum.Questions.Select;

import java.util.List;
import java.util.Objects;

import coms309.TriviaBum.Helpers.Parsers.SelectParser;
import coms309.TriviaBum.Helpers.Parsers.SelectParserID;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;

@RestController
//@Api(tags = "Select Controller", description = "Controller for multiple choice question objects")
public class SelectController{
    @Autowired
    SelectRepository selectRepository;
    private final String success = "{\"message\":\"success\"}";
    private final String failure = "{\"message\":\"failure\"}";


    /**
     * Method to get all questions
     * @return an array of all of the Select question objects
     */
    @Operation(summary = "Method to get all questions")
    @GetMapping(path = "/select")
    List<SelectQ> getAllQuestions(){
        return selectRepository.findAll();
    }

    /**
     * Method to get a certain Select question object
     * @param id The ID of the question you are trying to find
     * @return the Select object associated with the ID given
     */
    @Operation(summary = "Method to get a certain Select question object")
    @GetMapping(path = "/select/{id}")
    SelectQ findById(@PathVariable Long id){
        return selectRepository.getReferenceById(id);
    }



    /**
     * This is a method used to get the correct answer of a Select question object
     * @param id the ID of the question you are trying to search about
     * @return returns the string of the correct answer
     */
    @Operation(summary = "Method used to get the correct answer of a Select question object")
    @GetMapping(path = "/select/getanswer1/{id}")
    String getAnswer1(@PathVariable Long id){
        SelectQ temp = selectRepository.getReferenceById(id);
        return temp.answer1;
    }

    /**
     * This is a method used to get the first incorrect answer of a Select question object
     * @param id the ID of the question you are trying to search about
     * @return returns the string of the first incorrect answer
     */
    @Operation(summary = "Method used to get the first incorrect answer of a Select question object")
    @GetMapping(path = "/select/getanswer2/{id}")
    String getAnswer2(@PathVariable Long id){
        SelectQ temp = selectRepository.getReferenceById(id);
        return temp.answer2;
    }

    /**
     * This is a method used to get the second incorrect answer of a Select question object
     * @param id the ID of the question you are trying to search about
     * @return returns the string of the second answer
     */
    @Operation(summary = "Method used to get the second incorrect answer of a Select question object")
    @GetMapping(path = "/select/getanswer3/{id}")
    String getAnswer3(@PathVariable Long id){
        SelectQ temp = selectRepository.getReferenceById(id);
        return temp.answer3;
    }

    /**
     * This is a method used to get the first third answer of a Select question object
     * @param id the ID of the question you are trying to find
     * @return returns the string of the third incorrect answer
     */
    @Operation(summary = "Method used to get the third incorrect answer of a Select question object")
    @GetMapping(path = "/select/getanswer4/{id}")
    String getAnswer4(@PathVariable Long id){
        SelectQ temp = selectRepository.getReferenceById(id);
        return temp.answer4;
    }

    /**
     * this is a method used to get the question of a Select question object
     * @param id the ID of the question you are trying to search about
     * @return returns the string of the question
     */
    @Operation(summary = "Method to get the question of a Select question object")
    @GetMapping(path = "/select/getquestion/{id}")
    String getQuestion(@PathVariable Long id){
        SelectQ temp = selectRepository.getReferenceById(id);
        return temp.question;
    }

    @Operation(summary = "Method to get the question of a Select question object")
    @GetMapping(path = "/select/answer1boolean/{id}")
    String getAnswer1Boolean(@PathVariable Long id){
        SelectQ temp = selectRepository.getReferenceById(id);
        return temp.getAnswer1correct();
    }

    @Operation(summary = "Method to get the question of a Select question object")
    @GetMapping(path = "/select/answer2boolean/{id}")
    String getAnswer2Boolean(@PathVariable Long id){
        SelectQ temp = selectRepository.getReferenceById(id);
        return temp.getAnswer2correct();
    }

    @Operation(summary = "Method to get the question of a Select question object")
    @GetMapping(path = "/select/answer3boolean/{id}")
    String getAnswer3Boolean(@PathVariable Long id){
        SelectQ temp = selectRepository.getReferenceById(id);
        return temp.getAnswer3correct();
    }

    @Operation(summary = "Method to get the question of a Select question object")
    @GetMapping(path = "/select/answer4boolean/{id}")
    String getAnswer4Boolean(@PathVariable Long id){
        SelectQ temp = selectRepository.getReferenceById(id);
        return temp.getAnswer4correct();
    }

    /**
     * this is a method used to delete a Select question object from its respective repository
     * @param id the ID of the question you are trying to search about
     * @return a string based that reads either success or failure depending on if the delete task worked
     */

    @Transactional
    @Operation(summary = "Method used to delete a Select question object from its respective repository")
    @DeleteMapping(path = "/select/{id}")
    String deleteSelect(@PathVariable Long id){
        selectRepository.deleteById(id);
        return success;
    }





    /**
     * This is a method used to create a new Select question object
     * @param question requests a parsed string that contains the question, correct answer, and 3 incorrect answers
     *                 this is used with the SelectParser get the separate strings
     * @return a string based on if the task was able to be completed. Should expect to receive either success or failure
     */
    @Parameter(description = "The field must contain an answer, followed by either 'true' or 'false' for 4 answers, " +
            "then the question needs to follow. All of these must be seperated by '%3A'")
    @Operation(summary = "Method used to create a new Select question object")
    @PostMapping(path = "/select/create")
    String createSelectRB(@RequestBody String question){
        SelectParser temp = new SelectParser(question);
        SelectQ Select = new SelectQ(temp.getAnswer1(), temp.getAnswer2(), temp.getAnswer3(), temp.getAnswer4(),
                 temp.getQuestion(), temp.getAnswer1correct(), temp.getAnswer2correct(), temp.getAnswer3correct(), temp.getAnswer4correct());
        selectRepository.save(Select);
        return success;
    }


    /**
     * This is a method used to edit an already existing Select question object
     * @param question requests a parsed string that contains the question, correct answer, and 3 incorrect answers
     *      *                 this is used with the SelectParser get the separate strings
     * @return a string based on if the task was able to be completed. Should expect to receive either success or failure
     */

    @Parameter(description = "The field must contain the question id, correct answer, 3 inncorrect answers and the question in that order seperated by '%3A'")
    @Operation(summary = "Method used to edit an already existing Select question object")
    @PutMapping(path = "select/edit")
    String editSelect(@RequestBody String question){
        try {
            SelectParserID temp = new SelectParserID(question);
            SelectQ old = selectRepository.getReferenceById((long)temp.getID());
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
            if(temp.getAnswer1correct() != null) {
                old.setAnswer1correct(temp.getAnswer1correct());
            }
            if(temp.getAnswer2correct() != null) {
                old.setAnswer2correct(temp.getAnswer2correct());
            }
            if(temp.getAnswer3correct() != null) {
                old.setAnswer3correct(temp.getAnswer3correct());
            }
            if(temp.getAnswer4correct() != null) {
                old.setAnswer4correct(temp.getAnswer4correct());
            }
            selectRepository.save(old);
        }catch(Exception e){
            return failure;
        }


        return success;
    }





}