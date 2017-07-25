import java.io.IOException;
import java.security.PublicKey;
import java.util.Arrays;

import io.blocko.coinstack.AbstractEndpoint;
import io.blocko.coinstack.CoinStackClient;
import io.blocko.coinstack.ECKey;
import io.blocko.coinstack.Math;
import io.blocko.coinstack.TransactionBuilder;
import io.blocko.coinstack.TransactionUtil;
import io.blocko.coinstack.exception.CoinStackException;
import io.blocko.coinstack.model.Block;
import io.blocko.coinstack.model.BlockchainStatus;
import io.blocko.coinstack.model.CredentialsProvider;
import io.blocko.coinstack.model.Input;
import io.blocko.coinstack.model.Output;
import io.blocko.coinstack.model.Transaction;
import io.blocko.coinstack.util.Codecs;

public class SampleMain {
	
	public static void main(String[] args) {
		String privateKeyWIF = "L5Ln2xNzDQ9S7d1Ly6v8Dx1zQQxPqGEtFpoVJJjDxC5qW5xmhLRd";
		String address = "1J6pc2mhPPiK6uUYtncjQwbP42n8p13SfH";
		String receiverAddr1 = "1A65VHc4ZGErUB6PbnHaFHV83smFhMwv7W";
		String receiverAddr2 = "1ABZFRrv6FiriYfsYhtz3Usuh4okr1zni2";
		try {
			CoinStackClient client = createNewClient();
			sampleStatus(client);
			sampleKey(client);
			sampleSendTx(client, privateKeyWIF, receiverAddr1, "0.0001");
			sampleDataTx(client, privateKeyWIF, receiverAddr2, "0.0001", "SAMPLE_TEXT".getBytes());
			sampleAddress(client, address);
			sampleHistory(client, address);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (CoinStackException e) {
			e.printStackTrace();
		}
	}
	
	public static CoinStackClient createNewClient() {
		CredentialsProvider credentials = null;
		AbstractEndpoint endpoint = new AbstractEndpoint() {
			@Override
			public String endpoint() {
				return "http://testchain.blocko.io";
			}
			@Override
			public boolean mainnet() {
				return true;
			}
			@Override
			public PublicKey getPublicKey() {
				return null;
			}
		};
		CoinStackClient client = new CoinStackClient(credentials, endpoint);
		return client;
	}
	
	
	public static void sampleStatus(CoinStackClient client) throws IOException, CoinStackException {
		// 블록체인 상태 조회
		BlockchainStatus status = client.getBlockchainStatus();
		String bestBlockHash = status.getBestBlockHash();
		int bestHeight = status.getBestHeight();
		
		// 블록 조회
		Block block = client.getBlock(bestBlockHash);
		String blockId = block.getBlockId();
		int height = block.getHeight();
		String parentBlockId = block.getParentId();
		String[] childBlockIds = block.getChildIds();
		String[] txIds = block.getTransactionIds();
		
		// 트랜잭션 조회
		String coinbaseTxId = txIds[0];
		Transaction tx = client.getTransaction(coinbaseTxId);
		String txId = tx.getId();
		Input[] inputs = tx.getInputs();
		Output[] outputs = tx.getOutputs();
	}
	

	public static void sampleKey(CoinStackClient client) throws IOException, CoinStackException {
		// 개인 키 생성
		String privateKeyWIF = ECKey.createNewPrivateKey();
		
		// 주소 생성
		String address = ECKey.deriveAddress(privateKeyWIF);
	}
	

	public static void sampleAddress(CoinStackClient client, String address) throws IOException, CoinStackException {
		// 잔고 조회
		long balance = client.getBalance(address); // 1 BTC == 100,000,000 satoshi 
		System.out.println("- address: "+address);
		System.out.println("- balance: "+balance);
		
		// UTXO 조회
		Output[] utxos = client.getUnspentOutputs(address);
		for (Output utxo : utxos) {
			String txId = utxo.getTransactionId(); // Caution: reverse byte order
			int vout = utxo.getIndex();
			long value = utxo.getValue();
			System.out.printf("- utxo[] txId=%s, vout=%d, value=%d\n",
					utxo.getTransactionId(), utxo.getIndex(), utxo.getValue());
		}
	}
	

	public static void sampleHistory(CoinStackClient client, String address) throws IOException, CoinStackException {
		// 거래내역 조회
		String[] txIds = client.getTransactions(address);
		for (int i=0; i<txIds.length; i++) {
			if (i >= 5) break;
			String txId = txIds[i];
			System.out.println("- txId: "+txId);
			Transaction tx = client.getTransaction(txId);
			String[] blockIds = tx.getBlockIds();
			int[] blockHeights = tx.getBlockHeights();
			System.out.println("  - blockIds: "+Arrays.toString(blockIds));
			System.out.println("  - blockHeights: "+Arrays.toString(blockHeights));
			Input[] inputs = tx.getInputs();
			long sumInputs = 0;
			for (Input input : inputs) {
				sumInputs += input.getValue();
				System.out.printf("  - input[]: txId=%s, vout=%d, addr=%s, value=%d\n",
						input.getOutputTransactionId(), input.getOutputIndex(),
						input.getOutputAddress(), input.getValue());
			}
			Output[] outputs = tx.getOutputs();
			long sumOutputs = 0;
			for (Output output : outputs) {
				sumOutputs += output.getValue();
				byte[] data = output.getData();
				String hexData = (data == null) ? null : Codecs.HEX.encode(data);
				System.out.printf("  - output[]: vout=%d, addr=%s, value=%d, data=%s\n",
						output.getIndex(), output.getAddress(), output.getValue(), hexData);
			}
			long fee = (sumInputs == 0) ? 0 : (sumInputs - sumOutputs);
			System.out.println("  - fee="+fee);
		}
	}
	

	public static void sampleSendTx(CoinStackClient client, String privateKeyWIF, String receiverAddress, String amountBTC)
			throws IOException, CoinStackException {
		long amount = Math.convertToSatoshi(amountBTC);
		TransactionBuilder txBuilder = new TransactionBuilder();
		txBuilder.allowDustyOutput(true);
		txBuilder.shuffleOutputs(false);
		txBuilder.setFee(Math.convertToSatoshi("0.0001"));
		txBuilder.addOutput(receiverAddress, amount);
		String rawTx = txBuilder.buildTransaction(client, privateKeyWIF);
		String txId = TransactionUtil.getTransactionHash(rawTx);
		client.sendTransaction(rawTx);
		System.out.printf("- sendTx: txId=%s, receiverAddr=%s, amountBTC=%s\n",
				txId, receiverAddress, amountBTC);
	}


	public static void sampleDataTx(CoinStackClient client, String privateKeyWIF, String receiverAddress, String amountBTC, byte[] data)
			throws IOException, CoinStackException {
		long amount = Math.convertToSatoshi(amountBTC);
		TransactionBuilder txBuilder = new TransactionBuilder();
		txBuilder.allowDustyOutput(true);
		txBuilder.shuffleOutputs(false);
		txBuilder.setFee(Math.convertToSatoshi("0.0001"));
		txBuilder.addOutput(receiverAddress, amount);
		txBuilder.allowLargePayload(true); // data size more than 80 byte
		txBuilder.setData(data);
		String rawTx = txBuilder.buildTransaction(client, privateKeyWIF);
		String txId = TransactionUtil.getTransactionHash(rawTx);
		client.sendTransaction(rawTx);
		System.out.printf("- sendTx: txId=%s, receiverAddr=%s, amountBTC=%s, data=%s\n",
				txId, receiverAddress, amountBTC, Codecs.HEX.encode(data));
	}


}
