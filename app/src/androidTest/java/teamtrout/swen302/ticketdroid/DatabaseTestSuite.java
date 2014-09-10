package teamtrout.swen302.ticketdroid;

import android.test.AndroidTestCase;

public class DatabaseTestSuite extends AndroidTestCase {

    //***********************************Testing Valid inputs********************************

    /**
     * Testing creation of one account
     */
    public void testCreate1() {
        Database db = createDatabase("Troy", "Password");

        if(!db.validUser("Troy","Password")){
            fail("Should be a valid account");
        }
    }

    /**
     * Testing creation of two accounts
     */
    public void testValid2() {
        Database db = createDatabase("Troy", "Password");
        db.addAccount(getContext(),"Marc","Password");

        if(!db.validUser("Troy","Password")){
            fail("Should be a valid account");
        }
    }

    /**
     * Testing that username is not case sensitive
     */
    public void testValid3_caseSensitive_name() {
        Database db = createDatabase("Troy", "Password");

        if(!db.validUser("troy","Password")){
            fail("Should be a valid account");
        }
    }

    public void testValid4() {
        //fail("Not yet implemented");
    }

    //***********************Testing invalid inputs******************************

    /**
     * Testing incorrect user name
     */
    public void testInvalid_Name(){
        Database db = createDatabase("Troy", "Password");

        if(db.validUser("Tory","Password")){
            fail("Should not be a valid account. Wrong username");
        }

    }

    /**
     * Testing incorrect password
     */
    public void testInvalid_Password(){
        Database db = createDatabase("Troy", "Password");

        if(db.validUser("Troy","Pw")){
            fail("Should not be a valid account. Wrong Password");
        }

    }

    /**
     * Testing case sensitivity for password
     */
    public void testInvalid_CaseSensitive_Password(){
        Database db = createDatabase("Troy", "Password");

        if(db.validUser("Troy","password")){
            fail("Should not be a valid account. Incorrect case sensitivity");
        }
    }

    /**
     * Testing for empty input on name field
     */
    public void testInvalid_Empty_Name(){
        Database db = createDatabase("Troy", "Password");

        if(db.validUser("","Password")){
            fail("Should not be a valid account. Empty name field");
        }
    }

    /**
     * Testing for empty input on password field
     */
    public void testInvalid_Empty_Password(){
        Database db = createDatabase("Troy", "Password");

        if(db.validUser("Troy","")){
            fail("Should not be a valid account. Empty password field");
        }
    }

    /**
     * Testing for password mismatch
     */
    public void testPasswordMismatch(){
        Database db = createDatabase("Troy", "Password");
        db.addAccount(getContext(),"Marc","1234");

        if(db.validUser("Troy","1234")){
            fail("Password does not belong to user, should fail");
        }
    }

    public void testEmptyUser(){
        Database db = new Database(getContext());

        assertFalse(db.addAccount(getContext(),"", "Password"));
    }

    public void testEmptyPassword(){
        Database db = new Database(getContext());

        assertFalse(db.addAccount(getContext(),"User",""));
    }

    //************************************Helper methods*************************************

    private Database createDatabase(String user, String password){
        Database db = new Database(getContext());
        db.addAccount(getContext(),user,password);
        return db;
    }
}