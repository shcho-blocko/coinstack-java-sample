package io.blocko.coinstack.sample;

import io.blocko.coinstack.CoinStackClient;
import io.blocko.coinstack.ECKey;
import io.blocko.coinstack.Endpoint;
import io.blocko.coinstack.exception.CoinStackException;
import io.blocko.coinstack.model.CredentialsProvider;
import io.blocko.coinstack.openkeychain.InMemoryKeyManager;
import io.blocko.coinstack.openkeychain.KeyManager;

public class CoinStackManager {
	private static CoinStackManager _instance = null;
	public static CoinStackManager getInstance() {
		if (_instance == null) {
			synchronized (CoinStackManager.class) {
				if (_instance == null) {
					_instance = new CoinStackManager();
				}
			}
		}
		return _instance;
	}
	
	private static final String SERVER_PRIVATE_KEY = "KxQA7uBs4ZtaA8nxN2vLcz2WkoxtHJgz7AMaU9JFBzX6vwBhJXSp"; // SECRET: private key for test
	//private static final String SERVER_AUTHORITY_ADDRESS = "1DvSQTCeb4LM6ANK8hHoh1J16fp7FEHn5C";
	
	private CoinStackClient coinstack = null;
	private KeyManager keyManager = null;
	
	private CoinStackManager() {
		this.coinstack = initCoinStack();
		this.keyManager = initKeyManager(SERVER_PRIVATE_KEY);
	}
	
	
	public CoinStackClient getCoinStackClient() {
		return coinstack;
	}
	public KeyManager getKeyManager() {
		return keyManager;
	}
	
	
	public static CoinStackClient initCoinStack() {
		return new CoinStackClient(new CredentialsProvider() {
			@Override
			public String getAccessKey() {
				return "17155ccf15e603853c19a35559f3f5"; // coinstack api key: "coinstack-sample-java"
			}
			@Override
			public String getSecretKey() {
				return "4ffe022576916bf0d9c4c13718d582";
			}
		}, Endpoint.MAINNET);
	}
	public static KeyManager initKeyManager(String privateKey) {
		try {
			String authAddress = ECKey.deriveAddress(privateKey);
			return initKeyManager(privateKey, authAddress);
		} catch (CoinStackException e) {
			throw new RuntimeException(e);
		}
	}
	public static KeyManager initKeyManager(String privateKey, String authAddress) {
		KeyManager keyManager = new InMemoryKeyManager();
		keyManager.registerKey(authAddress, privateKey.toCharArray());
		return keyManager;
	}
}
