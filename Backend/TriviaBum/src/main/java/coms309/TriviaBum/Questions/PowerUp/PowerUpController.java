package coms309.TriviaBum.Questions.PowerUp;

import coms309.TriviaBum.Questions.PowerUp.PowerUpRepository;
import coms309.TriviaBum.Users.User;
import coms309.TriviaBum.Users.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@RestController
public class PowerUpController {
    @Autowired
    PowerUpRepository powerUpRepository;
    @Autowired
    UserRepository userRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

//    @Operation(summary = "Method to get all powerups")
//    @GetMapping(path = "powerups")
//    List<Powerup> getAllPowerups(){
//        return powerUpRepository.findAll();
//    }

    @Operation(summary = "Method to get all powerups from user")
    @GetMapping(path = "powerups/{user}")
    String getUserPowerups(@PathVariable String user){
        User user1 = userRepository.findByPlayerUsername(user);
        if (user1.getPowerup() == null){
            return "0%3A0%3A0%3A";
        }
        else{
            return(Integer.toString(user1.getPowerup().getDoublePointsCount()) + "%3A" +
                    Integer.toString(user1.getPowerup().getTimeFreezeCount()) + "%3A"
                    + Integer.toString(user1.getPowerup().getTrollCount()));
        }
    }

    @Operation(summary = "Method to give a user a new powerup")
    @PutMapping(path = "powerup/new/{user}")
    String GivePowerup(@PathVariable String user){

        Powerup temp = userRepository.findByPlayerUsername(user).getPowerup();
            Random ran = new Random();
            int i = ran.nextInt(3) + 1;
            if (i == 1) {
                temp.setTimeFreezeCount(temp.getTimeFreezeCount() + 1);
                powerUpRepository.save(temp);
                return "Time Freeze Power-up Received!";
            }
            if (i == 2) {
                temp.setDoublePointsCount(temp.getDoublePointsCount() + 1);
                powerUpRepository.save(temp);
                return "Double Points Power-up Received!";
            }
            else {
                temp.setTrollCount(temp.getTrollCount() + 1);
                powerUpRepository.save(temp);
                return "Gamble Power-up Received!";
            }
    }

    @Operation(summary = "Method to edit all powerups from a user")
    @PutMapping(path = "powerups/{user}/{freeze}/{x2}/{troll}")
    String EdittUserPowerups(@PathVariable String user, @PathVariable int freeze,
                            @PathVariable int x2,@PathVariable int troll){
        //finish writing code to delete powerup. Need to check if powerup exists, then clear it
        //then save the user and return success, make failure case as well
        User temp = userRepository.findByPlayerUsername(user);
        if (temp.getPowerup() == null){
            return failure;
        }
        temp.getPowerup().setDoublePointsCount(x2);
        temp.getPowerup().setTimeFreezeCount(freeze);
        temp.getPowerup().setTrollCount(troll);

        powerUpRepository.save(temp.getPowerup());
        return success;
    }

    @Operation(summary = "Method to delete all powerups from a user (set all to 0)")
    @DeleteMapping(path = "powerups/{user}")
    String deleteUserPowerups(@PathVariable String user){
        //finish writing code to delete powerup. Need to check if powerup exists, then clear it
        //then save the user and return success, make failure case as well
        User temp = userRepository.findByPlayerUsername(user);
        if (temp.getPowerup() == null){
            return failure;
        }
        temp.getPowerup().setTrollCount(0);
        temp.getPowerup().setDoublePointsCount(0);
        temp.getPowerup().setTimeFreezeCount(0);

        powerUpRepository.save(temp.getPowerup());
        return success;
    }

    @Operation(summary = "Method to use a freeze powerup from a user")
    @PutMapping(path = "powerups/freeze/{user}")
    String useFreezePowerup(@PathVariable String user){
        User temp = userRepository.findByPlayerUsername(user);
        if (temp.getPowerup().getTimeFreezeCount() <= 0){
            return failure;
        }
        int tempP = (temp.getPowerup().getTimeFreezeCount()) - 1;
        temp.getPowerup().setTimeFreezeCount(tempP);
        powerUpRepository.save(temp.getPowerup());
        return success;
    }

    @Operation(summary = "Method to use a double points powerup from a user")
    @PutMapping(path = "powerups/double/{user}")
    String useDoublePointsPowerup(@PathVariable String user){
        User temp = userRepository.findByPlayerUsername(user);
        if (temp.getPowerup().getDoublePointsCount() <= 0){
            return failure;
        }
        int tempP = (temp.getPowerup().getDoublePointsCount()) - 1;
        temp.getPowerup().setDoublePointsCount(tempP);
        powerUpRepository.save(temp.getPowerup());
        return success;
    }

    @Operation(summary = "Method to use a troll powerup from a user")
    @PutMapping(path = "powerups/troll/{user}")
    String useTrollPowerups(@PathVariable String user){
        User temp = userRepository.findByPlayerUsername(user);
        if (temp.getPowerup().getTrollCount() <= 0){
            return failure;
        }
        int tempP = (temp.getPowerup().getTrollCount()) - 1;
        temp.getPowerup().setTrollCount(tempP);
        powerUpRepository.save(temp.getPowerup());
        return success;
    }
}
