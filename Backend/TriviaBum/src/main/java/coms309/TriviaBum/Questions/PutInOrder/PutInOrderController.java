package coms309.TriviaBum.Questions.PutInOrder;

import coms309.TriviaBum.Helpers.Parsers.Q4RequestParser;
import coms309.TriviaBum.Helpers.Parsers.Q4RequestParserID;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;


@RestController
public class PutInOrderController {
    @Autowired
    PutInOrderRepository PutInOrderRepository;
    private final String success = "{\"message\":\"success\"}";
    private final String failure = "{\"message\":\"failure\"}";


    /**
     * Method to get all putinorder questions
     * @return an array of all of the putinorder question objects
     */
    @Operation(summary = "Method to get all questions")
    @GetMapping(path = "/putinorder")
    List<PutInOrder> getAllQuestions(){
        return PutInOrderRepository.findAll();
    }

    /**
     * Method to get a certain putinorder question object
     * @param id The ID of the question you are trying to find
     * @return the putinorder object associated with the ID given
     */
    @Operation(summary = "Method to get a certain putinorder question object")
    @GetMapping(path = "/putinorder/{id}")
    PutInOrder findById(@PathVariable Long id){
        return PutInOrderRepository.getReferenceById(id);
    }


    /**
     * This is a method used to get the correct answer of a putinorder question object
     * @param id the ID of the question you are trying to search about
     * @return returns the string of the correct answer
     */
    @Operation(summary = "Method used to get the correct answer of a putinorder question object")
    @GetMapping(path = "/putinorder/getanswer1/{id}")
    String getAnswer1(@PathVariable Long id){
        PutInOrder temp = PutInOrderRepository.getReferenceById(id);
        return temp.answer1;
    }

    /**
     * This is a method used to get the first incorrect answer of a putinorder question object
     * @param id the ID of the question you are trying to search about
     * @return returns the string of the first incorrect answer
     */
    @Operation(summary = "Method used to get the first incorrect answer of a putinorder question object")
    @GetMapping(path = "/putinorder/getanswer2/{id}")
    String getAnswer2(@PathVariable Long id){
        PutInOrder temp = PutInOrderRepository.getReferenceById(id);
        return temp.answer2;
    }

    /**
     * This is a method used to get the second incorrect answer of a putinorder question object
     * @param id the ID of the question you are trying to search about
     * @return returns the string of the second answer
     */
    @Operation(summary = "Method used to get the second incorrect answer of a putinorder question object")
    @GetMapping(path = "/putinorder/getanswer3/{id}")
    String getAnswer3(@PathVariable Long id){
        PutInOrder temp = PutInOrderRepository.getReferenceById(id);
        return temp.answer3;
    }

    /**
     * This is a method used to get the first third answer of a putinorder question object
     * @param id the ID of the question you are trying to find
     * @return returns the string of the third incorrect answer
     */
    @Operation(summary = "Method used to get the third incorrect answer of a putinorder question object")
    @GetMapping(path = "/putinorder/getanswer4/{id}")
    String getAnswer4(@PathVariable Long id){
        PutInOrder temp = PutInOrderRepository.getReferenceById(id);
        return temp.answer4;
    }

    /**
     * this is a method used to get the question of a putinorder question object
     * @param id the ID of the question you are trying to search about
     * @return returns the string of the question
     */
    @Operation(summary = "Method to get the question of a putinorder question object")
    @GetMapping(path = "/putinorder/getquestion/{id}")
    String getQuestion(@PathVariable Long id){
        PutInOrder temp = PutInOrderRepository.getReferenceById(id);
        return temp.question;
    }

    /**
     * this is a method used to delete a putinorder question object from its respective repository
     * @param id the ID of the question you are trying to search about
     * @return a string based that reads either success or failure depending on if the delete task worked
     */
    @Operation(summary = "Method used to delete a putinorder question object from its respective repository")
    @DeleteMapping(path = "/putinorder/{id}")
    String deletePutInOrderQuestion(@PathVariable Long id){
        PutInOrderRepository.deleteById(id);
        return success;
    }

    /**
     * This is a method used to create a new putinorder question object
     * @param question requests a parsed string that contains the question, correct answer, and 3 incorrect answers
     *                 this is used with the Q4parser get the separate strings
     * @return a string based on if the task was able to be completed. Should expect to receive either success or failure
     */
    @Parameter(description = "The field must contain the correct answer, 3 inncorrect answers and the question in that order seperated by '%3A'")
    @Operation(summary = "Method used to create a new putinorder question object")
    @PostMapping(path = "/putinorder/create")
    String createPutInOrderRB(@RequestBody String question){
        Q4RequestParser temp = new Q4RequestParser(question);
        PutInOrder put = new PutInOrder(temp.getAnswer1(), temp.getAnswer2(), temp.getAnswer3(),
                temp.getAnswer4(), temp.getQuestion());
        PutInOrderRepository.save(put);
        return success;
    }


    /**
     * This is a method used to edit an already existing putinorder question object
     * @param question requests a parsed string that contains the question, correct answer, and 3 incorrect answers
     *      *                 this is used with the Q4parser get the separate strings
     * @return a string based on if the task was able to be completed. Should expect to receive either success or failure
     */

    @Parameter(description = "The field must contain the question id, correct answer, 3 inncorrect answers and the question in that order seperated by '%3A'")
    @Operation(summary = "Method used to edit an already existing putinorder question object")
    @PutMapping(path = "putinorder/edit")
    String editPutInOrder(@RequestBody String question){
        try {
            Q4RequestParserID temp = new Q4RequestParserID(question);
            PutInOrder old = PutInOrderRepository.getReferenceById((long)temp.getID());
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
            PutInOrderRepository.save(old);
        }catch(Exception e){
            return failure;
        }


        return success;
    }





}