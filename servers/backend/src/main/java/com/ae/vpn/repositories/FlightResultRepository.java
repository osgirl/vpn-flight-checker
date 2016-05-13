package com.ae.vpn.repositories;

import com.ae.vpn.servers.common.model.FlightQuery;
import com.ae.vpn.servers.common.model.FlightResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by ae on 12-11-15.
 */
@Transactional
public interface FlightResultRepository extends JpaRepository<FlightResult, Long> {
    List<FlightResult> findByFlightQuery(FlightQuery query);
}
