package io.blocko.coinstack.sample;


import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.*;

import io.blocko.bitcoinj.core.Sha256Hash;
import io.blocko.bitcoinj.core.Utils;
import io.blocko.coinstack.CoinStackClient;
import io.blocko.coinstack.InstanceFactory;
import io.blocko.coinstack.exception.CoinStackException;
import io.blocko.coinstack.model.Output;
import io.blocko.coinstack.model.Stamp;
import io.blocko.coinstack.model.Transaction;

public class TestStamp {
	
	CoinStackClient coinstack = null;
	
	@Before
	public void before() {
		coinstack = InstanceFactory.createNewCoinStackClient();
	}
	
	@After
	public void after() {
		coinstack.close();
	}
	
	
	//@Test
	public void testStamp() throws IOException, CoinStackException {
		System.out.println("## testGetStamp");
		
		String testDocumentHashHex = getDocumentHashHex();
		String stampId = testStampDocument(testDocumentHashHex);
		String stampHash = testGetStamp(stampId);
		assertEquals(testDocumentHashHex, stampHash);
		
		//hexHash: ff786d948022a3fa699ae0da8a307f39da78aa81dfc5643e831a4f3d84c825d6
		//stampId: 0c0310a449c805237f0ed636fe0f2328444fa8ff0148cc55c7377d19c187a93e-0
		System.out.println("");
	}
	
	@Test
	public void testGetStamp() throws Exception {
		System.out.println("## testGetStamp");
		String stampHash = testGetStamp("0c0310a449c805237f0ed636fe0f2328444fa8ff0148cc55c7377d19c187a93e-0");
		assertEquals("ff786d948022a3fa699ae0da8a307f39da78aa81dfc5643e831a4f3d84c825d6", stampHash);
		System.out.println("");
	}
	
	public String getDocumentHashHex() {
		byte[] data = "TEST_DOCUMENT_DATA".getBytes();
		byte[] hash = Sha256Hash.create(data).getBytes();
		String hexHash = Utils.HEX.encode(hash);
		return hexHash;
	}
	
	public String testStampDocument(String hexHash) throws IOException, CoinStackException {
		System.out.println("hexHash: "+hexHash);
		String stampId = coinstack.stampDocument(hexHash);
		assertNotNull(stampId);
		System.out.println("stampId: "+stampId);
		return stampId;
	}
	
	public String testGetStamp(String stampId) throws IOException, CoinStackException {
		Stamp stamp = coinstack.getStamp(stampId);
		assertNotNull(stamp);
		System.out.println("TxId: "+stamp.getTxId());
		System.out.println("OutputIndex: "+stamp.getOutputIndex());
		System.out.println("Confirmations: "+stamp.getConfirmations());
		System.out.println("Timestamp: "+stamp.getTimestamp());
		
		Transaction tx = coinstack.getTransaction(stamp.getTxId());
		assertNotNull(tx);
		Output[] outs = tx.getOutputs();
		assertNotNull(outs);
		assertTrue(outs.length > stamp.getOutputIndex());
		Output out = outs[stamp.getOutputIndex()];
		String strData = Utils.HEX.encode(out.getData());
		String strHash = strData.substring(4);
		System.out.println("strData: "+strData);
		System.out.println("strHash:     "+strHash);
		return strHash;
	}

}
