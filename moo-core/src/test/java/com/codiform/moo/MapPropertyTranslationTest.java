package com.codiform.moo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.codiform.moo.curry.Translate;

public class MapPropertyTranslationTest {

	@Test
	public void testTranslateCopiesMap() {
		StockPrices domain = new StockPrices();
		domain.setPrice( "AAPL", 246.25 );
		domain.setPrice( "MSFT", 28.98 );
		domain.setPrice( "ORCL", 23.91 );

		StockPricesDto dto = Translate.to( StockPricesDto.class ).from( domain );

		assertNotSame( domain.getPrices(), dto.getPrices() );
		assertEquals( domain.getPrices(), dto.getPrices() );
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

	public static class StockPricesDto {
		private Map<String, Double> prices = new HashMap<String, Double>();

		public Map<String, Double> getPrices() {
			return prices;
		}

	}
}
