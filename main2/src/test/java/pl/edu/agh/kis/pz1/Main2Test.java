package pl.edu.agh.kis.pz1;

import static junit.framework.TestCase.assertNotNull;
import org.junit.Test;


public class Main2Test {


    /**
     * Test for the construction of Main and the 
     * main method being called
     *
     */
    @Test
    public void shouldCreateMainObject(){
        Main2 main = new Main2();
        assertNotNull("Main method called on class Main2.", main);
    }
}


