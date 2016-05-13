package com.ae.vpn.controller;


import com.ae.vpn.servers.common.model.FlightResult;
import com.ae.vpn.service.query.QueryService;
import com.ae.vpn.service.session.SESSION_STATUS;
import com.ae.vpn.service.session.SessionService;
import com.ae.vpn.service.session.Session;
import com.ae.vpn.service.query.AirlineQuerier;
import com.ae.vpn.repositories.FlightQueryRepository;
import com.ae.vpn.repositories.FlightResultRepository;
import com.ae.vpn.servers.common.model.FlightQuery;
import com.ae.vpn.servers.common.model.Portal;
import com.ae.vpn.servers.common.model.Provider;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Component
public class Controller implements CommandLineRunner {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private String seleniumPostfix = "wd/hub";

  @Autowired
  private FlightResultRepository flightResultRepository;

  @Autowired
  private FlightQueryRepository flightQueryRepository;

  @Autowired
  private SessionService sessionService;

  @Autowired
  private QueryService queryService;

  private Map<Portal, Session> portal2session = new HashMap<>();

  //
  // The code below will not scale because it involves buys waiting.
  // Rewrite if one wants to drastically increase (i.e. a factor 30)
  // the number of queries.
  //
  private void handleQuery(FlightQuery query) {
    for (Portal country : query.getQueryPortals()) {
      try {

        if (!portal2session.containsKey(country)
            || portal2session.get(country) == null) {
          portal2session.put(country, sessionService.createSession(country));
        } else {
          Session session = portal2session.get(country);
          if (session.getStatus() == SESSION_STATUS.FAILDED) {
            sessionService.destorySession(session);
            portal2session.put(country, sessionService.createSession(country));
          }
        }

        Session session = portal2session.get(country);
        SESSION_STATUS status;
        final int maxTries = 5;
        int i = 0;
        do  {
          status = session.getStatus();
          Thread.sleep(4000);
        } while (status == SESSION_STATUS.STARTING && i < maxTries);

        log.info("After init our session status is " + status + " ..\n");
        if (status != SESSION_STATUS.UP) {
          sessionService.destorySession(session);
          portal2session.remove(country);
          continue; // better luck next time
        }

        DesiredCapabilities capability = DesiredCapabilities.firefox();
        String address = String.format("http://%s:%s/%s",
                                       session.getIp(),
                                       session.getPort(),
                                       seleniumPostfix);
        log.info("Acquired solaris instance ..");
        for (Provider provider : query.getQueryProviders()) {
          RemoteWebDriver wd = new RemoteWebDriver(new URL(address), capability);
          AirlineQuerier querier  = queryService.getQuerier(provider);
          querier.setRemoteWebDriver(wd);
          List<FlightResult> results = querier.getFlightresults(query);
          log.info(String.format("Read %d raw results ..", results.size()));
          for (FlightResult result: results) {
            log.info("\t-" + result.toString());
          }
          results = results.stream()
                           .filter(f -> !StringUtils.isEmpty(f.getAmount()))
                           .collect(Collectors.toList());
          LocalDateTime now = LocalDateTime.now();
          log.info(String.format("Saving %d results ..", results.size()));
          for (FlightResult result: results) {
            log.info("\t-" + result.toString());
          }
          results.stream().forEach(f -> f.setCountry(country));
          results.stream().forEach(f -> f.setQueryTime(now));
          results.stream().forEach(f -> f.setFlightQuery(query));
          results.stream().forEach(f -> f.setIp(""));
          flightResultRepository.save(results);
          wd.close();
        }

      } catch (Exception e) {
        log.error("Exception While querying", e);
      }
    }
  }

  @PostConstruct
  private void init() {
    if (!flightQueryRepository.findAll().isEmpty()) {
      return;
    }
    FlightQuery flightQuery;

    flightQuery = new FlightQuery();
    flightQuery.setDepartureDate(LocalDateTime.now().plusWeeks(4));
    flightQuery.setReturnDate(LocalDateTime.now().plusWeeks(5));
    flightQuery.setSource("London Gatwick");
    flightQuery.setDestination("Tallinn");
    flightQuery.setQueryProviders( Arrays.asList(new Provider[] { Provider.EASYJET }));
    flightQuery.setQueryPortals(
        Arrays.asList(new Portal[] { Portal.RU, Portal.JP, Portal.US } )
    );
    flightQueryRepository.save(flightQuery);
  }

  private List<FlightQuery> getOverdueQueries() {
    List<FlightQuery> rvalue = flightQueryRepository.findAll();
    return rvalue != null ? rvalue : new ArrayList<>();
  }

  @Scheduled(initialDelay = 1000, fixedDelay = 1000 * 60 * 60)
  private void performScraping() {
    log.info("Starting update ..");
    getOverdueQueries().stream().forEach(this::handleQuery);
  }

  @Override
  public void run(String... strings) throws Exception {
    while(true) {
      Thread.sleep(5000);
    }
  }
}
