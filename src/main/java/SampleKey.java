import java.io.IOException;

import io.blocko.coinstack.ECKey;
import io.blocko.coinstack.exception.CoinStackException;

public class SampleKey {
	
	public static void main(String[] args) throws IOException, CoinStackException {
		System.out.println("## SampleKey");
		
		
		System.out.println("### sampleCreateKey");
		sampleCreateKey();
	}
	
	
	public static void sampleCreateKey() throws IOException, CoinStackException {
		// 개인 키 생성
		String privateKeyWIF = ECKey.createNewPrivateKey();
		
		// 주소 생성
		String address = ECKey.deriveAddress(privateKeyWIF);
		
		System.out.println("- privateKeyWIF: "+privateKeyWIF);
		System.out.println("  address: "+address);
	}
}
