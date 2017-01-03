package io.blocko.coinstack.sample;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.*;

import io.blocko.coinstack.CoinStackClient;
import io.blocko.coinstack.exception.CoinStackException;
import io.blocko.coinstack.InstanceFactory;
import io.blocko.coinstack.model.Block;
import io.blocko.coinstack.model.BlockchainStatus;
import io.blocko.coinstack.model.Transaction;

public class QuickStartGuide {
	
	CoinStackClient coinstack = null;
	
	@Before
	public void before() {
		coinstack = InstanceFactory.createNewCoinStackClient();
	}
	
	@After
	public void after() {
		coinstack.close();
	}
	
	
	@Test
	public void quickStartGuide() throws Exception {
		testBestBlock();
		testTransactions();
		testAddress();
	}
	
	public void testBestBlock() throws IOException, CoinStackException {
		System.out.println("## BestBlock");
		
		// Blockchain 상태 조회, Best Height / Best Block Hash 출력
		BlockchainStatus status = coinstack.getBlockchainStatus();
		assertNotNull(status);
		System.out.println("- BlockChainStatus");
		System.out.println("  bestBlockHash: "+status.getBestBlockHash());
		System.out.println("  bestHeight: "+status.getBestHeight());
		System.out.println("");
	}
	
	public void testTransactions() throws IOException, CoinStackException {
		System.out.println("## Transactions");
		
		// BestBlock 조회
		System.out.println("- Block");
		BlockchainStatus status = coinstack.getBlockchainStatus();
		String blockId = status.getBestBlockHash();
		System.out.println("  blockId: "+blockId);
		Block block = coinstack.getBlock(blockId);
		assertNotNull(block);
		System.out.println("  height: "+block.getHeight());
		System.out.println("  parentId: "+block.getParentId());
		
		// Transaction 조회
		System.out.println("  - Transaction");
		String[] txIds = block.getTransactionIds();
		assertNotNull(txIds);
		for (int x=1, len=txIds.length; x<len; x++) {
			String txId = txIds[x];
			System.out.println("    - txId: "+txId);
			Transaction tx = coinstack.getTransaction(txId);
			assertNotNull(tx);
			System.out.println("      inputs.cnt: "+tx.getInputs().length);
			System.out.println("      outs.cnt: "+tx.getOutputs().length);
			if (x > 3) {
				break;
			}
		}
		System.out.println("");
	}
	
	
	public void testAddress() throws IOException, CoinStackException {
		System.out.println("## Address");
		
		// Address 조회, Balance 출력
		String YOUR_BLOCKCHAIN_ADDRESS = "13YcaCxxFoBmkciWT9ScyHF2Uij6X14Lxo";
		
		System.out.println("- Address: "+YOUR_BLOCKCHAIN_ADDRESS);
		
		long balance = coinstack.getBalance(YOUR_BLOCKCHAIN_ADDRESS);
		System.out.println("- Balance: "+balance);
		
		System.out.println("- Transactions");
		String[] txIds = coinstack.getTransactions(YOUR_BLOCKCHAIN_ADDRESS);
		for (String txId : txIds) {
			System.out.println("  txIds[]: "+txId);
		}
		System.out.println("");
	}
}
