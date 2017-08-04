import java.io.IOException;
import java.util.Arrays;

import io.blocko.coinstack.CoinStackClient;
import io.blocko.coinstack.exception.CoinStackException;
import io.blocko.coinstack.model.Block;
import io.blocko.coinstack.model.BlockchainStatus;
import io.blocko.coinstack.model.Input;
import io.blocko.coinstack.model.Output;
import io.blocko.coinstack.model.Transaction;
import io.blocko.coinstack.util.Codecs;

public class SampleBlockchain {
	
	public static void main(String[] args) throws IOException, CoinStackException {
		System.out.println("## SampleBlockchain");
		
		CoinStackClient client = SampleMain.getClient();
		String blockId = SampleMain.SAMPLE_BLOCK_ID;
		String[] txIds = SampleMain.SAMPLE_TX_IDS;
		
		
		System.out.println("### sampleStatus");
		sampleStatus(client);
		
		System.out.println("### sampleBlock: "+blockId);
		sampleBlock(client, blockId);
		
		System.out.println("### sampleTx: "+Arrays.toString(txIds));
		for (String txId : txIds) {
			sampleTx(client, txId);
		}
		
	}
	
	
	public static void sampleStatus(CoinStackClient client) throws IOException, CoinStackException {
		// 블록체인 상태 조회
		BlockchainStatus status = client.getBlockchainStatus();
		String bestBlockHash = status.getBestBlockHash();
		int bestHeight = status.getBestHeight();
		System.out.println("- bestBlockHash: "+bestBlockHash);
		System.out.println("  bestHeight: "+bestHeight);
	}
	
	public static void sampleBlock(CoinStackClient client, String blockId) throws IOException, CoinStackException {
		// 블록 조회
		Block block = client.getBlock(blockId);
		int height = block.getHeight();
		String parentBlockId = block.getParentId();
		String[] childBlockIds = block.getChildIds();
		String[] txIds = block.getTransactionIds();
		System.out.printf("- block: height=%d, id=%s\n", height, block.getBlockId());
		System.out.println("  parentBlockId: "+parentBlockId);
		System.out.println("  childBlockIds: "+Arrays.toString(childBlockIds));
		System.out.println("  txIds: "+Arrays.toString(txIds));
	}
	
	public static void sampleTx(CoinStackClient client, String txId) throws IOException, CoinStackException {
		// 트랜잭션 조회
		System.out.println("- tx: id="+txId);
		Transaction tx = client.getTransaction(txId);
		String[] blockIds = tx.getBlockIds();
		int[] blockHeights = tx.getBlockHeights();
		System.out.println("  - blockIds: "+Arrays.toString(blockIds));
		System.out.println("  - blockHeights: "+Arrays.toString(blockHeights));
		
		boolean isCoinbase = tx.isCoinbase();
		if (isCoinbase) {
			System.out.println("  - isCoinbaseTx: "+isCoinbase);
		}
		
		Input[] inputs = tx.getInputs();
		System.out.println("  - inputs: cnt="+inputs.length);
		long sumOfInputValues = 0;
		for (int index=0, len=inputs.length; index<len; index++) {
			Input input = inputs[index];
			sumOfInputValues += input.getValue();
			System.out.printf("    inputs[%d]: txId=%s, outputIdx=%d, addr=%s, value=%d\n",
					index,
					input.getOutputTransactionId(), input.getOutputIndex(),
					input.getOutputAddress(), input.getValue());
		}
		System.out.println("    sumOfInputValues: "+sumOfInputValues);
		
		Output[] outputs = tx.getOutputs();
		System.out.println("  - outputs: cnt="+outputs.length);
		long sumOfOutputValues = 0;
		for (Output output : outputs) {
			sumOfOutputValues += output.getValue();
			byte[] data = output.getData();
			String hexData = (data == null) ? null : Codecs.HEX.encode(data);
			System.out.printf("    outputs[%d]: addr=%s, value=%d, data=%s\n",
					output.getIndex(),
					output.getAddress(), output.getValue(), hexData);
		}
		System.out.println("    sumOfOutputValues: "+sumOfOutputValues);
		
		long fee = (sumOfInputValues == 0)
				? 0 : (sumOfInputValues - sumOfOutputValues);
		System.out.println("  - fee="+fee);
	}
}
