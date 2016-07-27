package io.blocko.coinstack.sample;

import org.junit.Test;

import io.blocko.coinstack.ECKey;
import io.blocko.coinstack.exception.MalformedInputException;

public class TestGenerateKey {
	@Test
	public void testGenerateKey() throws MalformedInputException {
		String privateKey = ECKey.createNewPrivateKey();
		String authorityAddress = ECKey.deriveAddress(privateKey);
		System.out.println("privateKey: "+privateKey);
		System.out.println("authorityAddress: "+authorityAddress);
	}
	
	@Test
	public void testGenerateKeyForTestNet() throws MalformedInputException {
		boolean isMainNet = false;
		String privateKey = ECKey.createNewPrivateKey(isMainNet);
		String authorityAddress = ECKey.deriveAddress(privateKey, isMainNet);
		System.out.println("[TestNet] privateKey: "+privateKey);
		System.out.println("[TestNet] authorityAddress: "+authorityAddress);
	}
}
