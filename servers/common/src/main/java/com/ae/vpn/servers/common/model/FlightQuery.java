package com.ae.vpn.servers.common.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;

/**
 * Created by ae on 12-11-15.
 */
@Entity
public class FlightQuery {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  Long id;

  private String source;

  private String destination;

  @JsonFormat(pattern="yyyy-MM-dd")
  private LocalDateTime departureDate;

  @JsonFormat(pattern="yyyy-MM-dd")
  private LocalDateTime returnDate;

  @ElementCollection(targetClass = Portal.class, fetch = FetchType.EAGER)
  @Column(name = "portal", nullable = false)
  @JoinTable(name = "portals", joinColumns = @JoinColumn(name = "flight_query_id"))
  @Enumerated
  private List<Portal> queryPortals;

  @ElementCollection(targetClass = Provider.class, fetch = FetchType.EAGER)
  @Column(name = "providers", nullable = false)
  @JoinTable(name = "providers", joinColumns = @JoinColumn(name = "flight_provider_id"))
  @Enumerated
  private List<Provider> queryProviders;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getDestination() {
    return destination;
  }

  public void setDestination(String destination) {
    this.destination = destination;
  }

  public LocalDateTime getDepartureDate() {
    return departureDate;
  }

  public void setDepartureDate(LocalDateTime departureDate) {
    this.departureDate = departureDate;
  }

  public LocalDateTime getReturnDate() {
    return returnDate;
  }

  public void setReturnDate(LocalDateTime returnDate) {
    this.returnDate = returnDate;
  }

  public List<Portal> getQueryPortals() {
    return queryPortals;
  }

  public void setQueryPortals(List<Portal> queryPortals) {
    this.queryPortals = queryPortals;
  }

  public List<Provider> getQueryProviders() {
    return queryProviders;
  }

  public void setQueryProviders(List<Provider> queryProviders) {
    this.queryProviders = queryProviders;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("FlightQuery{");
    sb.append("id=").append(id);
    sb.append(", source='").append(source).append('\'');
    sb.append(", destination='").append(destination).append('\'');
    sb.append(", departureDate=").append(departureDate);
    sb.append(", returnDate=").append(returnDate);
    sb.append(", queryPortals=").append(queryPortals);
    sb.append(", queryProviders=").append(queryProviders);
    sb.append('}');
    return sb.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof FlightQuery)) {
      return false;
    }
    FlightQuery that = (FlightQuery) o;
    return Objects.equals(id, that.id) &&
           Objects.equals(source, that.source) &&
           Objects.equals(destination, that.destination) &&
           Objects.equals(departureDate, that.departureDate) &&
           Objects.equals(returnDate, that.returnDate) &&
           Objects.equals(queryPortals, that.queryPortals) &&
           Objects.equals(queryProviders, that.queryProviders);
  }

  @Override
  public int hashCode() {
    return Objects
        .hash(id, source, destination, departureDate, returnDate, queryPortals, queryProviders);
  }
}
