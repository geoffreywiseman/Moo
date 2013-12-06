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
		Security security = new Security( "BB", "TSE", new Security( "RIMM", "TSE" ) );
		source.add( security, new Position( 3000, 38.94f, cal.getTime() ) );

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
	
	@Test
	public void testTranslatePortfolioToRenamedHoldingsByTranslatingMapKeyToPreviousSecurity() {
		// given
		Security bb = new Security( "BB", "TSE" );
		Security rimm = new Security( "RIMM", "TSE" );

		// when
		RenamedHoldings rh = Translate.to( RenamedHoldings.class ).from( source );

		// then
		assertThat( rh.size(), equalTo( 1 ) );
		assertNotNull( rh.getPosition( rimm ) );
		assertThat( rh.getPosition( rimm ), is( equalTo( source.getPosition( bb ) ) ) );
	}

	public static class Portfolio {
		private Map<Security, Position> positions = new HashMap<Security, Position>();

		public void add( Security symbol, Position position ) {
			positions.put( symbol, position );
		}

		public Position getPosition( Security security ) {
			return positions.get( security );
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

	public static class RenamedHoldings {
		@MapProperty( keySource = "previousSecurity", nullKeys=false )
		private Map<Security, Position> positions = new HashMap<Security, Position>();

		public Position getPosition( Security security ) {
			return positions.get( security );
		}

		public int size() {
			return positions.size();
		}
	}

	public static class PreviousPortfolio {
		@MapProperty
		private Map<Security, Position> positions = new HashMap<Security, Position>();

		public Position getPosition( Security security ) {
			return positions.get( security );
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
		
		private Position previousPosition;

		public Position( int shares, float lastKnownPrice, Date pricingDate ) {
			this.shares = shares;
			this.lastKnownPrice = lastKnownPrice;
			this.pricingDate = pricingDate;
		}
		
		public Date getPricingDate() {
			return pricingDate;
		}

		public Position( int shares, float lastKnownPrice, Date pricingDate, Position previous ) {
			this( shares, lastKnownPrice, pricingDate );
			this.previousPosition = previous;
		}

		public double getLastKnownValue() {
			return ( (double)shares ) * ( (double)lastKnownPrice );
		}
		
		public Position getPreviousPosition() {
			return previousPosition;
		}
	}

	public static class Security {
		private String symbol;
		private String market;
		private Security previousSecurity;

		public Security( String symbol, String market ) {
			this.symbol = symbol;
			this.market = market;
		}
		
		public Security( String symbol, String market, Security previous ) {
			this( symbol, market );
			this.previousSecurity = previous;
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
		
		public Security getPreviousSecurity() {
			return previousSecurity;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ( ( market == null ) ? 0 : market.hashCode() );
			result = prime * result + ( ( symbol == null ) ? 0 : symbol.hashCode() );
			return result;
		}

		@Override
		public boolean equals( Object obj ) {
			if ( this == obj )
				return true;
			if ( obj == null )
				return false;
			if ( getClass() != obj.getClass() )
				return false;
			Security other = (Security)obj;
			if ( market == null ) {
				if ( other.market != null )
					return false;
			} else if ( !market.equals( other.market ) )
				return false;
			if ( symbol == null ) {
				if ( other.symbol != null )
					return false;
			} else if ( !symbol.equals( other.symbol ) )
				return false;
			return true;
		}
	}

}
