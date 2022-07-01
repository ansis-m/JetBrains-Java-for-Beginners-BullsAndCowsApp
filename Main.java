package bullscows;

import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        int size = getSize(); //ask user for the size of the secret code
        int[] secretCode = new int[size];
        int symbols = getSymbols(size); //ask user the number of symbols
        getRandomCode(secretCode, size, symbols); //generate a random code of the correct size and correct symbols
        printStartingMessages(size, symbols); //start the game

        for(int i = 1; ;i++) { //infinite loop until the user finds the correct combination
            int[] userInput = new int[size]; //int array to store user input. chars are also stored as ints
            System.out.printf("Turn %d:\n", i);
            getUserInput(userInput, size, symbols); //get user input the guess for the secret code
            int bulls = countBulls(secretCode, userInput, size); //count bulls
            int cows = countCows(secretCode, userInput, size); //count cows
            if(bulls == size) { //if all are bulls print congratulation and exit the infinite loop
                System.out.printf("Grade: %d bulls\n" +
                        "Congratulations! You guessed the secret code.", size);
                break;
            }
            printResult(bulls, cows); //if correct combination was not guessed print the number of cows and bulls
        }
    }

    public static int getSize() {

        System.out.println("Input the length of the secret code:");
        Scanner scanner = new Scanner(System.in);

        int result = 4;
        try{ //getting user input. If wrong format catch the exception and exit.
            result = scanner.nextInt();
        }
        catch (Exception e) {
            System.out.println("format error");
            System.exit(1);
        }

        if (result > 36 || result < 1) {
            System.out.printf("error: can't generate a secret code with a length of %d.\n", result);
            System.exit(1);
        }
        return result;
    }

    public static int getSymbols(int len) {

        System.out.println("Input the number of possible symbols in the code:");
        Scanner scanner = new Scanner(System.in);
        int result = scanner.nextInt();
        if (result > 36 || result < len) {
            System.out.printf("error: can't generate a secret code with %d characters.\n", result);
            System.exit(1);
        }
        return result;
    }

    public static void getRandomCode(int[] secretCode, int size, int symbols) {

        Random random = new Random(System.currentTimeMillis());
        for(int i = 0; i < size; i++) {
            int n = random.nextInt(symbols);
            secretCode[i] = n;
            for(int j = 0; j < i; j++) {
                if (n == secretCode[j]) {
                    i--;
                    break;
                }
            }
        }
    }

    public static void printStartingMessages(int size, int symbols) {
        System.out.print("The secret is prepared: ");
        for(int i = 0; i < size; i++)
            System.out.print("*");
        if (symbols <= 10)
            System.out.printf(" (0-%d).", symbols - 1);
        else if (symbols == 11)
            System.out.print(" (0-9, a).");
        else
            System.out.printf(" (0-9, a-%c).", symbols - 11 + 'a');
        System.out.println("\nOkay, let's start a game!");
    }

    public static void getUserInput(int[] userInput, int size, int symbols) {

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        char[] array = input.toCharArray();
        if(array.length != size) {
            badInput(size);
            return;
        }
        for(int i = 0; i < size; i++) { //transfer the user input to int array. chars are converted to ints

            if(array[i] >= '0' && array[i] <= '9') {
                if(array[i] - '0' > symbols)
                    badInput(size);
                userInput[i] = array[i] - '0'; //convert the digits to ints
            }
            else if(array[i] >= 'a' && array[i] <= 'z') {
                if(array[i] - 'a' + 10 > symbols)
                    badInput(size);
                userInput[i] = array[i] - 'a' + 10; //convert the letters to ints
            }
        }
    }

    public static void badInput(int size) {
        System.out.printf("error: %d digits only.\n", size);
        System.exit(1);
    }

    public static int countBulls(int[] secretCode, int[] userInput, int size) {
        int bulls = 0;
        for (int i = 0; i < size; i++)
            if (secretCode[i] == userInput[i])
                bulls++;
        return bulls;
    }

    public static int countCows(int[] secretCode, int[] userInput, int size) {

        int cows = 0;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if(i != j && secretCode[j] == userInput[i])
                    cows++;
            }
        }

        return cows;
    }

    public static void printResult(int bulls, int cows) {

        String c;
        String b;

        if(cows > 1)
            c = "s";
        else
            c = "";

        if(bulls > 1)
            b = "s";
        else
            b = "";

        if (bulls == 0 && cows == 0)
            System.out.print("Grade: None.\n");
        else if (bulls == 0)
            System.out.printf("Grade: %d cow%s.\n", cows, c);
        else if (cows == 0)
            System.out.printf("Grade: %d bull%s.", bulls, b);
        else
            System.out.printf("Grade: %d bull%s and %d cow%s.", bulls, b, cows, c);
    }
}
