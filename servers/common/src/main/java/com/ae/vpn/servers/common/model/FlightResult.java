package com.ae.vpn.servers.common.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Created by ae on 12-11-15.
 */
@Entity
public class FlightResult {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private FlightQuery flightQuery;

    private Provider provider;

    private Portal country;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private LocalDateTime queryTime;

    private String currency;

    private String amount;

    private String departure;

    private String duration;

    private String ip;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FlightQuery getFlightQuery() {
        return flightQuery;
    }

    public void setFlightQuery(FlightQuery flightQuery) {
        this.flightQuery = flightQuery;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public Portal getCountry() {
        return country;
    }

    public void setCountry(Portal country) {
        this.country = country;
    }

    public LocalDateTime getQueryTime() {
        return queryTime;
    }

    public void setQueryTime(LocalDateTime queryTime) {
        this.queryTime = queryTime;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FlightResult)) {
            return false;
        }
        FlightResult that = (FlightResult) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(flightQuery, that.flightQuery) &&
               Objects.equals(provider, that.provider) &&
               Objects.equals(country, that.country) &&
               Objects.equals(queryTime, that.queryTime) &&
               Objects.equals(currency, that.currency) &&
               Objects.equals(amount, that.amount) &&
               Objects.equals(departure, that.departure) &&
               Objects.equals(duration, that.duration) &&
               Objects.equals(ip, that.ip);
    }

    @Override
    public int hashCode() {
        return Objects
            .hash(id, flightQuery, provider, country, queryTime, currency, amount, departure,
                  duration,
                  ip);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("FlightResult{");
        sb.append("id=").append(id);
        sb.append(", flightQuery=").append(flightQuery);
        sb.append(", provider=").append(provider);
        sb.append(", country=").append(country);
        sb.append(", queryTime=").append(queryTime);
        sb.append(", currency='").append(currency).append('\'');
        sb.append(", amount='").append(amount).append('\'');
        sb.append(", departure='").append(departure).append('\'');
        sb.append(", duration='").append(duration).append('\'');
        sb.append(", ip='").append(ip).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
