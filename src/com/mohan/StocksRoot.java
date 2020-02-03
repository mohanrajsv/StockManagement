package com.mohan;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

@XmlRootElement(name = "Stocks")
@XmlAccessorType(XmlAccessType.FIELD)
public class StocksRoot {
    @XmlElement(name = "stock")
    public static Set<Stocks> alStock = new HashSet<>();
    public Set<Stocks> getStocks()
    {
        return (Set<Stocks>) alStock;
    }

    public void setStocks(Set<Stocks> alStock)
    {
        StocksRoot.alStock = (Set<Stocks>) alStock;
    }

    public String toString()
    {
        return alStock.toString();
    }
}

