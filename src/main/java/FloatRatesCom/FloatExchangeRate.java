package FloatRatesCom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for getting currency rates referring to dollar USA
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "channel")
public class FloatExchangeRate {
    @JacksonXmlProperty(localName = "title")
    private String title;

    @JacksonXmlProperty(localName = "xmlLink")
    private String xmlLink;

    @JacksonXmlProperty(localName = "description")
    private String description;

    @JacksonXmlProperty(localName = "baseCurrency")
    private String baseCurrency;

    @JacksonXmlProperty(localName = "pubDate")
    private String pubDate;

    @JacksonXmlProperty(localName = "item")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<FloatCurrency> currencies = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getXmlLink() {
        return xmlLink;
    }

    public void setXmlLink(String xmlLink) {
        this.xmlLink = xmlLink;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public List<FloatCurrency> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<FloatCurrency> currencies) {
        this.currencies = currencies;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "title='" + title + '\'' +
                ", xmlLink='" + xmlLink + '\'' +
                ", description='" + description + '\'' +
                ", baseCurrency='" + baseCurrency + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", currencies=" + currencies +
                '}';
    }
}
