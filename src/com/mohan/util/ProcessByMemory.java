package com.mohan.util;
import com.mohan.Inventory;
import com.mohan.Stocks;
import org.json.*;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ProcessByMemory extends Stocks  implements Inventory {
    //logger utils
    static Logger logger = Logger.getLogger(ProcessByMemory.class.getName());
    static FileHandler fh;

    //initialize things for ProcessByMemory
    public ProcessByMemory() throws IOException, JSONException, IOException {
        logger.setUseParentHandlers(false); //turned off the console output log
        fh = new FileHandler("MemoryStocks.log",5000,1,true);
        logger.addHandler(fh); // add file handler to the loggger
        fh.setFormatter(new SimpleFormatter()); // setting formamtter for json
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
                System.out.println("\tProduct added to the inventory.!");
                logger.info(s.getItemName()+" is added to the inventory");
            }
            else
            {
                System.err.println("Specified item code is already available in inventory!");
                logger.warning(s.getItemCode()+" redundant item insertion failed");
            }
        } catch (Exception e) {
            System.err.println("INVALID DATA IS GIVEN");
            e.printStackTrace();
            addStock();
        }
    }

    //delete stock data
    public void deleteStock()
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("\t\t DELETE ITEM");
        Iterator itr = stroot.getStocks().iterator();
        System.out.println("Enter ItemCode : " );
        String itcode = sc.next().trim();
        int found = 0 ;
        while(itr.hasNext())
        {
            Stocks st = (Stocks) itr.next();
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
            System.err.println(itcode + " is not found in our inventory.!");
            logger.warning(itcode + " is not found in our inventory.!");
        }
    }

    //exit from system
    void exitFromSystem()
    {
        System.out.println("Thank you");
        fh.close();
    }

    //update stocks in the inventory
    public void updateStock()
    {
        Scanner inc = new Scanner(System.in);
        System.out.println("\t\t UPDATE ITEM");
        Iterator<Stocks> itr =  stroot.getStocks().iterator();
        System.out.println("Enter ItemCode : " );
        String itcode = inc.next().trim();
        int found = 0, choice = 0 ;
        try {
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
                            System.out.println("Invalid choice");
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
        } catch (NumberFormatException e) {
            System.err.println("INFO : Invalid choice");
            logger.warning("Invalid choice has been pressed while updating!");
            updateStock();
        }
        if ( found == 0)
        {
            System.err.println(itcode + " is not found in our inventory.!");
            logger.warning(itcode + " is not found in our inventory.!");
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
                try
                {
                    System.out.print("\nEnter ( 1 / 2 / 3 / 4 / 5 ) : ");
                    choice = inc.nextInt();
                }
                catch (Exception e)
                {
                    System.err.println("Invalid choice");
                }
                inc.nextLine();
            } while (choice <= 0  || choice > 5);

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
                    System.out.println("INFO : Invalid choice");
                    break;
            }
        } while(choice != 5);
    }
}

