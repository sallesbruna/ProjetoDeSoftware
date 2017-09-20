package exercicio;

import java.util.Scanner;

/**
 * Created by bruna on 20/09/2017.
 */
public class Console {
    public static String readLine(String s) {
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

    public static double readDouble(String s) {
        Scanner sc = new Scanner(System.in);
        return sc.nextDouble();
    }

    public static int readInt(String s) {
        Scanner sc = new Scanner(System.in);
        return sc.nextInt();
    }
}
