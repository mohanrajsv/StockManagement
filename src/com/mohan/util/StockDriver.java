package com.mohan.util;
import com.mohan.Inventory;
import com.mohan.Stocks;
import com.mohan.StocksRoot;

import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class StockDriver {
    StocksRoot stroot = new StocksRoot();

    //Logger utils
    static Logger logger = Logger.getLogger(Inventory.class.getName());
    static FileHandler fh;

    StockDriver() throws IOException {
        logger.setUseParentHandlers(false); //turned off the console output log
        fh = new FileHandler("Stocks.log",5000,1);
        logger.addHandler(fh); // add file handler to the loggger
        fh.setFormatter(new SimpleFormatter()); // setting formamtter for json
    }

    //delete stocks from inventory
    public void deleteStockInInventory()
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
            System.out.println("INFO : " + itcode + " is not found in our inventory.!");
            logger.warning(itcode + " is not found in our inventory.!");
        }
    }


    public void updateStockInInventory()
    {
        Scanner inc = new Scanner(System.in);
        inc.nextLine(); // to clear buffer
        System.out.println("\t\t UPDATE ITEM");
        Iterator<Stocks> itr = stroot.getStocks().iterator();
        System.out.print("Enter ItemCode : " );
        String itcode = inc.next().trim();
        int found = 0 ;
        while(itr.hasNext())
        {
            Stocks st = (Stocks) itr.next();
            if (st.getItemCode().equals(itcode))
            {
                System.out.println("Which one you want to update ? ");
                System.out.println("1. Name  2. Price  3. Description  4. Exit ");
                System.out.print("Enter ( 1 / 2 / 3 / 4 ) : ");

                int choice = Integer.parseInt(inc.next().trim());

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
            System.out.println(itcode + " is not found in our inventory.!");
            logger.warning(itcode + " is not found in our inventory.!");
        }
    }


}
