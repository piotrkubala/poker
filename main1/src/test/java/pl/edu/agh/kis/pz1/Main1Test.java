package pl.edu.agh.kis.pz1;

import static junit.framework.TestCase.assertNotNull;
import org.junit.Test;


public class Main1Test {


    /**
     * Test for the construction of Main and the 
     * main method being called
     *
     */
    @Test
    public void shouldCreateMainObject(){
        Main1 main = new Main1();
        assertNotNull("Main method called in class Main1.", main);
    }
}


