package com.mohan;
import com.mohan.util.*;
import org.json.JSONException;
import org.xml.sax.SAXException;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Main extends Property{
    public static void main(String[] args){
        Inventory inventory = null;
        int flag = 0;
        System.out.println("\n\t\t :::::::::: Stockify STOCK MANAGEMENT SYSTEM :::::::::::::::");
        try {
            do {
                setConfig();
                flag = readConfig();

                if(flag == 1)
                {
                    inventory = new ProcessByMemory();
                    inventory.menuCard();
                }
                else if(flag == 2)
                {
                    inventory = new ProcessByXml();
                    inventory.menuCard();
                }
                else if (flag == 3)
                {
                    inventory = new ProcessByJson();
                    inventory.menuCard();
                }
                else{
                    System.out.println("\t\t Thank you for using Stockify :)");
                }
            } while (flag != 4);
        } catch (IOException | ParserConfigurationException | JSONException | SAXException | JAXBException e) {
            System.out.println("INFO : Invalid action performed");
            System.out.println(e.getMessage());
        }
    }
}
