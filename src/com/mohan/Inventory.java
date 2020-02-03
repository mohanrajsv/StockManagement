package com.mohan;
import java.util.Iterator;
public interface Inventory {
    StocksRoot stroot = new StocksRoot(); // ref created for access all the stocks in the inventory

    void addStock();//get stock details from memory or file

    void updateStock();//update stock details in inventory

    void deleteStock();//delete stock from inventory

     void menuCard(); //display menu

    default void displayStock()
    {

        System.out.println("\t\t STOCKS FROM STORE");
        Iterator<Stocks> itr =  stroot.getStocks().iterator();
        while(itr.hasNext())
        {
            Stocks st=itr.next();
            System.out.println("ITEM CODE : " + st.getItemCode());
            System.out.println("ITEM NAME : " + st.getItemName());
            System.out.println("ITEM PRICE : " + st.getPrice());
            System.out.println("ITEM DESCRIPTION : " + st.getDescription());
            System.out.println("===========================================");
        }
        if(stroot.getStocks().isEmpty())
            System.err.println("INFO : No item found in the inventory\n");
    }

    default Boolean checkIfAlreadyExist(String itcode)  //to check whether the specified item already present or not
    {
        for (Stocks st : stroot.getStocks()) {
            if (itcode.equals(st.getItemCode())) {
                return true;
            }
        }
        return false;
    }

}
