package io.blocko.coinstack;

import io.blocko.coinstack.CoinStackClient;
import io.blocko.coinstack.Endpoint;
import io.blocko.coinstack.model.CredentialsProvider;

public class InstanceManager {
	
	public static CoinStackClient createNewCoinStackClient() {
		CoinStackClient coinstack = new CoinStackClient(new CredentialsProvider() {
			@Override
			public String getAccessKey() {
				return "17155ccf15e603853c19a35559f3f5"; // coinstack api key: "coinstack-sample-java"
			}
			@Override
			public String getSecretKey() {
				return "4ffe022576916bf0d9c4c13718d582";
			}
		}, Endpoint.MAINNET);
		return coinstack;
	}
	
}
