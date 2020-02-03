package com.mohan;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.BufferedReader;
import java.util.InputMismatchException;
import java.util.Scanner;

@XmlRootElement(name = "stock")
@XmlAccessorType(XmlAccessType.FIELD)
public class Stocks  {
    transient public Scanner sc = new Scanner(System.in);
    private String itemCode;
    private String itemName;
    private int price;
    private String description;
    public Stocks() {}

    public Stocks(String itemCode, String itemName, int price, String description) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.price = price;
        this.description = description;
    }

    public String getItemCode()
    {
        return this.itemCode.toUpperCase();
    }
    public String getDescription()
    {
        return this.description;
    }
    public int getPrice()
    {
        return this.price;
    }
    public String getItemName()
    {
        return this.itemName;
    }

    //getting item Name from user
    public void setItemName()
    {
        System.out.print("Enter Item Name : ");
        this.itemName = sc.nextLine().trim();
        this.itemName = this.itemName.replaceAll("[^a-zA-Z0-9\\s+]", "");
        if(this.itemName.isEmpty())
        {
            while(this.itemName.isEmpty())
                this.itemName = sc.nextLine().trim();
        }
    }

    //getting description from user

    public void setDescription()
    {
        System.out.print("Enter Item Description : ");
        this.description = sc.nextLine().trim();
        if(this.description.isEmpty())
        {
            while(this.description.isEmpty())
                this.description = sc.nextLine().trim();
        }
    }

    //getting price from user

    public void setPrice()
    {
        Scanner input = new Scanner(System.in);
        do {
            try {
                System.out.print("Enter Item Price : ");
                this.price = input.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid price");
            }
            input.nextLine(); // clears the buffer
        } while (this.price <= 0);

    }

    //getting item code from user
    public void setItemCode()
    {
        System.out.print("Enter Item Code : ");
        this.itemCode = sc.next().trim().toUpperCase();
        if(this.itemCode.isEmpty())
        {
            while(this.itemCode.isEmpty())
                this.itemCode = sc.nextLine().trim();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Stocks other = (Stocks) obj;
        if (itemCode != other.itemCode)
            return false;
        return true;
    }

    public void updateItemName(String name)
    {
        this.itemName = name;
    }
    public void updateDescription(String desc)
    {
        this.description = desc;
    }
    public void updatePrice(int price)
    {
        this.price = price;
    }

    @Override
    public String toString() {
        return "{" +
                "itemCode:'" + this.itemCode + '\'' +
                ", itemName:'" + this.itemName + '\'' +
                ", price:" + this.price +
                ", description:'" + this.description + '\'' +
                '}';
    }
}
