package com.kietlaptrinh.shop_ecomerce.config;

import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class PaypalConfig {

    private String clientId = "AQOvHPa7FCu4tJ69WQPiwmfWG_rTioMcmCvFi2o8xj8f3uB4AS90VVt_68DHIRHfo2875qK5ITdNxezj";
    private String clientSecret = "EBeME5Mq9H2RQqIUVNK14oF7Z4AGWEdHCEFN-jTcdTwfhoEBM0IH_YO_OrX2VXbWS507PMTfHWzheWnl";
    private String mode = "sandbox";

    @Bean
    public Map<String, String> paypalSdkConfig() {
        Map<String, String> configMap = new HashMap<>();
        configMap.put("mode", mode);
        return configMap;
    }

    @Bean
    public OAuthTokenCredential oAuthTokenCredential() {
        return new OAuthTokenCredential(clientId, clientSecret, paypalSdkConfig());
    }

    @Bean
    public APIContext apiContext() throws PayPalRESTException {
        APIContext context = new APIContext(oAuthTokenCredential().getAccessToken());
        context.setConfigurationMap(paypalSdkConfig());
        return context;
    }
}

