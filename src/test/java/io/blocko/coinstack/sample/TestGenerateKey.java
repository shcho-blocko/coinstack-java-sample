package io.blocko.coinstack.sample;

import org.junit.Test;

import io.blocko.coinstack.ECKey;
import io.blocko.coinstack.exception.CoinStackException;

public class TestGenerateKey {
	
	@Test
	public void testGenerateKey() throws CoinStackException {
		String privateKey = ECKey.createNewPrivateKey();
		String authorityAddress = ECKey.deriveAddress(privateKey);
		System.out.println("create privateKey: "+privateKey);
		System.out.println("derive authorityAddress: "+authorityAddress);
	}
	
	//@Test
	public void testDeriveKey() throws CoinStackException {
		String privateKey = ""; // SECRET
		String authorityAddress = ECKey.deriveAddress(privateKey);
		System.out.println("derive authorityAddress: "+authorityAddress);
	}
	
	//@Test
	public void testGenerateKeyForTestNet() throws CoinStackException {
		boolean isMainNet = false;
		String privateKey = ECKey.createNewPrivateKey(isMainNet);
		String authorityAddress = ECKey.deriveAddress(privateKey, isMainNet);
		System.out.println("[TestNet] create privateKey: "+privateKey);
		System.out.println("[TestNet] derive authorityAddress: "+authorityAddress);
	}
}
