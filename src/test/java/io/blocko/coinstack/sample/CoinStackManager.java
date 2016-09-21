package io.blocko.coinstack.sample;

import io.blocko.coinstack.CoinStackClient;
import io.blocko.coinstack.ECKey;
import io.blocko.coinstack.Endpoint;
import io.blocko.coinstack.exception.CoinStackException;
import io.blocko.coinstack.exception.MalformedInputException;
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
	
	private static final String SERVER_PRIVATE_KEY = ""; // TODO SECRET: need server private key
	private static final String SERVER_AUTHORITY_ADDRESS = "1LNerxk3A4KDtoXMtYXLfL3LRnhjvwkC55";
	
	private CoinStackClient coinstack = null;
	private KeyManager keyManager = null;
	
	private CoinStackManager() {
		this.coinstack = initCoinStack();
		this.keyManager = initKeyManager(SERVER_PRIVATE_KEY, SERVER_AUTHORITY_ADDRESS);
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
	public static boolean checkValidKeyPair(KeyManager keyManager) {
		if (keyManager == null) {
			return false;
		}
		char[] pkey = keyManager.fetchPrivateKey();
		if (pkey == null || pkey.length == 0) {
			return false;
		}
		String addr = keyManager.fetchAddress();
		if (addr == null || addr.isEmpty()) {
			return false;
		}
		try {
			String derivedAddr = ECKey.deriveAddress(new String(pkey));
			return (addr.equals(derivedAddr));
		} catch (MalformedInputException e) {
			throw new RuntimeException(e);
		}
	}
}
