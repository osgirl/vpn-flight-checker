package com.ae.vpn.service.query;

import com.ae.vpn.servers.common.model.FlightQuery;
import com.ae.vpn.servers.common.model.FlightResult;
import com.ae.vpn.servers.common.model.Provider;
import org.jsoup.Jsoup;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by ae on 29-11-15.
 */
public class EasyJetQuerier extends AirlineQuerier {

  private String createStringRep(LocalDateTime date) {
    return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
  }

  private String createDivNameFromDate(LocalDateTime date) {
    //12 november 2015
    String template = "//div[@data-column-date='%s']";
    String
        dateString =
        date.format(
            DateTimeFormatter.ofPattern("ddMMyyyy")); // needs to be changed to .. 2-10-1
    dateString = dateString.toLowerCase();
    return String.format(template, dateString);
  }

  private String denomenationToCurrencty(String symbol) {
    if ("€".equals(symbol)) { return "EUR"; }
    if ("£".equals(symbol)) { return "GBP"; }
    if ("$".equals(symbol)) { return "USD"; }
    throw new IllegalStateException("Unknown CurrencyRode");
  }

  private FlightResult processFlight(WebElement rawData) {
    String price = Jsoup.parse(rawData.getAttribute("charge-debit")).text();
    String currency = denomenationToCurrencty(price.substring(0, 1));
    String amount = price.substring(1);
    WebElement departureElement = rawData.findElements(By.xpath(".//span[@class='time']")).get(0);
    String departureTime = departureElement.findElements(By.xpath(".//span")).get(2).getText();
    FlightResult flightResult = new FlightResult();
    flightResult.setProvider(Provider.EASYJET);
    flightResult.setAmount(amount);
    flightResult.setCurrency(currency);
    flightResult.setDeparture(departureTime);
    return flightResult;
  }

  @Override
  protected List<FlightResult> doGetFlightresults(FlightQuery flightQuery) {
    remoteWebDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    remoteWebDriver.get("http://www.easyjet.com/en");
    RemoteWebDriver rw = (RemoteWebDriver) remoteWebDriver.switchTo().frame(0);
    WebElement sourceField = remoteWebDriver.findElement(By.xpath
        ("//div[1]/div[2]/div/div[2]/div[3]/input[1]"));
    sourceField.click();
    sourceField.clear();
    simulateHumanTyping(flightQuery.getSource() + Keys.ARROW_DOWN + Keys.ENTER,
                        sourceField);
    WebElement destinationField = remoteWebDriver.findElement(By.xpath
        ("//div[1]/div[2]/div/div[2]/div[4]/input[1]"));
    destinationField.click();
    destinationField.clear();
    simulateHumanTyping(flightQuery.getDestination() + Keys.ARROW_DOWN + Keys.ENTER,
                        destinationField);

    WebElement outboundField = remoteWebDriver.findElement(
        By.xpath("//div[1]/div[2]/div/div[2]/div[5]/div[1]/input"));
    outboundField.click();
    outboundField.clear();
    outboundField.sendKeys(createStringRep(flightQuery.getDepartureDate()));

    WebElement returnField = remoteWebDriver.findElement(
        By.xpath("//div[1]/div[2]/div/div[2]/div[5]/div[2]/input[2]"));
    returnField.click();
    returnField.clear();
    returnField.sendKeys(createStringRep(flightQuery.getReturnDate()));

    WebElement showFlightsButton;
    showFlightsButton = getRemoteWebDriver().findElement(
        By.xpath("//div[1]/div[2]/div/div[2]/div[10]/input"));
    showFlightsButton.click();

    List<FlightResult> results = new ArrayList<>();
    String xpathQuery = createDivNameFromDate(flightQuery.getDepartureDate());
    WebElement toFlightsToProcess = remoteWebDriver.findElement(By.xpath(xpathQuery));
    List<WebElement> flights = toFlightsToProcess.findElements(By.xpath(".//a"));
    for (WebElement flight : flights) {
      results.add(processFlight(flight));
    }
    return results;
  }

  public static void main(String[] args) throws Exception {
    EasyJetQuerier querier = new EasyJetQuerier();
    doTestRun(querier);
  }


}
