package io.blocko.coinstack.sample;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.*;

import io.blocko.coinstack.CoinStackClient;
import io.blocko.coinstack.ECKey;
import io.blocko.coinstack.exception.CoinStackException;
import io.blocko.coinstack.openkeychain.KeyManager;
import io.blocko.coinstack.openkeychain.client.LoginManager;
import io.blocko.coinstack.openkeychain.model.Challenge;
import io.blocko.coinstack.openkeychain.model.RegistrationMetadata;
import io.blocko.coinstack.openkeychain.model.Response;
import io.blocko.coinstack.openkeychain.server.AuthorizationManager;
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
	
	
	/*
	 * newly generated (mainNet)
	 * ECKey.createNewPrivateKey();
	 * ECKey.deriveAddress(privateKey);
	 */
	//private static final boolean isMainNet = true;
	private static final String SERVER_PRIVATE_KEY = "KytX3e2LF5ia8uiydXMhcZyDEM3D7PcDuHJ32uQvRSGJP4wjKTNa";
	private static final String SERVER_AUTHORITY_ADDRESS = "1FyYMVJ8L1f3Uri5iNUQix2FQ7P1KfeNo3";
	private static final String CLIENT_PRIVATE_KEY = "L1TgQVi3RMGMsBvaq3jmisAX6veyGs1hBg17Vb9Q2bbjoUxgjtw4";
	private static final String CLIENT_AUTHORITY_ADDRESS = "1Q1RjMkpwaTdYAtskvCWTGrfQ5fZ3Yahas";
	
	
	private CoinStackClient coinstack = null;
	private KeyManager keyManager = null;
	
	public static String generateRandomContextString() {
		return ECKey.createNewPrivateKey();
	}
	
	@Before
	public void prepare() throws Exception {
		coinstack = getClient();
		keyManager = new InMemoryKeyManager();
		keyManager.registerKey(SERVER_AUTHORITY_ADDRESS, SERVER_PRIVATE_KEY.toCharArray());
		
		//System.out.println("server status (bestHeight): "+coinstack.getBlockchainStatus().getBestHeight());
		System.out.println("server balance: "+coinstack.getBalance(SERVER_AUTHORITY_ADDRESS));
	}
	@After
	public void fini() {
		coinstack.close();
	}
	
	@Test
	public void testRegistration() throws Exception {
		System.out.println("serverAddress: "+SERVER_AUTHORITY_ADDRESS);
		System.out.println("clientAddress: "+CLIENT_AUTHORITY_ADDRESS);
		
		// [server side] prepare keyManager and regManager
		ChallengeResponseManager regManager = new RegistrationManager(coinstack, keyManager);
		regManager.setThresholdMinutes(Integer.MAX_VALUE);
		
		// [client side] prepare clientKeyManager and clientLoginManager
		String clientPrivateKey = CLIENT_PRIVATE_KEY; // ECKey.createNewPrivateKey();
		String clientAddress = CLIENT_AUTHORITY_ADDRESS; // ECKey.deriveAddress(clientPrivateKey);
		KeyManager clientKeyManager = new InMemoryKeyManager();
		clientKeyManager.registerKey(clientAddress, clientPrivateKey.toCharArray());
		LoginManager clientLoginManager = new LoginManager(coinstack, clientKeyManager);
		// [client side] send request to server
		
		
		// [server side] create challenge and send to client
		String CHALLENGE_CONTEXT = generateRandomContextString();
		Challenge challenge = regManager.createChallenge(CHALLENGE_CONTEXT);
		//System.out.println("challenge: "+challenge.marshal());
		
		
		// [client side] check challenge and send response to server
		assertNotNull(challenge);
		assertTrue(clientLoginManager.checkChallenge(challenge, SERVER_AUTHORITY_ADDRESS));
		System.out.println("get challenge context: "+challenge.getContext());
		Response response = clientLoginManager.createResponse(challenge);
		//System.out.println("response: "+response.marshal());
		
		
		// [server side] check response
		assertNotNull(response);
		assertTrue(regManager.checkResponse(CHALLENGE_CONTEXT, response));
		String buf = new String(io.blocko.apache.commons.codec.binary.Base64.decodeBase64(response.getChallenge()));
		Challenge clientReceived = Challenge.unmarshal(buf);
		System.out.println("check response context: "+clientReceived.getContext());
		
		// [server side] record registration
		testRegistration(response.getCertificate()); // use BTC
		
		// [server side] check registration
		testSignin(response.getCertificate());
	}
	
	public void testRegistration(String certificateAddress) throws IOException {
		RegistrationManager regManager = new RegistrationManager(coinstack, keyManager);
		RegistrationMetadata metadata = new RegistrationMetadata() {
			@Override
			public byte[] marshal() {
				return "[testmeta]".getBytes();
			}
		};
		System.out.println("test registration metadata: "+new String(metadata.marshal()));
		
		String txId = regManager.recordRegistration(certificateAddress, metadata);
		System.out.println("test registration txId: "+txId);
	}
	
	public void testSignin(String certificateAddress) throws IOException, CoinStackException {
		AuthorizationManager authManager = new AuthorizationManager(coinstack, keyManager);
		byte[] metadata = authManager.checkRegistration(certificateAddress);
		if (metadata == null) {
			System.out.println("check registration fail.");
		}
		else {
			System.out.println("check registration success.");
			System.out.println("metadata: "+new String(metadata));
		}
	}
}
