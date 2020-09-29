package BnmRate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.IOException;

public class XMLFormatter {
    public static void main(String[] args) throws IOException {
        XmlMapper mapper = new XmlMapper();
        ExchangeRate valutes = mapper.readValue(data, ExchangeRate.class);
        System.out.println(valutes);
    }

    public static String data = "<ValCurs Date=\"29.09.2020\" name=\"Обменные ставки\">\n" +
            "<link type=\"text/css\" id=\"dark-mode\" rel=\"stylesheet\" href=\"\"/>\n" +
            "<style type=\"text/css\" id=\"dark-mode-custom-style\"/>\n" +
            "<Valute ID=\"47\">\n" +
            "<NumCode>978</NumCode>\n" +
            "<CharCode>EUR</CharCode>\n" +
            "<Nominal>1</Nominal>\n" +
            "<Name>Евро</Name>\n" +
            "<Value>19.7491</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"44\">\n" +
            "<NumCode>840</NumCode>\n" +
            "<CharCode>USD</CharCode>\n" +
            "<Nominal>1</Nominal>\n" +
            "<Name>Доллар США</Name>\n" +
            "<Value>16.9374</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"36\">\n" +
            "<NumCode>643</NumCode>\n" +
            "<CharCode>RUB</CharCode>\n" +
            "<Nominal>1</Nominal>\n" +
            "<Name>Российский Рубль</Name>\n" +
            "<Value>0.2150</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"35\">\n" +
            "<NumCode>946</NumCode>\n" +
            "<CharCode>RON</CharCode>\n" +
            "<Nominal>1</Nominal>\n" +
            "<Name>Румынский Лей</Name>\n" +
            "<Value>4.0534</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"43\">\n" +
            "<NumCode>980</NumCode>\n" +
            "<CharCode>UAH</CharCode>\n" +
            "<Nominal>1</Nominal>\n" +
            "<Name>Украинская Гривна</Name>\n" +
            "<Value>0.5987</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"61\">\n" +
            "<NumCode>784</NumCode>\n" +
            "<CharCode>AED</CharCode>\n" +
            "<Nominal>1</Nominal>\n" +
            "<Name>О.А.Э. Дирхам</Name>\n" +
            "<Value>4.6114</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"64\">\n" +
            "<NumCode>008</NumCode>\n" +
            "<CharCode>ALL</CharCode>\n" +
            "<Nominal>10</Nominal>\n" +
            "<Name>Албанский лек</Name>\n" +
            "<Value>1.5943</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"1\">\n" +
            "<NumCode>051</NumCode>\n" +
            "<CharCode>AMD</CharCode>\n" +
            "<Nominal>10</Nominal>\n" +
            "<Name>Армянский Драм</Name>\n" +
            "<Value>0.3478</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"3\">\n" +
            "<NumCode>036</NumCode>\n" +
            "<CharCode>AUD</CharCode>\n" +
            "<Nominal>1</Nominal>\n" +
            "<Name>Австралийский Доллар</Name>\n" +
            "<Value>11.9637</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"4\">\n" +
            "<NumCode>944</NumCode>\n" +
            "<CharCode>AZN</CharCode>\n" +
            "<Nominal>1</Nominal>\n" +
            "<Name>Азербайджанский Манат</Name>\n" +
            "<Value>9.9702</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"6\">\n" +
            "<NumCode>975</NumCode>\n" +
            "<CharCode>BGN</CharCode>\n" +
            "<Nominal>1</Nominal>\n" +
            "<Name>Болгарский Лев</Name>\n" +
            "<Value>10.0905</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"50\">\n" +
            "<NumCode>933</NumCode>\n" +
            "<CharCode>BYN</CharCode>\n" +
            "<Nominal>1</Nominal>\n" +
            "<Name>Белорусский Рубль</Name>\n" +
            "<Value>6.4512</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"8\">\n" +
            "<NumCode>124</NumCode>\n" +
            "<CharCode>CAD</CharCode>\n" +
            "<Nominal>1</Nominal>\n" +
            "<Name>Канадский Доллар</Name>\n" +
            "<Value>12.6687</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"9\">\n" +
            "<NumCode>756</NumCode>\n" +
            "<CharCode>CHF</CharCode>\n" +
            "<Nominal>1</Nominal>\n" +
            "<Name>Швейцарский Франк</Name>\n" +
            "<Value>18.2899</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"54\">\n" +
            "<NumCode>156</NumCode>\n" +
            "<CharCode>CNY</CharCode>\n" +
            "<Nominal>1</Nominal>\n" +
            "<Name>Китайский юань Ренминби</Name>\n" +
            "<Value>2.4864</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"10\">\n" +
            "<NumCode>203</NumCode>\n" +
            "<CharCode>CZK</CharCode>\n" +
            "<Nominal>1</Nominal>\n" +
            "<Name>Чешская Крона</Name>\n" +
            "<Value>0.7279</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"12\">\n" +
            "<NumCode>208</NumCode>\n" +
            "<CharCode>DKK</CharCode>\n" +
            "<Nominal>1</Nominal>\n" +
            "<Name>Датская Крона</Name>\n" +
            "<Value>2.6524</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"17\">\n" +
            "<NumCode>826</NumCode>\n" +
            "<CharCode>GBP</CharCode>\n" +
            "<Nominal>1</Nominal>\n" +
            "<Name>Английский Фунт Стерлингов</Name>\n" +
            "<Value>21.8561</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"18\">\n" +
            "<NumCode>981</NumCode>\n" +
            "<CharCode>GEL</CharCode>\n" +
            "<Nominal>1</Nominal>\n" +
            "<Name>Грузинский Лари</Name>\n" +
            "<Value>5.1248</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"63\">\n" +
            "<NumCode>344</NumCode>\n" +
            "<CharCode>HKD</CharCode>\n" +
            "<Nominal>1</Nominal>\n" +
            "<Name>Гонконгский доллаp</Name>\n" +
            "<Value>2.1855</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"20\">\n" +
            "<NumCode>191</NumCode>\n" +
            "<CharCode>HRK</CharCode>\n" +
            "<Nominal>1</Nominal>\n" +
            "<Name>Хорватская Куна</Name>\n" +
            "<Value>2.6157</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"21\">\n" +
            "<NumCode>348</NumCode>\n" +
            "<CharCode>HUF</CharCode>\n" +
            "<Nominal>100</Nominal>\n" +
            "<Name>Венгерский Форинт</Name>\n" +
            "<Value>5.4272</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"49\">\n" +
            "<NumCode>376</NumCode>\n" +
            "<CharCode>ILS</CharCode>\n" +
            "<Nominal>1</Nominal>\n" +
            "<Name>Израильский Шекель</Name>\n" +
            "<Value>4.8793</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"65\">\n" +
            "<NumCode>356</NumCode>\n" +
            "<CharCode>INR</CharCode>\n" +
            "<Nominal>10</Nominal>\n" +
            "<Name>Индийская pупия</Name>\n" +
            "<Value>2.2954</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"23\">\n" +
            "<NumCode>352</NumCode>\n" +
            "<CharCode>ISK</CharCode>\n" +
            "<Nominal>10</Nominal>\n" +
            "<Name>Исландская Крона</Name>\n" +
            "<Value>1.2176</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"25\">\n" +
            "<NumCode>392</NumCode>\n" +
            "<CharCode>JPY</CharCode>\n" +
            "<Nominal>100</Nominal>\n" +
            "<Name>Японская Йена</Name>\n" +
            "<Value>16.0796</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"26\">\n" +
            "<NumCode>417</NumCode>\n" +
            "<CharCode>KGS</CharCode>\n" +
            "<Nominal>10</Nominal>\n" +
            "<Name>Киргизский Сом</Name>\n" +
            "<Value>2.1843</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"66\">\n" +
            "<NumCode>410</NumCode>\n" +
            "<CharCode>KRW</CharCode>\n" +
            "<Nominal>100</Nominal>\n" +
            "<Name>Южнокорейский вон</Name>\n" +
            "<Value>1.4433</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"59\">\n" +
            "<NumCode>414</NumCode>\n" +
            "<CharCode>KWD</CharCode>\n" +
            "<Nominal>1</Nominal>\n" +
            "<Name>Кувейтский Динар</Name>\n" +
            "<Value>55.2895</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"27\">\n" +
            "<NumCode>398</NumCode>\n" +
            "<CharCode>KZT</CharCode>\n" +
            "<Nominal>10</Nominal>\n" +
            "<Name>Казахский Тенге</Name>\n" +
            "<Value>0.3951</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"62\">\n" +
            "<NumCode>807</NumCode>\n" +
            "<CharCode>MKD</CharCode>\n" +
            "<Nominal>10</Nominal>\n" +
            "<Name>Македонский денар</Name>\n" +
            "<Value>3.2015</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"56\">\n" +
            "<NumCode>458</NumCode>\n" +
            "<CharCode>MYR</CharCode>\n" +
            "<Nominal>1</Nominal>\n" +
            "<Name>Малайзийский ринггит</Name>\n" +
            "<Value>4.0569</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"32\">\n" +
            "<NumCode>578</NumCode>\n" +
            "<CharCode>NOK</CharCode>\n" +
            "<Nominal>1</Nominal>\n" +
            "<Name>Норвежская Крона</Name>\n" +
            "<Value>1.7799</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"53\">\n" +
            "<NumCode>554</NumCode>\n" +
            "<CharCode>NZD</CharCode>\n" +
            "<Nominal>1</Nominal>\n" +
            "<Name>Новозеландский Доллар</Name>\n" +
            "<Value>11.1118</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"33\">\n" +
            "<NumCode>985</NumCode>\n" +
            "<CharCode>PLN</CharCode>\n" +
            "<Nominal>1</Nominal>\n" +
            "<Name>Польская Злота</Name>\n" +
            "<Value>4.3404</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"48\">\n" +
            "<NumCode>941</NumCode>\n" +
            "<CharCode>RSD</CharCode>\n" +
            "<Nominal>100</Nominal>\n" +
            "<Name>Сербский Динар</Name>\n" +
            "<Value>16.7889</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"37\">\n" +
            "<NumCode>752</NumCode>\n" +
            "<CharCode>SEK</CharCode>\n" +
            "<Nominal>1</Nominal>\n" +
            "<Name>Шведская Крона</Name>\n" +
            "<Value>1.8683</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"51\">\n" +
            "<NumCode>972</NumCode>\n" +
            "<CharCode>TJS</CharCode>\n" +
            "<Nominal>1</Nominal>\n" +
            "<Name>Таджикский Сомони</Name>\n" +
            "<Value>1.6398</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"60\">\n" +
            "<NumCode>934</NumCode>\n" +
            "<CharCode>TMT</CharCode>\n" +
            "<Nominal>1</Nominal>\n" +
            "<Name>Туркменский Манат</Name>\n" +
            "<Value>4.8393</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"42\">\n" +
            "<NumCode>949</NumCode>\n" +
            "<CharCode>TRY</CharCode>\n" +
            "<Nominal>1</Nominal>\n" +
            "<Name>Турецкая Лира</Name>\n" +
            "<Value>2.1736</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"45\">\n" +
            "<NumCode>860</NumCode>\n" +
            "<CharCode>UZS</CharCode>\n" +
            "<Nominal>100</Nominal>\n" +
            "<Name>Узбекский Сум</Name>\n" +
            "<Value>0.1642</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"46\">\n" +
            "<NumCode>960</NumCode>\n" +
            "<CharCode>XDR</CharCode>\n" +
            "<Nominal>1</Nominal>\n" +
            "<Name>СДР</Name>\n" +
            "<Value>23.8223</Value>\n" +
            "</Valute>\n" +
            "</ValCurs>";
}