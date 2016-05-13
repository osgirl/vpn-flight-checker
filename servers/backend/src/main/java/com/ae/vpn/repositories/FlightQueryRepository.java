package com.ae.vpn.repositories;


import com.ae.vpn.servers.common.model.FlightQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by ae on 12-11-15.
 */
@Transactional
public interface FlightQueryRepository extends JpaRepository<FlightQuery, Long> {

}
