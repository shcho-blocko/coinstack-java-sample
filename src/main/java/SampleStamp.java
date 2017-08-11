import java.io.IOException;

import io.blocko.coinstack.CoinStackClient;
import io.blocko.coinstack.exception.CoinStackException;
import io.blocko.coinstack.model.Stamp;
import io.blocko.coinstack.util.Codecs;

public class SampleStamp {

	public static void main(String[] args) throws IOException, CoinStackException {
		System.out.println("## SampleStamp");
		
		CoinStackClient client = SampleMain.getClient();
		String privateKeyWIF = SampleMain.SAMPLE_STAMP_PRIVKEY;
		String documentHash = SampleMain.SAMPLE_STAMP_DOCUMENT_HASH;
		String stampId = SampleMain.SAMPLE_STAMP_ID;
		
		
		System.out.println("### stamp document");
		sampleStampDocument(client, privateKeyWIF, documentHash);
		
		System.out.println("### get stamp");
		sampleGetStamp(client, stampId);
		
		System.out.println("### new document hash");
		byte[] documentBytes = new byte[] { 0x12, 0x34, 0x56, 0x78, 0x03 };
		sampleNewDocumentHash(documentBytes);
	}
	
	public static void sampleStampDocument(CoinStackClient client, String privateKeyWIF, String documentHash) throws IOException, CoinStackException {
		System.out.println("- stampDocument: hash="+documentHash);
		String stampId = client.stampDocument(privateKeyWIF, documentHash);
		System.out.println("  stampId="+stampId);
		sampleGetStamp(client, stampId);
	}
	
	public static void sampleGetStamp(CoinStackClient client, String stampId) throws IOException, CoinStackException {
		Stamp stamp = client.getStamp(stampId);
		System.out.println("- getStamp: stampId="+stampId);
		System.out.println("  txId="+stamp.getTxId());
		System.out.println("  outIdx="+stamp.getOutputIndex());
		System.out.println("  hash="+stamp.getHash());
		System.out.println("  confirmations="+stamp.getConfirmations());
		System.out.println("  timestamp="+stamp.getTimestamp());
	}
	
	public static void sampleNewDocumentHash(byte[] documentBytes) {
		byte[] hash = Codecs.SHA256.digest(documentBytes);
		String hexHash = Codecs.HEX.encode(hash); // hex string formatted
		//String hexFormatted = Codecs.SHA256.digestEncodeHex(documentBytes);
		
		System.out.println("- document bytes: "+Codecs.HEX.encode(documentBytes));
		System.out.println("  document hash: "+hexHash);
		
	}
}
