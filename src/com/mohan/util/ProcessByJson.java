package com.mohan.util;
import com.mohan.Inventory;
import com.mohan.Stocks;
import org.json.*;
import java.io.*;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import org.apache.commons.io.FileUtils;

public class ProcessByJson extends Stocks  implements Inventory {
    public static JSONArray jArr = new JSONArray();// json array

    //Logger utils
    static Logger logger = Logger.getLogger(ProcessByJson.class.getName());
    static FileHandler fh;

    //initialize things for parsing and getting data from json
    public ProcessByJson() throws IOException, JSONException
    {
        try
        {
            parseJsonFile();
        }
        catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        logger.setUseParentHandlers(false); //turned off the console output log
        fh = new FileHandler("JsonStocks.log",5000,1,true);
        logger.addHandler(fh); //add file handler to the loggger
        fh.setFormatter(new SimpleFormatter()); //setting formamtter for json
    }

    //putting stocks obj in json array
    public void putObjToJsonArray() throws JSONException {
        for (Stocks st : stroot.getStocks()) {
            JSONObject temp = new JSONObject(st.toString());
            jArr.put(temp);
        }
    }

    //adding stocks data
    public void addStock()
    {
        System.out.println("\t\t ADD STOCKS");
        Stocks s = new Stocks();
        try {
            s.setItemCode();
            if(!checkIfAlreadyExist(s.getItemCode()))
            {
                s.setItemName();
                s.setPrice();
                s.setDescription();
                stroot.getStocks().add(s);
                stroot.setStocks(stroot.getStocks());
                System.out.println("\tProduct added to the inventory.!");
                logger.info(s.getItemName()+" is added to the inventory");
            }
            else
            {
                System.out.println("INFO : Specified item code is already available in inventory!");
                logger.warning(s.getItemCode()+" redundant item insertion failed");
            }

        } catch (Exception e) {
            System.out.println("INFO : INVALID DATA IS GIVEN");
            e.printStackTrace();
            addStock();
        }
    }

    //delete stocks from inventory
    public void deleteStock()
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("\t\t DELETE ITEM");
        Iterator<Stocks> itr = stroot.getStocks().iterator();
        System.out.print("Enter ItemCode : " );
        String itcode = sc.next().trim();
        int found = 0 ;
        while(itr.hasNext())
        {
            Stocks st = itr.next();
            if (st.getItemCode().equals(itcode))
            {
                System.out.println(st.getItemName() + " is deleted successfully.!");
                logger.info(st.getItemName() + " is deleted successfully.!");
                itr.remove();
                found = 1;
                break;
            }
        }
        if ( found == 0)
        {
            System.err.println("INFO : " + itcode + " is not found in our inventory.!");
            logger.warning(itcode + " is not found in our inventory.!");
        }
    }

    //exit from JSON Model
    void exitFromSystem() {
        updateJsonFile();
        System.out.println( " Thank you ");
        fh.close(); // closing logger file handle
    }

    //update stocks in the inventory
    public void updateStock()
    {
        Scanner inc = new Scanner(System.in);
        inc.nextLine(); // to clear buffer
        System.out.println("\t\t UPDATE ITEM");
        Iterator<Stocks> itr = stroot.getStocks().iterator();
        System.out.print("Enter ItemCode : " );
        String itcode = inc.next().trim();
        int found = 0, choice = 0 ;
        while(itr.hasNext())
        {
            Stocks st = itr.next();
            if (st.getItemCode().equals(itcode))
            {
                System.out.println("Which one you want to update ? ");
                System.out.println("1. Name  2. Price  3. Description  4. Exit ");
                do {
                    try {
                        System.out.print("Enter ( 1 / 2 / 3 / 4 ) : ");
                        choice = inc.nextInt();
                    } catch (InputMismatchException e) {
                        System.err.println("Invalid choice");
                    }
                    inc.nextLine(); // clears the buffer
                } while (choice <= 0);

                if(choice == 1)
                {
                    String name = inc.nextLine().trim();
                    st.updateItemName(name);
                    logger.info(st.getItemCode() + " is updated with new name " + name);
                }
                else if(choice == 2)
                {
                    int price = inc.nextInt();
                    st.updatePrice(price);
                    logger.info(st.getItemCode() + " is updated with new price " + price);
                }
                else if(choice == 3)
                {
                    String desc = inc.nextLine().trim();
                    st.updateDescription(desc);
                    logger.info(st.getItemCode() + " is updated with new description " + desc);
                }
                else
                {
                    break;
                }
                System.out.println(st.getItemCode() + " is updated successfully.!");
                found = 1;
                break;
            }
        }
        if ( found == 0)
        {
            System.err.println(itcode + " is not found in our inventory.!");
            logger.warning(itcode + " is not found in our inventory.!");
        }
    }

    //parsing jSon file data and stroing it in the arrayList
    public void parseJsonFile() throws JSONException, IOException {
        try {
            File file = new File("StocksJson.json");
            if(file.exists() && file.length()>0 )
            {
                String content = FileUtils.readFileToString(file, "utf-8");
                // Convert JSON string to JSONObject
                JSONObject obj = new JSONObject(content);
                JSONArray arr = obj.getJSONArray("Stock");
                for (int i = 0; i < arr.length(); i++) {
                    String icode = arr.getJSONObject(i).getString("itemCode");
                    String iName = arr.getJSONObject(i).getString("itemName");
                    String iDesc = arr.getJSONObject(i).getString("description");
                    int iPrice = arr.getJSONObject(i).getInt("price");
                    Stocks tempStock = new Stocks(icode, iName, iPrice, iDesc);
                    if(!checkIfAlreadyExist(tempStock.getItemCode()))
                    {
                        stroot.getStocks().add(tempStock);
                    }
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    //updating json file
    public void updateJsonFile() {
        try {
            putObjToJsonArray();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject exportObj = new JSONObject();
        try {
            exportObj.put("Stock",jArr);
            File Jfile = new File("StocksJson.json");
            if(Jfile.delete()){
                Jfile.createNewFile();
            }
            FileWriter fw = new FileWriter(Jfile,false);
            fw.write(exportObj.toString());
            fw.close();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    //menu driven system
    public void menuCard()
    {
        Scanner inc = new Scanner(System.in);
        System.out.println("\n\t\t\t\t Main Menu");
        int choice = 0;
        do
        {
            System.out.println("\n\t1. Add Stock  2. Delete Stock  3. Display Item 4. Update Stock 5. Close ");
            do {
                try {
                    System.out.print("\nEnter ( 1 / 2 / 3 / 4 / 5 ) : ");
                    choice = inc.nextInt();
                } catch (InputMismatchException e) {
                    System.err.println("Invalid choice");
                }
            } while (choice <= 0 || choice > 5);
            switch (choice) {
                case 1:
                    addStock();
                    break;
                case 2:
                    deleteStock();
                    break;
                case 3:
                    displayStock();
                    break;
                case 4:
                    updateStock();
                    break;
                case 5:
                    exitFromSystem();
                    break;
                default:
                    System.err.println("INFO : Invalid choice");
                    break;
            }
        } while(choice != 5);
    }
}
