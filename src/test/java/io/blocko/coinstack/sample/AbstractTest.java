package io.blocko.coinstack.sample;

import org.junit.*;

import io.blocko.coinstack.CoinStackClient;
import io.blocko.coinstack.Endpoint;
import io.blocko.coinstack.model.CredentialsProvider;

public abstract class AbstractTest {
	
	private CoinStackClient client = null;
	
	@Before
	public void init() {
		client = new CoinStackClient(new CredentialsProvider() {
			@Override
			public String getAccessKey() {
				return "17155ccf15e603853c19a35559f3f5"; // coinstack api key: "coinstack-java-sample"
			}
			@Override
			public String getSecretKey() {
				return "4ffe022576916bf0d9c4c13718d582";
			}
		}, Endpoint.TESTNET);
	}
	
	@After
	public void fini() {
		client.close();
	}
	
	protected CoinStackClient getClient() {
		return client;
	}
	
}
