import java.io.IOException;

import io.blocko.coinstack.CoinStackClient;
import io.blocko.coinstack.exception.CoinStackException;
import io.blocko.coinstack.model.Output;

public class SampleAddress {

	public static void main(String[] args) throws IOException, CoinStackException {
		System.out.println("## SampleAddress");
		
		CoinStackClient client = SampleMain.getClient();
		String address = SampleMain.SAMPLE_ADDRESS;
		
		
		System.out.println("### sampleBalance: "+address);
		sampleBalance(client, address);
		
		System.out.println("### sampleUtxo: "+address);
		sampleUtxo(client, address);
		
		System.out.println("### sampleHistory: "+address);
		sampleHistory(client, address);
	}
	
	
	public static void sampleBalance(CoinStackClient client, String address) throws IOException, CoinStackException {
		// 잔고 조회
		long balance = client.getBalance(address); // 1 BTC == 100,000,000 satoshi
		System.out.println("- balance: "+balance+" (satoshi)");
	}
	
	public static void sampleUtxo(CoinStackClient client, String address) throws IOException, CoinStackException {
		// UTXO 조회
		Output[] utxos = client.getUnspentOutputs(address);
		System.out.println("- utxo: cnt="+utxos.length);
		
		for (Output utxo : utxos) {
			String txIdReverseOrdered = utxo.getTransactionId(); // Caution: reverse byte order
			int vout = utxo.getIndex();
			long value = utxo.getValue();
			System.out.printf("  utxo[] txIdReverseOrdered=%s, index=%d, value=%d\n",
					txIdReverseOrdered, vout, value);
		}
	}
	
	public static void sampleHistory(CoinStackClient client, String address) throws IOException, CoinStackException {
		// 거래내역 조회
		String[] txIds = client.getTransactions(address);
		System.out.println("- txIds: cnt="+txIds.length);
		
		for (int i=0; i<txIds.length; i++) {
			if (i >= 5) {
				System.out.println("  ...");
				break;
			}
			SampleBlockchain.sampleTx(client, txIds[i]);
		}
	}

}
