package pl.edu.agh.kis.pz1;

import pl.edu.agh.kis.pz1.util.TextUtils;

/**
 * Przykładowy kod do zajęć laboratoryjnych 2, 3, 4 z przedmiotu: Programowanie zaawansowane 1
 * @author Paweł Skrzyński
 */
public class Main1 {
    public static void main( String[] args ) {
        System.out.println( "Szablon projektu z dwiema metodami main i zależnościami wbudowanymi w wykonywalny jar" );
        //wywołanie metody generującej hash SHA-512
        System.out.println("HASH 512 dla słowa test: " + TextUtils.sha512Hash("test"));
    }
}
