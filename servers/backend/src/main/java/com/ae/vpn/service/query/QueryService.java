package com.ae.vpn.service.query;

import com.ae.vpn.servers.common.model.Provider;
import org.springframework.stereotype.Component;

/**
 * Created by ae on 10-5-16.
 */

@Component
public class QueryService {

    public AirlineQuerier getQuerier(Provider provider) {
        switch (provider) {
            case KLM:  return new KLMQuerier();
            case EASYJET: return new EasyJetQuerier();
            default: throw new RuntimeException("");
        }
    }
}
