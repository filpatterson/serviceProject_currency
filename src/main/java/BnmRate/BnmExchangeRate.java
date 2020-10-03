package BnmRate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.ArrayList;
import java.util.List;

//  mark root element of XML file and set to ignore all elements that are not mentioned
@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "ValCurs")
public class BnmExchangeRate {
    /**
     * List of all currencies for date. Array is attached to property and show that deserialization must create array
     */
    @JacksonXmlProperty(localName = "Valute")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<BnmCurrency> currencies = new ArrayList<>();

    //  save date of currency rate as String, there is no need currently to make operations over date
    @JacksonXmlProperty(isAttribute = true)
    private String date;

    @JacksonXmlProperty(isAttribute = true)
    private String name;

    public List<BnmCurrency> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<BnmCurrency> currencies) {
        this.currencies = currencies;
    }

    @Override
    public String toString() {
        return "ExchangeRate{" +
                "currencies=" + currencies +
                '}';
    }
}
