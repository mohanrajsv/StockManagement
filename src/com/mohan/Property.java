package com.mohan;
import java.io.*;
import java.util.Properties;
import java.util.Scanner;
public class Property {
    public static File file =  new File("config.properties");;
    Property() {
    }
    public static void setConfig() throws IOException {
        Properties ppt = new Properties();
        int choice = 0;
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        Scanner inc = new Scanner(System.in);
        FileOutputStream output = new FileOutputStream(file);
        System.out.println("\n 1. Process using Memory  2. Process Using XML  3. Process using JSon  4. Exit\n");
        do{
            try
            {
                System.out.print("\nEnter ( 1 / 2 / 3 / 4 / 5 ) : ");
                choice = inc.nextInt();
            }
            catch (Exception e)
            {
                System.err.println("Please Enter b/w [1-5]");
            }
            inc.nextLine();
        } while (choice <= 0);

        switch (choice)
        {
            case 1:
                ppt.setProperty("type", "memory");
                ppt.store(output, null);
                output.close();
                break;
            case 2:
                ppt.setProperty("type", "xml");
                ppt.store(output, null);
                output.close();
                break;
            case 3:
                ppt.setProperty("type", "json");
                ppt.store(output, null);
                output.close();
                break;
            case 4:
                System.out.println("INFO : Thank you for using Stockify");
                System.exit(0);
                output.close();
                break;
            default:
                System.out.println("INFO : Invalid choice");
                break;
        }
    }

    public static int readConfig() throws IOException {
        Properties ppt = new Properties();
        int choice = 0;
        FileInputStream input = null;
        try {
            input = new FileInputStream(file);
            ppt.load(input);
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String type = ppt.getProperty("type");

        if (type.equals("memory"))
            return 1;
        else if (type.equals("xml"))
            return 2;
        else if (type.equals("json"))
            return 3;
        return 4;
    }
}

