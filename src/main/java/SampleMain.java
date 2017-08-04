import java.io.IOException;
import java.security.PublicKey;

import io.blocko.coinstack.AbstractEndpoint;
import io.blocko.coinstack.CoinStackClient;
import io.blocko.coinstack.exception.CoinStackException;
import io.blocko.coinstack.model.CredentialsProvider;

public class SampleMain {
	
	public static String SAMPLE_BLOCK_ID = "00001294b6960ac809f5da28edacd301ae2aee272249d60fa248549eb08905d2";
	public static String[] SAMPLE_TX_IDS = new String[] {
			"306b7f360f3acca49c9b9b5f10b17069db638c1678a103079014a87cc0354f4c",
			"5c2fcd62c20713143b15a2d12400794719a36ace52d8e4ec0666d206bc79e330",
	};
	
	public static String SAMPLE_PRIVKEY = "L5Ln2xNzDQ9S7d1Ly6v8Dx1zQQxPqGEtFpoVJJjDxC5qW5xmhLRd";
	public static String SAMPLE_ADDRESS = "1J6pc2mhPPiK6uUYtncjQwbP42n8p13SfH";
	
	public static String SAMPLE_RECEIVER_ADDR1 = "1A65VHc4ZGErUB6PbnHaFHV83smFhMwv7W";
	public static String SAMPLE_RECEIVER_ADDR2 = "1ABZFRrv6FiriYfsYhtz3Usuh4okr1zni2";
	
	
	
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
		
		
		// - create private key
		// - derive address
		SampleKey.main(args);
		
		// - get blockchain status
		// - get block
		// - get transaction
		SampleBlockchain.main(args);
		
		// - get balance
		// - get utxo
		// - get transaction history
		SampleAddress.main(args);
		
		// - build transaction
		SampleTxBuild.main(args);
		
	}
	
}
