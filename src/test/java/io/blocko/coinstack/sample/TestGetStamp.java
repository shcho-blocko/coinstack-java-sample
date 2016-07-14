package io.blocko.coinstack.sample;


import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.*;

import io.blocko.coinstack.CoinStackClient;
import io.blocko.coinstack.exception.CoinStackException;
import io.blocko.coinstack.model.Output;
import io.blocko.coinstack.model.Stamp;
import io.blocko.coinstack.model.Transaction;
import io.blocko.spongycastle.util.encoders.Hex;

public class TestGetStamp extends AbstractTest {
	
	@Test
	public void testGetStamp() throws IOException, CoinStackException {
		CoinStackClient client = getClient();
		
		String stampId = "7e7335644971d7821d89bc64e57da4cece6ffde86c97942eb7bad145acbd258f-0";
		Stamp stamp = client.getStamp(stampId);
		assertNotNull(stamp);
		System.out.println("TxId: "+stamp.getTxId());
		
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
