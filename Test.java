import java.util.Scanner;

/**
 * Created by DJ on 1/29/2018.
 */
public class Test {

    public static void main (String args[])
    {
        System.out.println("enter grades");

        System.out.println("how many grades would you like to enter?");

        Scanner input = new Scanner(System.in);

        int numGrades = input.nextInt();

        System.out.println("enter your grades");
        String[] grades = new String[numGrades];

        for(int i = 0; i<=numGrades;i++)
        {
            grades[i] = input.next();
        }





        for(int i = 0; i<=numGrades;i++)
        {
            System.out.println(grades[i]);
        }




    }
}
