import java.io.IOException;

import io.blocko.coinstack.*;
import io.blocko.coinstack.Math;
import io.blocko.coinstack.exception.*;
import io.blocko.coinstack.util.*;

public class SampleTxBuild {
	
	public static void main(String[] args) throws IOException, CoinStackException {
		System.out.println("## SampleTxBuild");
		
		CoinStackClient client = SampleMain.getClient();
		String privateKeyWIF = SampleMain.SAMPLE_PRIVKEY;
		String receiverAddr1 = SampleMain.SAMPLE_RECEIVER_ADDR1;
		String receiverAddr2 = SampleMain.SAMPLE_RECEIVER_ADDR2;
		byte[] txdata = SampleMain.SAMPLE_TX_DATA;
		
		
		System.out.println("### send simple tx");
		sampleSendSimpleTx(client, privateKeyWIF, receiverAddr1, "0.0001");
		
		System.out.println("### send data tx");
		sampleSendDataTx(client, privateKeyWIF, receiverAddr2, "0.0001", txdata);
	}
	
	
	public static void sampleSendSimpleTx(CoinStackClient client, String privateKeyWIF, String receiverAddress, String amountBTC)
			throws IOException, CoinStackException {
		// txbuilder instance
		TransactionBuilder txBuilder = new TransactionBuilder();
		txBuilder.allowDustyOutput(true);
		txBuilder.shuffleOutputs(false);
		
		// set fee
		txBuilder.setFee(Math.convertToSatoshi("0.0001"));
		
		// add output
		long amount = Math.convertToSatoshi(amountBTC);
		txBuilder.addOutput(receiverAddress, amount);
		
		// build tx
		String rawTx = txBuilder.buildTransaction(client, privateKeyWIF);
		String txId = TransactionUtil.getTransactionHash(rawTx);
		
		// send tx
		client.sendTransaction(rawTx);
		System.out.printf("- sendTx: txId=%s, receiverAddr=%s, amountBTC=%s\n",
				txId, receiverAddress, amountBTC);
		
		// get tx
		SampleTx.sampleGetTx(client, txId);
	}
	
	
	public static void sampleSendDataTx(CoinStackClient client, String privateKeyWIF, String receiverAddress, String amountBTC, byte[] data)
			throws IOException, CoinStackException {
		// txbuilder instance
		TransactionBuilder txBuilder = new TransactionBuilder();
		txBuilder.allowDustyOutput(true);
		txBuilder.shuffleOutputs(false);
		
		// set fee
		txBuilder.setFee(Math.convertToSatoshi("0.0001"));
		
		// add output
		long amount = Math.convertToSatoshi(amountBTC);
		txBuilder.addOutput(receiverAddress, amount);
		
		// add data output
		txBuilder.allowLargePayload(true); // data size more than 80 byte
		txBuilder.setData(data);
		
		// build tx
		String rawTx = txBuilder.buildTransaction(client, privateKeyWIF);
		String txId = TransactionUtil.getTransactionHash(rawTx);
		
		// send tx
		client.sendTransaction(rawTx);
		System.out.printf("- sendTx: txId=%s, receiverAddr=%s, amountBTC=%s, data=%s\n",
				txId, receiverAddress, amountBTC, Codecs.HEX.encode(data));
		
		// get tx
		SampleTx.sampleGetTx(client, txId);
	}
	
}
