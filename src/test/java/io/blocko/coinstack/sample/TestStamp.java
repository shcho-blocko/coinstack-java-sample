package io.blocko.coinstack.sample;


import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.*;

import io.blocko.bitcoinj.core.Sha256Hash;
import io.blocko.coinstack.CoinStackClient;
import io.blocko.coinstack.exception.CoinStackException;
import io.blocko.coinstack.model.Output;
import io.blocko.coinstack.model.Stamp;
import io.blocko.coinstack.model.Transaction;
import io.blocko.spongycastle.util.encoders.Hex;

public class TestStamp extends AbstractTest {
	
	@Test
	public void testStamp() throws IOException, CoinStackException {
		String stampId = null;
		stampId = testStampDocument("HELLO_WORLD");
		/*
		stampId = "16e30a24aaa2ce6b943b237500bb3072d11ad5a7b3a3ce0a7032bb4bcf038427-0";
		stampId = "85eec47d2b6fa6f4eb358cfdd3577ee2226ac9441c20c3d88918eb50da4713d3-0";
		*/
		
		testGetStamp(stampId);
	}
	
	public String testStampDocument(String msg) throws IOException, CoinStackException {
		CoinStackClient client = getClient();
		
		Sha256Hash hash = Sha256Hash.create(msg.getBytes());
		String strHash = Hex.toHexString(hash.getBytes());
		System.out.println("strHash: "+strHash);
		
		String stampId = client.stampDocument(strHash);
		assertNotNull(stampId);
		System.out.println("stampId: "+stampId);
		return stampId;
		
		//strHash: 6f9b514093848217355d76365df1f54f42bdfd5f4e5f54a654c46b493d162c39
		//stampId: 7e7335644971d7821d89bc64e57da4cece6ffde86c97942eb7bad145acbd258f-0
	}
	
	public void testGetStamp(String stampId) throws IOException, CoinStackException {
		CoinStackClient client = getClient();
		
		Stamp stamp = client.getStamp(stampId);
		assertNotNull(stamp);
		System.out.println("TxId: "+stamp.getTxId());
		System.out.println("OutputIndex: "+stamp.getOutputIndex());
		System.out.println("Confirmations: "+stamp.getConfirmations());
		System.out.println("Timestamp: "+stamp.getTimestamp());
		
		Transaction tx = client.getTransaction(stamp.getTxId());
		assertNotNull(tx);
		Output[] outs = tx.getOutputs();
		assertNotNull(outs);
		assertTrue(outs.length > stamp.getOutputIndex());
		Output out = outs[stamp.getOutputIndex()];
		String strData = new String(Hex.encode(out.getData()));
		String strHash = strData.substring(4);
		System.out.println("strData: "+strData);
		System.out.println("strHash:     "+strHash);
		
		//TxId: 7e7335644971d7821d89bc64e57da4cece6ffde86c97942eb7bad145acbd258f
		//strData: 53316f9b514093848217355d76365df1f54f42bdfd5f4e5f54a654c46b493d162c39
		//strHash:     6f9b514093848217355d76365df1f54f42bdfd5f4e5f54a654c46b493d162c39
	}

}
