package io.blocko.coinstack.study;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.*;

import io.blocko.coinstack.CoinStackClient;
import io.blocko.coinstack.ECKey;
import io.blocko.coinstack.exception.MalformedInputException;
import io.blocko.coinstack.openkeychain.KeyManager;
import io.blocko.coinstack.openkeychain.client.LoginManager;
import io.blocko.coinstack.openkeychain.model.Challenge;
import io.blocko.coinstack.openkeychain.model.Response;
import io.blocko.coinstack.openkeychain.server.ChallengeResponseManager;
import io.blocko.coinstack.openkeychain.server.RegistrationManager;

public class TestAuth extends AbstractTest {
	
	public static class InMemoryKeyManager extends KeyManager {
		private char[] key;
		private String address;
		
		@Override
		public char[] fetchPrivateKey() {
			return this.key;
		}
		
		@Override
		public void registerKey(String address, char[] privateKey) {
			this.key = privateKey;
			this.address = address;
		}
		
		@Override
		public String fetchAddress() {
			return this.address;
		}
	}
	
	private static final String PRIVATE_KEY = "KytX3e2LF5ia8uiydXMhcZyDEM3D7PcDuHJ32uQvRSGJP4wjKTNa";
	private static final String AUTHORITY_ADDRESS = "1FyYMVJ8L1f3Uri5iNUQix2FQ7P1KfeNo3";
	
	private KeyManager keyManager = null;
	private ChallengeResponseManager regManager = null;
	
	
	//@Test
	public void testGenerateKey() throws MalformedInputException {
		/*
		private static final String PRIVATE_KEY = "Kx5zouqiNyyMYaorh4TodriRwC9pV4zBD1gKjA58sgfuRGadDbz3";
		private static final String AUTHORITY_ADDRESS = "16pTMKr2qtB3GT8hCjZs2NmoAvNtudwaKS";
		private static final String PRIVATE_KEY = "cQdHVoYR9S8dA8sSAXfnz28WdprW4cN2N9TGwim9yFx3jBjxwPrr";
		private static final String AUTHORITY_ADDRESS = "mxo8XuGVDsCYjjw9dwe6vYSsYGH1bMSFRv";
		 */
		String privateKey = ECKey.createNewPrivateKey();
		String authorityAddress = ECKey.deriveAddress(privateKey);
		System.out.println("privateKey: "+privateKey);
		System.out.println("authorityAddress: "+authorityAddress);
	}
	
	
	@Test
	public void testRegistration() throws MalformedInputException, IOException {
		// [server side] prepare keyManager and regManager
		CoinStackClient client = getClient();
		keyManager = new InMemoryKeyManager();
		keyManager.registerKey(AUTHORITY_ADDRESS, PRIVATE_KEY.toCharArray());
		regManager = new RegistrationManager(client, keyManager);
		regManager.setThresholdMinutes(Integer.MAX_VALUE);
		System.out.println("serverAddress: "+AUTHORITY_ADDRESS);
		
		
		// [client side] prepare clientKeyManager and clientLoginManager
		String clientPrivateKey = ECKey.createNewPrivateKey();
		String clientAddress = ECKey.deriveAddress(clientPrivateKey);
		System.out.println("clientAddress: "+clientAddress);
		KeyManager clientKeyManager = new InMemoryKeyManager();
		clientKeyManager.registerKey(clientAddress, clientPrivateKey.toCharArray());
		LoginManager clientLoginManager = new LoginManager(client, clientKeyManager);
		// [client side] send request to server
		
		
		// [server side] create challenge and send to client
		String TEST_CHALLENGE_CONTEXT = "TEST_"+System.currentTimeMillis();
		Challenge challenge = regManager.createChallenge(TEST_CHALLENGE_CONTEXT);
		assertNotNull(challenge);
		//System.out.println("challenge: "+challenge.marshal());
		
		
		// [client side] check challenge and send response to server
		assertTrue(clientLoginManager.checkChallenge(challenge, AUTHORITY_ADDRESS));
		System.out.println("get challenge context: "+challenge.getContext());
		Response response = clientLoginManager.createResponse(challenge);
		assertNotNull(response);
		//System.out.println("response: "+response.marshal());
		
		
		// [server side] check response
		System.out.println("check response context: "+TEST_CHALLENGE_CONTEXT);
		assertTrue(regManager.checkResponse(TEST_CHALLENGE_CONTEXT, response));
		// [server side] registration
	}
	
}
