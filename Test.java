import java.util.Scanner;

/**
 * Created by DJ on 1/29/2018.
 */
public class Test {

    public static void main (String args[]) {
        String isPalin = "acca";

        int halfString = isPalin.length()/2;


        String stringTotal[] = new String[isPalin.length()];

        for (int i = 0; i < isPalin.length(); i++){

            stringTotal[i]= isPalin.substring(i);
        }

        for (int i = 0; i < isPalin.length()-1; i++) {
            System.out.println(stringTotal[i]);

        }





    }
}
