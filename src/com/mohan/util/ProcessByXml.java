package com.mohan.util;
import com.mohan.Inventory;
import com.mohan.Stocks;
import com.mohan.StocksRoot;
import org.json.*;
import java.io.*;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.*;

public class ProcessByXml extends Stocks  implements Inventory
{
    //logger utils
    static Logger logger = Logger.getLogger(ProcessByXml.class.getName());
    static FileHandler fh;

    //MAIN ROOT OF STOCK Which provides stocks
    static StocksRoot stroot = new StocksRoot();

    //initialize things for ProcessByXml
    public ProcessByXml() throws IOException, JSONException, ParserConfigurationException, SAXException, JAXBException {
        logger.setUseParentHandlers(false); //turned off the console output log
        fh = new FileHandler("XMLStocks.log",5000,1,true);
        logger.addHandler(fh); // add file handler to the loggger
        fh.setFormatter(new SimpleFormatter()); // setting formamtter for json
        readXml();
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
                System.out.println("Specified item code is already available in inventory!");
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
        System.out.println("\t\t DELETE ITEM");
        Iterator itr = stroot.getStocks().iterator();
        System.out.print("Enter ItemCode : " );
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
    void exitFromSystem()  {
        jaxbObjectToXML(); //convert and store Stocks obj in to XML FILE
        System.out.println( "INFO : Thank you ");
        fh.close();
    }

    //update stocks data
    public void updateStock()
    {
        Scanner inc = new Scanner(System.in);
        System.out.println("\t\t UPDATE ITEM");
        Iterator<Stocks> itr = stroot.getStocks().iterator();
        System.out.print("Enter ItemCode : " );
        String itcode = sc.next().trim();
        int choice = 0 ;
        int found = 0 ;
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
        } catch (Exception e) {
            System.out.println("INFO : invalid ");
            updateStock();
        }
        if ( found == 0)
        {
            System.err.println(itcode + " is not found in our inventory.!");
            logger.warning(itcode + " is not found in our inventory.!");
        }
    }

    //menu driven
    public void menuCard()  {
        Scanner inc = new Scanner(System.in);
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("\n\t\t\t\t Main Menu");
        int choice = 0;

        do
        {
            System.out.println("\n\t1. Add Stock  2. Delete Stock  3. Display Item 4. Update Stock 5. Close ");

            do {
                try {
                    System.out.print("\n Enter ( 1 / 2 / 3 / 4 / 5 ) : ");
                    choice = inc.nextInt();
                } catch (InputMismatchException e) {
                    System.err.println("Invalid choice");
                }

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
                    System.err.println("Invalid choice");
                    break;
            }
        } while(choice != 5);
    }

    //convert Stocks obj to xml and stroring in xml file
    private static void jaxbObjectToXML()
    {
        try
        {
            JAXBContext jaxbContext = JAXBContext.newInstance(StocksRoot.class);    //Create JAXB Context
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller(); //Create Marshaller
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); //Required formatting
            jaxbMarshaller.marshal(stroot, new File("StocksXml.xml")); //Writes XML file to file-system
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    //reading xml file and storing the data in to arrayList
    void readXml()
    {
        try
        {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); //an instance of factory that gives a document builder
            DocumentBuilder db = dbf.newDocumentBuilder(); //an instance of builder to parse the specified xml file
            Document doc = db.parse(new File("StocksXml.xml"));
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("stock");  // nodeList is not iterable, so we are using for loop
            for (int itr = 0; itr < nodeList.getLength(); itr++)
            {
                Node node = nodeList.item(itr);
                if (node.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element eElement = (Element) node;
                    String iCode =  eElement.getElementsByTagName("itemCode").item(0).getTextContent();
                    if(!checkIfAlreadyExist(iCode))
                    {
                        String iName = eElement.getElementsByTagName("itemName").item(0).getTextContent();
                        int iPrice = Integer.parseInt(eElement.getElementsByTagName("price").item(0).getTextContent());
                        String iDesc = eElement.getElementsByTagName("description").item(0).getTextContent();
                        Stocks tempStock = new Stocks(iCode, iName, iPrice, iDesc);
                        stroot.getStocks().add(tempStock);
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
