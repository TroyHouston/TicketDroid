package tests;

import static org.junit.Assert.*;

import org.junit.Test;

public class LoginTestSuite {


	//***********************************Testing Valid inputs********************************
	/**
	 * Testing one account login
	 */
	@Test
	public void testValid1() {

		Database db = createAccount("Troy","Password");

		if(!db.validUser("Troy","Password")){
			fail("Should be a valid account");
		}
	}

	/**
	 * Testing two account login
	 */
	@Test
	public void testValid2() {
		Database db = createAccount("Troy","Password");
		db.addAccount("Marc","Password");

		if(!db.validUser("Troy","Password")){
			fail("Should be a valid account");
		}
	}

	/**
	 * Testing that username is not case sensitive
	 */
	@Test
	public void testValid3_Casesensitive_Name() {
		Database db = createAccount("Troy","Password");

		if(!db.validUser("troy","Password")){
			fail("Should be a valid account");
		}
	}

	@Test
	public void testValid4() {
		//fail("Not yet implemented");
	}

	//***********************Testing invalid inputs******************************

	/**
	 * Testing incorrect user name
	 */
	@Test
	public void testInvalid_Name(){
		Database db = createAccount("Troy","Password");

		if(db.validUser("Tory","Password")){
			fail("Should not be a valid account. Wrong username");
		}

	}

	/**
	 * Testing incorrect password
	 */
	@Test
	public void testInvalid_Password(){
		Database db = createAccount("Troy","Password");

		if(db.validUser("Troy","Pw")){
			fail("Should not be a valid account. Wrong Password");
		}

	}

	/**
	 * Testing case sensitivity for password
	 */
	@Test
	public void testInvalid_CaseSensitive_Password(){
		Database db = createAccount("Troy","Password");

		if(db.validUser("Troy","password")){
			fail("Should not be a valid account. Incorrect case sensitivity");
		}

	}


	/**
	 * Testing for empty input on name field
	 */
	@Test
	public void testInvalid_Empty_Name(){
		Database db = createAccount("Troy","Password");

		if(db.validUser("","Password")){
			fail("Should not be a valid account. Empty name field");
		}

	}

	/**
	 * Testing for empty input on password field
	 */
	@Test
	public void testInvalid_Empty_Password(){
		Database db = createAccount("Troy","Password");

		if(db.validUser("Troy","")){
			fail("Should not be a valid account. Empty password field");
		}

	}



	//************************************Helper methods*************************************

	private Database createAccount(String user , String password){
		Database db = new Database();
		db.addAccount(user,password);
		return db;
	}

}
