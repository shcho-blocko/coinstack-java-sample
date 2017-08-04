import java.io.IOException;

import io.blocko.coinstack.CoinStackClient;
import io.blocko.coinstack.Math;
import io.blocko.coinstack.TransactionBuilder;
import io.blocko.coinstack.TransactionUtil;
import io.blocko.coinstack.exception.CoinStackException;
import io.blocko.coinstack.util.Codecs;

public class SampleTxBuild {
	
	public static void main(String[] args) throws IOException, CoinStackException {
		System.out.println("## SampleTxBuild");
		
		CoinStackClient client = SampleMain.getClient();
		String privateKeyWIF = SampleMain.SAMPLE_PRIVKEY;
		String receiverAddr1 = SampleMain.SAMPLE_RECEIVER_ADDR1;
		String receiverAddr2 = SampleMain.SAMPLE_RECEIVER_ADDR2;
		
		
		System.out.println("### sampleSendTx");
		sampleSendTx(client, privateKeyWIF, receiverAddr1, "0.0001");
		
		System.out.println("### sampleDataTx");
		sampleDataTx(client, privateKeyWIF, receiverAddr2, "0.0001", "SAMPLE_TEXT".getBytes());
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
		
		SampleBlockchain.sampleTx(client, txId);
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
		
		SampleBlockchain.sampleTx(client, txId);
	}
	
}
