package io.blocko.coinstack.sample;


import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.*;

import io.blocko.bitcoinj.core.Sha256Hash;
import io.blocko.coinstack.CoinStackClient;
import io.blocko.coinstack.exception.CoinStackException;
import io.blocko.spongycastle.util.encoders.Hex;

public class TestStamp extends AbstractTest {
	
	@Test
	public void testStamp() throws IOException, CoinStackException {
		CoinStackClient client = getClient();
		
		String testMessage = "HELLO_WORLD";
		Sha256Hash hash = Sha256Hash.create(testMessage.getBytes());
		String strHash = Hex.toHexString(hash.getBytes());
		System.out.println("strHash: "+strHash);
		
		String stampId = client.stampDocument(strHash);
		assertNotNull(stampId);
		System.out.println("stampId: "+stampId);
		
		//strHash: 6f9b514093848217355d76365df1f54f42bdfd5f4e5f54a654c46b493d162c39
		//stampId: 7e7335644971d7821d89bc64e57da4cece6ffde86c97942eb7bad145acbd258f-0
	}
}
