package coms309.TriviaBum;

import static org.junit.jupiter.api.Assertions.assertEquals;

import coms309.TriviaBum.Questions.PutInOrder.PutInOrder;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.boot.test.web.server.LocalServerPort;// SBv3


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class YonasSystemTest {

    @LocalServerPort
    int port = 8080;

    static final String BASE_WEBSOCKET_URI = "ws://localhost:8080/";

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void friendslistTest() {
        // Send request and receive response
        Response response = RestAssured.given().
                //header("Content-Type", "text/plain").
                //header("charset","utf-8").
                //body("hello").
                        when().
                get("/friends/test");


        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString.split(" "));
            //JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals("ethan", returnArr.get(1));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void totalcorrectTest() throws JSONException {
        // Send request and receive response
        Response response = RestAssured.given().
                //header("Content-Type", "text/plain").
                //header("charset","utf-8").
                //body("").
                        when().
                get("/users/test/totalcorrect");


        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        int num = response.getBody().as(Integer.class);
        assertEquals(116, num);
    }

    @Test
    public void createNewFillBlankQuestionTest() throws JSONException {
        // Send request and receive response
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset", "utf-8").
                body("yes%3Ais this a test").
                when().
                post("/fillblank/create");


        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        // Check response body for correct response
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString.split(" "));
            //JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals("{\"message\":\"success\"}", returnArr.get(0));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void deleteExistingMultitpleChoiceQuestion() throws JSONException {
        // Send request and receive response
        Response response = RestAssured.given().
                when().
                delete("/multiplechoicequestion/2");


        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        // Check response body for correct response
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString.split(" "));
            //JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals("{\"message\":\"success\"}", returnArr.get(0));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void createNewMultQuestionTest() throws JSONException {
        // Send request and receive response
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset", "utf-8").
                body("yes%3Ano%3Ano%3Ano%3Ais this a test").
                when().
                post("/multiplechoicequestion/create");


        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        // Check response body for correct response
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString.split(" "));
            //JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals("{\"message\":\"success\"}", returnArr.get(0));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void createNewSelectTest() throws JSONException {
        // Send request and receive response
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset", "utf-8").
                body("yes%3Atrue%3Ano%3Afalse%3Ano%3Afalse%3Ano%3Afalse%3Ais this a test").
                when().
                post("/select/create");


        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        // Check response body for correct response
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString.split(" "));
            //JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals("{\"message\":\"success\"}", returnArr.get(0));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void createNewTrueFalseTest() throws JSONException {
        // Send request and receive response
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset", "utf-8").
                body("yes%3Afalse%3Ais this a test").
                when().
                post("/truefalse/create");


        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        // Check response body for correct response
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString.split(" "));
            //JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals("{\"message\":\"success\"}", returnArr.get(0));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void editQuestionsTest() throws JSONException {
        // Send request and receive response
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset", "utf-8").
                body("1%3Ayes%3Atrue%3Ano%3Afalse%3Ano%3Afalse%3Ano%3Afalse%3Ais this a test").
                when().
                put("/select/edit");

        Response response2 = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset", "utf-8").
                body("1%3Ayes%3Ano%3Afalse%3Ano%3Ais this a test").
                when().
                put("/multiplechoicequestion/edit");

        Response response3 = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset", "utf-8").
                body("1%3Ayes%3Ano%3Ais this a test").
                when().
                put("/truefalse/edit");

        Response response4 = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset", "utf-8").
                body("1%3Ayesis this a test").
                when().
                put("/fillblank/edit");


        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        // Check response body for correct response
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString.split(" "));
            //JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals("{\"message\":\"failure\"}", returnArr.get(0));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void getQuestions() throws JSONException {
        // Send request and receive response
        Response response = RestAssured.given().
                when().
                get("/truefalse");
        Response response2 = RestAssured.given().
                when().
                get("/multiplechoicequestions");
        Response response3 = RestAssured.given().
                when().
                get("/putinorder");
        Response response4 = RestAssured.given().
                when().
                get("/select");
        Response response5 = RestAssured.given().
                when().
                get("/fillblank/");
        Response response6 = RestAssured.given().
                when().
                get("/multiplechoicequestions/getquestion/1");


        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

    }

    @Test
    public void leaderboardTest() throws JSONException {
        // Send request and receive response
        Response response = RestAssured.given().
                when().
                get("/leaderboard");
        Response response2 = RestAssured.given().
                when().
                get("/leaderboard/top");
        Response response3 = RestAssured.given().
                when().
                get("/leaderboard/top/orgs");
        Response response4 = RestAssured.given().
                when().
                get("/leaderboard/top/org/test");
        Response response5 = RestAssured.given().
                when().
                get("/leaderboard/top/friends/bro");


        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

    }

    @Test
    public void organizationTest() throws JSONException {
        // Send request and receive response
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset", "utf-8").
                body("testorg").
                when().
                get("/organizations");
        Response response2 = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset", "utf-8").
                body("testorg").
                when().
                post("/organizations/create");
        Response response3 = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset", "utf-8").
                body("testorg").
                when().
                get("/userorg/bro");
        Response response4 = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset", "utf-8").
                body("testorg").
                when().
                post("/organizations/post/bacon");
        Response response5 = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset", "utf-8").
                body("testorg").
                when().
                put("/organizations/3");
        Response response6 = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset", "utf-8").
                body("testorg").
                when().
                delete("/organizations/2");
        Response response7 = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset", "utf-8").
                body("testorg").
                when().
                get("/organizations/salsa");
        Response response8 = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset", "utf-8").
                body("testorg").
                when().
                put("/organizations/2");
        Response response9 = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset", "utf-8").
                body("testorg").
                when().
                get("/organizations/bacon/users");
        Response response10 = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset", "utf-8").
                body("testorg").
                when().
                put("/organizations/2");


        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
    }

    @Test
    public void userTests() throws JSONException {

        // Send request and receive response
        Response response = RestAssured.given().
                //header("Content-Type", "text/plain").
                //header("charset","utf-8").
                //body("").
                        when().
                get("/users/test/totalcorrect");



        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        int num = response.getBody().as(Integer.class);
        assertEquals(116, num);
    }

    @Test
    public void extraFillBlankTests() throws JSONException {
        // Send request and receive response
        Response response = RestAssured.given().
                //header("Content-Type", "text/plain").
                //header("charset","utf-8").
                //body("").
                        when().
                get("/fillblanks");
        Response response2 = RestAssured.given().
                //header("Content-Type", "text/plain").
                //header("charset","utf-8").
                //body("").
                        when().
                get("/fillblank/getquestion/1");
        Response response3 = RestAssured.given().
                //header("Content-Type", "text/plain").
                //header("charset","utf-8").
                //body("").
                        when().
                get("/fillblanks");
        Response response5 = RestAssured.given().
                //header("Content-Type", "text/plain").
                //header("charset","utf-8").
                //body("").
                        when().
                get("/fillblank/getanswer1/1");
        Response response6 = RestAssured.given().
                //header("Content-Type", "text/plain").
                //header("charset","utf-8").
                //body("").
                        when().
                delete("/fillblank/1");
        Response response7 = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                body("1%3Abacon%3Abacon%3Ais bacon good").
                        when().
                put("/fillblank/edit");



        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
    }

    @Test
    public void extraMultipleTests() throws JSONException {
        // Send request and receive response
        Response response = RestAssured.given().
                //header("Content-Type", "text/plain").
                //header("charset","utf-8").
                //body("").
                        when().
                get("/multiplechoicequestion/1");
        Response response3 = RestAssured.given().
                //header("Content-Type", "text/plain").
                //header("charset","utf-8").
                //body("").
                        when().
                get("/multiplechoicequestion/getanswer1/1");
        Response response4 = RestAssured.given().
                //header("Content-Type", "text/plain").
                //header("charset","utf-8").
                //body("").
                        when().
                get("/multiplechoicequestion/getanswer2/1");
        Response response5 = RestAssured.given().
                //header("Content-Type", "text/plain").
                //header("charset","utf-8").
                //body("").
                        when().
                get("/multiplechoicequestion/getanswer3/1");
        Response response6 = RestAssured.given().
                //header("Content-Type", "text/plain").
                //header("charset","utf-8").
                //body("").
                        when().
                get("/multiplechoicequestion/getanswer4/1");
        Response response7 = RestAssured.given().
                //header("Content-Type", "text/plain").
                //header("charset","utf-8").
                //body("").
                        when().
                delete("/multiplechoicequestion/1");
        Response response8 = RestAssured.given().
                //header("Content-Type", "text/plain").
                //header("charset","utf-8").
                //body("").
                        when().
                put("/multiplechoicequestion/edit");

        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(500, statusCode);
    }

    @Test
    public void extraPutInOrderTests() throws JSONException {
        // Send request and receive response
        Response response = RestAssured.given().
                //header("Content-Type", "text/plain").
                //header("charset","utf-8").
                //body("").
                        when().
                get("/putinorder/1");
        Response response3 = RestAssured.given().
                //header("Content-Type", "text/plain").
                //header("charset","utf-8").
                //body("").
                        when().
                get("/putinorder/getanswer1/1");
        Response response4 = RestAssured.given().
                //header("Content-Type", "text/plain").
                //header("charset","utf-8").
                //body("").
                        when().
                get("/putinorder/getanswer2/1");
        Response response5 = RestAssured.given().
                //header("Content-Type", "text/plain").
                //header("charset","utf-8").
                //body("").
                        when().
                get("/putinorder/getanswer3/1");
        Response response6 = RestAssured.given().
                //header("Content-Type", "text/plain").
                //header("charset","utf-8").
                //body("").
                        when().
                get("/putinorder/getanswer4/1");
        Response response7 = RestAssured.given().
                //header("Content-Type", "text/plain").
                //header("charset","utf-8").
                //body("").
                        when().
                get("/putinorder/getquestion/1");
        Response response8 = RestAssured.given().
                //header("Content-Type", "text/plain").
                //header("charset","utf-8").
                //body("").
                        when().
                delete("/putinorder/1");
        Response response9 = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                body("").
                        when().
                post("/putinorder/create");
        Response response10 = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                body("1%3A1%3A2%3A3%3A4%3Awhich one is 1").
                        when().
                put("/putinorder/edit");

        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(500, statusCode);
    }

    @Test
    public void methodTest() {
        // Send request and receive response
        Response response = RestAssured.given().
                //header("Content-Type", "text/plain").
                //header("charset","utf-8").
                //body("hello").
                        when().
                get("/friends/test");


        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString.split(" "));
            //JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals("ethan", returnArr.get(1));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void extraSelectTests() {
        // Send request and receive response
        Response response = RestAssured.given()
                .when()
                .get("/select");
        Response response2 = RestAssured.given()
                .when()
                .get("/select/1");
        Response response3 = RestAssured.given()
                .when()
                .get("/select/getanswer1/1");
        Response response4 = RestAssured.given()
                .when()
                .get("/select/getanswer2/1");
        Response response5 = RestAssured.given()
                .when()
                .get("/select/getanswer3/1");
        Response response6 = RestAssured.given()
                .when()
                .get("/select/getanswer4/1");
        Response response13 = RestAssured.given()
                .when()
                .get("/select/answer1boolean/1");
        Response response14 = RestAssured.given()
                .when()
                .get("/select/answer2boolean/1");
        Response response15 = RestAssured.given()
                .when()
                .get("/select/answer3boolean/1");
        Response response16 = RestAssured.given()
                .when()
                .get("/select/answer4boolean/1");
        Response response7 = RestAssured.given()
                .when()
                .get("/select/getquestion/1");
        Response response8 = RestAssured.given()
                .when()
                .delete("/select/1");
        Response response9 = RestAssured.given()
                .contentType("text/plain")
                .body("")
                .when()
                .post("/select/create");
        Response response10 = RestAssured.given()
                .contentType("text/plain")
                .body("")
                .when()
                .put("/select/edit");

        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
    }

    @Test
    public void extraTrueFalseTests() {
        // Send request and receive response
        Response response = RestAssured.given()
                .when()
                .get("/truefalse");
        Response response2 = RestAssured.given()
                .when()
                .get("/truefalse/1");
        Response response3 = RestAssured.given()
                .when()
                .post("/truefalse/create");
        Response response4 = RestAssured.given()
                .when()
                .delete("/truefalse/1");
        Response response5 = RestAssured.given()
                .when()
                .put("/truefalse/edit");

        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
    }


    @Test
    public void extraUserControllerTests() {
        // Send request and receive response
        Response response = RestAssured.given()
                .when()
                .get("/users");
        Response response2 = RestAssured.given()
                .when()
                .post("/users/post/testusername/testpassword/0/1");
        Response response3 = RestAssured.given()
                .when()
                .get("/users/testusername");
        Response response4 = RestAssured.given()
                .when()
                .put("/users/1");
        Response response5 = RestAssured.given()
                .when()
                .delete("/users/1");
        Response response6 = RestAssured.given()
                .when()
                .get("/users/testusername/totalquestions");
        Response response7 = RestAssured.given()
                .when()
                .get("/users/testusername/totalcorrect");
        Response response8 = RestAssured.given()
                .when()
                .get("/users/testusername/currenstreak");
        Response response9 = RestAssured.given()
                .when()
                .get("/users/testusername/longeststreak");
        Response response10 = RestAssured.given()
                .when()
                .get("/users/testusername/correctratio");
        Response response11 = RestAssured.given()
                .when()
                .put("/userorg/1/1");
        Response response12 = RestAssured.given()
                .when()
                .get("/userorg/testusername");
        Response response13 = RestAssured.given()
                .when()
                .get("/friends/testusername");
        Response response14 = RestAssured.given()
                .when()
                .delete("/friends/testusername/testusername2");
        Response response15 = RestAssured.given()
                .when()
                .post("/friends/testusername/testusername2");

        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
    }




}
