package com.codiform.moo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.codiform.moo.configuration.Configuration;
import com.codiform.moo.curry.Translate;

public class MapPropertyTranslationTest {

	@Test
	public void testTranslateWithoutAnnotationsCopiesMap() {
		StockPrices domain = new StockPrices();
		domain.setPrice( "AAPL", 246.25 );
		domain.setPrice( "MSFT", 28.98 );
		domain.setPrice( "ORCL", 23.91 );

		StockPricesCopy dto = Translate.to( StockPricesCopy.class ).from( domain );

		assertNotSame( domain.getPrices(), dto.getPrices() );
		assertEquals( domain.getPrices(), dto.getPrices() );
	}

	@Test
	public void testTranslateWithoutDefensiveCopyUsesSameMap() {
		StockPrices domain = new StockPrices();
		domain.setPrice( "JAVA", 112.04 );
		domain.setPrice( "NODE", 35.12 );
		domain.setPrice( "RUBY", 38.53 );

		Configuration configuration = new Configuration();
		configuration.setPerformingDefensiveCopies( false );
		
		StockPricesCopy dto = new Moo( configuration ).translate( domain, StockPricesCopy.class );

		assertEquals( domain.getPrices(), dto.getPrices() );
		assertSame( domain.getPrices(), dto.getPrices() );
	}


	public static class StockPrices {
		private Map<String, Double> prices = new HashMap<String, Double>();

		public void setPrice(String symbol, double price) {
			prices.put( symbol, price );
		}

		public Map<String, Double> getPrices() {
			return prices;
		}

	}

	public static class StockPricesCopy {
		private Map<String, Double> prices = new HashMap<String, Double>();

		public Map<String, Double> getPrices() {
			return prices;
		}

	}
}
