package io.blocko.coinstack.sample;

import static org.junit.Assert.*;

import org.junit.Test;

import io.blocko.coinstack.ECKey;
import io.blocko.coinstack.exception.CoinStackException;

public class TestGenerateKey {
	
	@Test
	public void testGenerateKey() throws CoinStackException {
		System.out.println("## testGenerateKey");
		String privateKey = ECKey.createNewPrivateKey();
		String authorityAddress = ECKey.deriveAddress(privateKey);
		System.out.println("create privateKey: "+privateKey);
		System.out.println("derive authorityAddress: "+authorityAddress);
		System.out.println("");
	}
	
	@Test
	public void testDeriveKey() throws CoinStackException {
		System.out.println("## testGenerateKey");
		String privateKey = "L4JfD5753EWSj8pUCNyxGAZ2WBoRA94d1s2JfZ2ma6vAuUXmbfdR";
		String authorityAddress = ECKey.deriveAddress(privateKey);
		System.out.println("derive authorityAddress: "+authorityAddress);
		assertEquals("1BBA9M2mtbf974RJyDWM7FTzB8e7y84kTq", authorityAddress);
		System.out.println("");
	}
}
