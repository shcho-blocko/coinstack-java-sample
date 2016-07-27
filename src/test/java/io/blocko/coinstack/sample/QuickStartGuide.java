package io.blocko.coinstack.sample;

import static org.junit.Assert.*;

import org.junit.*;

import io.blocko.coinstack.CoinStackClient;
import io.blocko.coinstack.Endpoint;
import io.blocko.coinstack.model.Block;
import io.blocko.coinstack.model.BlockchainStatus;
import io.blocko.coinstack.model.CredentialsProvider;
import io.blocko.coinstack.model.Transaction;

public class QuickStartGuide {
	
	CoinStackClient client = null;
	
	@Before
	public void before() {
		client = new CoinStackClient(new CredentialsProvider() {
			@Override
			public String getAccessKey() {
				return "17155ccf15e603853c19a35559f3f5";
			}
			@Override
			public String getSecretKey() {
				return "4ffe022576916bf0d9c4c13718d582";
			}
		}, Endpoint.MAINNET);
	}
	
	@After
	public void after() {
		client.close();
	}
	
	@Test
	public void testMain() throws Exception {
		assertTrue(true);
		//testQuickStartBlock();
		testQuickStartAddress();
	}
	
	public void testQuickStartBlock() throws Exception {
		// Blockchain 상태 조회, Best Height / Best Block Hash 출력
		BlockchainStatus status = client.getBlockchainStatus();
		System.out.println("**** BlockChainStatus");
		System.out.println("bestHeight: "+status.getBestHeight());
		System.out.println("bestBlockHash: "+status.getBestBlockHash());
		
		// Block 조회
		System.out.println("**** Block");
		String blockId = status.getBestBlockHash();
		System.out.println("blockId: "+blockId);
		Block block = client.getBlock(blockId);
		assertNotNull(block);
		System.out.println("blockId: "+block.getBlockId());
		System.out.println("parentId: "+block.getParentId());
		System.out.println("height: "+block.getHeight());
		
		// Transaction 조회
		System.out.println("**** Transaction");
		String[] txIds = block.getTransactionIds();
		assertTrue(txIds != null && txIds.length > 0);
		String txId = txIds[1];
		System.out.println("txId: "+txId);
		Transaction tx = client.getTransaction(txId);
		assertNotNull(tx);
		System.out.println("txId: "+tx.getId());
		System.out.println("ins.cnt: "+tx.getInputs().length);
		System.out.println("outs.cnt: "+tx.getOutputs().length);
	}
	
	public void testQuickStartAddress() throws Exception {
		// Address 조회, Balance 출력
		String YOUR_BLOCKCHAIN_ADDRESS = null;
		YOUR_BLOCKCHAIN_ADDRESS = "13YcaCxxFoBmkciWT9ScyHF2Uij6X14Lxo";
		
		System.out.println("**** Balance");
		long balance = client.getBalance(YOUR_BLOCKCHAIN_ADDRESS);
		System.out.println("balance: "+balance);
		
		System.out.println("**** Transactions");
		String[] txIds = client.getTransactions(YOUR_BLOCKCHAIN_ADDRESS);
		for (String txId : txIds) {
			System.out.println("txIds[]: "+txId);
		}
	}
}
