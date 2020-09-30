package FloatRatesCom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrencyItem {
    @JacksonXmlProperty(localName = "title")
    private String title;

    @JacksonXmlProperty(localName = "link")
    private String link;

    @JacksonXmlProperty(localName = "targetCurrency")
    private String targetCurrency;

    @JacksonXmlProperty(localName = "targetName")
    private String targetName;

    @JacksonXmlProperty(localName = "exchangeRate")
    private String exchangeRate;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    @Override
    public String toString() {
        return "CurrencyItem{" +
                "link='" + link + '\'' +
                ", targetCurrency='" + targetCurrency + '\'' +
                ", targetName='" + targetName + '\'' +
                ", exchangeRate=" + exchangeRate +
                '}';
    }
}
