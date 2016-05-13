package com.ae.vpn.service.query;

import com.ae.vpn.servers.common.model.FlightQuery;
import com.ae.vpn.servers.common.model.FlightResult;
import com.ae.vpn.servers.common.model.Portal;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ae on 12-11-15.
 */
public abstract class AirlineQuerier {

  protected RemoteWebDriver remoteWebDriver;

  private final Logger log = LoggerFactory.getLogger(getClass());

  protected abstract List<FlightResult> doGetFlightresults(FlightQuery flightQuery);

  public RemoteWebDriver getRemoteWebDriver() {
    return remoteWebDriver;
  }

  public void setRemoteWebDriver(RemoteWebDriver remoteWebDriver) {
    this.remoteWebDriver = remoteWebDriver;
  }

  public List<FlightResult> getFlightresults(FlightQuery flightQuery) {
    if (remoteWebDriver == null) {
      throw new IllegalStateException("Remote WD not initialized ..");
    }
    try {
      return doGetFlightresults(flightQuery);
    } catch (Exception e) {
      log.error("Could not execute query " + flightQuery, e);
    }
    return new ArrayList<>();
  }

  protected void doWait(long miliseconds) {
    try {
      Thread.sleep(miliseconds);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  protected void simulateHumanTyping(String input, WebElement webElement) {
    for (char c : input.toCharArray()) {
      log.info(String.format("Typed:%s ..", input));
      webElement.sendKeys(String.valueOf(c));
      doWait(100);
    }
  }

  private static RemoteWebDriver getRemoteDriver() throws MalformedURLException {
    String seleniumHost = "localhost";
    int seleniumPort = 4444;
    String seleniumPostfix = "wd/hub";

    DesiredCapabilities capability = DesiredCapabilities.firefox();

    String address = String.format("http://%s:%s/%s",
                                   seleniumHost,
                                   seleniumPort,
                                   seleniumPostfix);

    RemoteWebDriver wd = new RemoteWebDriver(new URL(address), capability);
    return wd;
  }

  public static void doTestRun(AirlineQuerier queryer) {
    Logger log = LoggerFactory.getLogger(AirlineQuerier.class);

    try {
      RemoteWebDriver wd = getRemoteDriver();
      queryer.setRemoteWebDriver(wd);

      FlightQuery flightQuery;
      flightQuery = new FlightQuery();
      flightQuery.setDepartureDate(LocalDateTime.now().plusWeeks(4));
      flightQuery.setReturnDate(LocalDateTime.now().plusWeeks(5));
      flightQuery.setSource("London");
      flightQuery.setDestination("Moscow");
//      flightQuery.setQueryProviders( Arrays.asList(new Provider[] { Provider.EASYJET }));
      flightQuery.setQueryPortals(Arrays.asList(new Portal[]{Portal.US}));

      List<FlightResult> fl = queryer.getFlightresults(flightQuery);
      for (FlightResult f : fl) {
        log.info("Read: " + f.toString());
      }
      wd.quit();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
