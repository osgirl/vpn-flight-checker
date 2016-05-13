package com.ae.vpn.service.query;

import com.ae.vpn.servers.common.model.FlightQuery;
import com.ae.vpn.servers.common.model.FlightResult;
import com.ae.vpn.servers.common.model.Provider;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by ae on 10-11-15.
 */

public class KLMQuerier extends AirlineQuerier {

  private final Logger log = LoggerFactory.getLogger(getClass());

  // can be debugged using java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005  -jar vpn-controller.jar

  private List<FlightResult> getPrices(FlightQuery query) {
    List<FlightResult> rvalue = new ArrayList<>();

    List<WebElement> results;
    results = remoteWebDriver.findElements(By.xpath("//div[@class='est-content-o']"));
    results = results.get(0).findElements(By.xpath(".//div[@class='est-flight-item ']"));
    for (WebElement we : results) {
      List<WebElement> parts;

      String
          currency =
          we.findElement(By.xpath(".//span[@class='est-flight-item-price-currency']")).getText();
      String
          amount =
          we.findElement(By.xpath(".//span[@class='est-flight-item-price-amount']")).getText();

      String
          departure =
          we.findElement(By.xpath(".//span[@class='est-flight-item-info-connection-departure']"))
              .getText();
      String
          duration =
          we.findElement(By.xpath(".//span[@class='est-flight-item-info-connection-duration']"))
              .getText();
      String m;
      m = String.format("Read currency:%s, amount:%s, departure:%s, duration:%s ..\n",
                        currency,
                        amount,
                        departure,
                        duration);
      log.info(m);

      FlightResult flightResult = new FlightResult();
      flightResult.setAmount(amount);
      flightResult.setCurrency(currency);
      flightResult.setDeparture(departure);
      flightResult.setDuration(duration);
      flightResult.setFlightQuery(query);
      flightResult.setProvider(Provider.KLM);
      rvalue.add(flightResult);
    }

    return rvalue;
  }

  private String createLiNameFromDate(LocalDateTime date, Locale locale) {
    //12 november 2015
    String template = "//li[@aria-label='%s']";
    String
        dateString =
        date.format(
            DateTimeFormatter.ofPattern("d MMMM yyyy", locale)); // needs to be changed to .. 2-10-1
    dateString = dateString.toLowerCase();
    return String.format(template, dateString);
  }

  @Override
  public List<FlightResult> doGetFlightresults(FlightQuery flightQuery) {
    remoteWebDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    remoteWebDriver.get("https://www.klm.com/home/nl/nl");
    WebElement sourceField = remoteWebDriver.findElement(By.xpath
        ("//div[@class='g-search-form--connections']/div[1]/label[1]/input"));
    sourceField.click();
    simulateHumanTyping(flightQuery.getSource() + Keys.ARROW_DOWN + Keys.ENTER,
                        sourceField);
    WebElement destinationField;
    destinationField =
        remoteWebDriver.findElement(
            By.xpath("//div[@class='g-search-form--connections']/div[1]/label[2]/input"));
    destinationField.click();
    simulateHumanTyping(flightQuery.getDestination() + Keys.ARROW_DOWN + Keys.ENTER,
                        destinationField);
    remoteWebDriver
        .findElement(By.xpath("//div[@class='g-search-form--connections']/div[1]/label[3]/input"))
        .click();

    //
    // Move to calandar
    //
    String todaysDateLink = createLiNameFromDate(LocalDateTime.now(), new Locale("nl"));
    Actions builder;
    builder = new Actions(remoteWebDriver);
    builder.moveToElement(remoteWebDriver.findElementsByXPath(todaysDateLink).get(0));
    Action moveToCalandarLink = builder.build();
    moveToCalandarLink.perform();

    builder = new Actions(remoteWebDriver);
    builder.sendKeys(Keys.PAGE_DOWN);
    Action pressPageDown = builder.build();

    //
    // Click departure date
    //
    String departureli = createLiNameFromDate(flightQuery.getDepartureDate(), new Locale("nl"));
    WebElement departureLink = remoteWebDriver.findElementsByXPath(departureli).get(0);
    while (!departureLink.isDisplayed()) {
      pressPageDown.perform();
      doWait(100);
    }
    departureLink.click();

    //
    // Click return date
    //
    String returnli = createLiNameFromDate(flightQuery.getReturnDate(), new Locale("nl"));
    WebElement returnLink = remoteWebDriver.findElementsByXPath(returnli).get(1);
    while (!returnLink.isDisplayed()) {
      pressPageDown.perform();
      doWait(100);
    }
    returnLink.click();

    remoteWebDriver.findElementByXPath("//div[@class='g-search-form-footer']/div/div/button")
        .click();
    return getPrices(flightQuery);
  }

  public static void main(String[] args) throws Exception {
    KLMQuerier querier = new KLMQuerier();
    doTestRun(querier);
  }
}
