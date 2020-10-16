package BnmRateMd;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class BnmCurrency {
    //  id of currency
    @JacksonXmlProperty(localName = "ID")
    private short id;

    //  numerical code of currency saved in National Bank system
    @JacksonXmlProperty(localName = "NumCode")
    private short numCode;

    //  short character code of currency
    @JacksonXmlProperty(localName = "CharCode")
    private String charCode;

    //  nominal cost
    @JacksonXmlProperty(localName = "Nominal")
    private short nominal;

    //  full name of currency
    @JacksonXmlProperty(localName = "Name")
    private String name;

    //  current cost of currency for internal market
    @JacksonXmlProperty(localName = "Value")
    private double value;

    //  list of getters and setters

    public short getNumCode() {
        return numCode;
    }

    public void setNumCode(short numCode) {
        this.numCode = numCode;
    }

    public String getCharCode() {
        return charCode;
    }

    public void setCharCode(String charCode) {
        this.charCode = charCode;
    }

    public short getNominal() {
        return nominal;
    }

    public void setNominal(short nominal) {
        this.nominal = nominal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Valute{" +
                "id=" + id +
                ", numCode=" + numCode +
                ", charCode='" + charCode + '\'' +
                ", nominal=" + nominal +
                ", name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
