import java.io.IOException;
import java.security.PublicKey;

import io.blocko.coinstack.*;
import io.blocko.coinstack.exception.*;
import io.blocko.coinstack.model.*;

public class SampleMain {
	
	public static String SAMPLE_BLOCK_ID = "00001b9ff6cd68169cacec7adc9de065fd0d8ee9c8a2ded2d511492caf04f4d2";
	public static String[] SAMPLE_TX_IDS = new String[] {
			"b1aaea775b442f41c16e088041400ee540e97e37c4f7f4458af44c8ceb12999e",
			"d146389546a3a13aa88d1271e655c48e6995c144c59710de41303f83c5685271",
	};
	
	public static String SAMPLE_PRIVKEY = "L5Ln2xNzDQ9S7d1Ly6v8Dx1zQQxPqGEtFpoVJJjDxC5qW5xmhLRd";
	public static String SAMPLE_ADDRESS = "1J6pc2mhPPiK6uUYtncjQwbP42n8p13SfH";
	
	public static String SAMPLE_RECEIVER_ADDR1 = "1A65VHc4ZGErUB6PbnHaFHV83smFhMwv7W";
	public static String SAMPLE_RECEIVER_ADDR2 = "1ABZFRrv6FiriYfsYhtz3Usuh4okr1zni2";
	
	public static byte[] SAMPLE_TX_DATA = "SAMPLE_TEXT".getBytes();
	//public static byte[] SAMPLE_TX_DATA = "SAMPLE_TEXT".getBytes();
	
	public static String SAMPLE_STAMP_PRIVKEY = "KyvFMNtJgRDo5Rp6RkcSfw9ghH88DdYRCwD4X5iCwiJHkridX2XU";
	//public static String SAMPLE_STAMP_ADDRESS = "17KLrfXXauAQYb6VcjkY5WpS11cxT8wkmb";
	public static String SAMPLE_STAMP_DOCUMENT_HASH = "b1674191a88ec5cdd733e4240a81803105dc412d6c6708d53ab94fc248f4f553";
	public static String SAMPLE_STAMP_ID = "f2086e1f2d1eef047140b1bcb4893bc45bed9194ba24b7d403e9486c0f26e6d3-0";
	
	
	
	public static CoinStackClient createNewClient() {
		// Client 객체 생성
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
	
	
	
	public static CoinStackClient _client = null;
	
	public static CoinStackClient getClient() {
		if (_client == null) {
			_client = createNewClient();
		}
		return _client;
	}
	
	public static void main(String[] args) throws IOException, CoinStackException {
		System.out.println("# SampleMain");
		
		CoinStackClient client = createNewClient();
		BlockchainStatus status = client.getBlockchainStatus();
		System.out.println("- bestBlockHash: "+status.getBestBlockHash());
		System.out.println("  bestHeight: "+status.getBestHeight());
		
		sampleAll();
	}
	
	public static void sampleAll() throws IOException, CoinStackException {
		System.out.println("## SampleAll");
		System.out.println("- blockchain, key, address, tx, tx build");
		
		// - blockchain status
		// - get block
		SampleBlockchain.main(null);
		
		// - create private key
		// - derive address
		SampleKey.main(null);
		
		// - get balance
		// - get utxo
		// - get transaction history
		SampleAddress.main(null);
		
		// - get transaction
		SampleTx.main(null);
		
		// - build transaction
		SampleTxBuild.main(null);
		
		// - stamp
		SampleStamp.main(null);
	}
	
}
