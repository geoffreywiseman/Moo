package com.codiform.moo;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.codiform.moo.annotation.MapProperty;
import com.codiform.moo.configuration.Configuration;
import com.codiform.moo.curry.Translate;

public class MapPropertyTranslationTest {

	private Portfolio source = new Portfolio();

	@Before
	public void testSetUpStocks() {
		Calendar cal = Calendar.getInstance();

		cal.set( 2013, Calendar.DECEMBER, 4, 19, 59, 00 );
		source.add( new Security( "AAPL", "NASDAQ" ), new Position( 10000, 565.00f, cal.getTime() ) );

		cal.set( Calendar.MINUTE, 57 );
		source.add( new Security( "BB", "TSE" ), new Position( 3000, 38.94f, cal.getTime() ) );

		cal.set( 2013, Calendar.DECEMBER, 4, 16, 0, 0 );
		source.add( new Security( "ORCL", "NYSE" ), new Position( 5000, 35.07f, cal.getTime() ) );
	}

	@Test
	public void testTranslateWithoutAnnotationsCopiesMap() {
		PortfolioCopy copy = Translate.to( PortfolioCopy.class ).from( source );

		assertNotSame( source.getPositions(), copy.getPositions() );
		assertEquals( source.getPositions(), copy.getPositions() );
	}

	@Test
	public void testTranslateWithoutDefensiveCopyUsesSameMap() {
		Configuration configuration = new Configuration();
		configuration.setPerformingDefensiveCopies( false );

		PortfolioCopy copy = new Moo( configuration ).translate( source, PortfolioCopy.class );

		assertEquals( source.getPositions(), copy.getPositions() );
		assertSame( source.getPositions(), copy.getPositions() );
	}

	@Test
	public void testTranslatePortfolioToPositionBySymbolByTranslatingMapKeyToString() {
		PositionBySymbol pbs = Translate.to( PositionBySymbol.class ).from( source );

		assertThat( pbs.size(), equalTo( 3 ) );
		assertNotNull( pbs.getPosition( "AAPL" ) );
		assertThat( pbs.getPosition( "AAPL" ).getLastKnownValue(), is( closeTo( 5650000d, 1d ) ) );
		assertThat( pbs.getPosition( "BB" ).getLastKnownValue(), is( closeTo( 116820d, 1d ) ) );
		assertThat( pbs.getPosition( "ORCL" ).getLastKnownValue(), is( closeTo( 175350d, 1d ) ) );
	}

	@Test
	public void testTranslatePortfolioToMarketHoldingsByTranslatingMapKeyToStringWithMarketKeySource() {
		MarketHoldings mh = Translate.to( MarketHoldings.class ).from( source );

		assertThat( mh.size(), equalTo( 3 ) );
		assertNotNull( mh.getPosition( "NASDAQ" ) );
		assertThat( mh.getPosition( "NASDAQ" ).getLastKnownValue(), is( closeTo( 5650000d, 1d ) ) );
		assertThat( mh.getPosition( "TSE" ).getLastKnownValue(), is( closeTo( 116820d, 1d ) ) );
		assertThat( mh.getPosition( "NYSE" ).getLastKnownValue(), is( closeTo( 175350d, 1d ) ) );
	}


	public static class Portfolio {
		private Map<Security, Position> positions = new HashMap<Security, Position>();

		public void add( Security symbol, Position position ) {
			positions.put( symbol, position );
		}

		public Map<Security, Position> getPositions() {
			return positions;
		}
	}

	public static class PortfolioCopy {
		private Map<Security, Position> positions = new HashMap<Security, Position>();

		public Map<Security, Position> getPositions() {
			return positions;
		}
	}

	public static class MarketHoldings {
		@MapProperty( keyClass = String.class, keySource = "market" )
		private Map<String, Position> positions = new HashMap<String, Position>();

		public Position getPosition( String market ) {
			return positions.get( market );
		}

		public int size() {
			return positions.size();
		}
	}

	public static class PositionBySymbol {
		@MapProperty( keyClass = String.class )
		private Map<String, Position> positions = new HashMap<String, Position>();

		public Position getPosition( String symbol ) {
			return positions.get( symbol );
		}

		public int size() {
			return positions.size();
		}
	}

	public static class Position {
		private int shares;
		private float lastKnownPrice;
		private Date pricingDate;

		public Position( int shares, float lastKnownPrice, Date pricingDate ) {
			this.shares = shares;
			this.lastKnownPrice = lastKnownPrice;
			this.pricingDate = pricingDate;
		}

		public double getLastKnownValue() {
			return ( (double)shares ) * ( (double)lastKnownPrice );
		}
	}

	public static class Security {
		private String symbol;
		private String market;

		public Security( String symbol, String market ) {
			this.symbol = symbol;
			this.market = market;
		}

		public String toString() {
			return symbol;
		}

		public String getSymbol() {
			return symbol;
		}

		public String getMarket() {
			return market;
		}
	}

}
