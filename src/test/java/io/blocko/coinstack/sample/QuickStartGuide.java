package io.blocko.coinstack.sample;

import static org.junit.Assert.*;

import org.junit.*;

import io.blocko.coinstack.CoinStackClient;
import io.blocko.coinstack.model.Block;
import io.blocko.coinstack.model.BlockchainStatus;
import io.blocko.coinstack.model.Transaction;

public class QuickStartGuide {
	
	CoinStackClient coinstack = null;
	
	@Before
	public void before() {
		coinstack = CoinStackManager.getInstance().getCoinStackClient();
	}
	
	@After
	public void after() {
		coinstack.close();
	}
	
	
	@Test
	public void testMain() throws Exception {
		assertTrue(true);
		testQuickStartBestBlock();
		testQuickStartAddress();
	}
	
	public void testQuickStartBestBlock() throws Exception {
		System.out.println("## testQuickStartBestBlock\n");
		
		// Blockchain 상태 조회, Best Height / Best Block Hash 출력
		BlockchainStatus status = coinstack.getBlockchainStatus();
		System.out.println("**** BlockChainStatus");
		System.out.println("bestHeight: "+status.getBestHeight());
		System.out.println("bestBlockHash: "+status.getBestBlockHash());
		System.out.println("");
		
		// Block 조회
		System.out.println("**** Block");
		String blockId = status.getBestBlockHash();
		System.out.println("blockId: "+blockId);
		Block block = coinstack.getBlock(blockId);
		assertNotNull(block);
		System.out.println("blockId: "+block.getBlockId());
		System.out.println("parentId: "+block.getParentId());
		System.out.println("height: "+block.getHeight());
		System.out.println("");
		
		// Transaction 조회
		System.out.println("**** Transaction");
		String[] txIds = block.getTransactionIds();
		assertNotNull(txIds);
		for (int x=0, len=txIds.length; x<len; x++) {
			String txId = txIds[x];
			System.out.println("- txId: "+txId);
			Transaction tx = coinstack.getTransaction(txId);
			assertNotNull(tx);
			System.out.println("  ins.cnt: "+tx.getInputs().length);
			System.out.println("  outs.cnt: "+tx.getOutputs().length);
			if (x > 3) {
				break;
			}
		}
		System.out.println("");
	}
	
	public void testQuickStartAddress() throws Exception {
		System.out.println("## testQuickStartAddress\n");
		
		// Address 조회, Balance 출력
		String YOUR_BLOCKCHAIN_ADDRESS = null;
		YOUR_BLOCKCHAIN_ADDRESS = "13YcaCxxFoBmkciWT9ScyHF2Uij6X14Lxo";
		
		System.out.println("**** Balance");
		long balance = coinstack.getBalance(YOUR_BLOCKCHAIN_ADDRESS);
		System.out.println("balance: "+balance);
		System.out.println("");
		
		System.out.println("**** Transactions");
		String[] txIds = coinstack.getTransactions(YOUR_BLOCKCHAIN_ADDRESS);
		for (String txId : txIds) {
			System.out.println("txIds[]: "+txId);
		}
		System.out.println("");
	}
}
