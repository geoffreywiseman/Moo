package com.codiform.moo;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.codiform.moo.annotation.MapProperty;
import com.codiform.moo.annotation.Property;
import com.codiform.moo.configuration.Configuration;
import com.codiform.moo.curry.Translate;

public class MapPropertyTranslationTest {

	private Portfolio source = new Portfolio();
	private Map<String, Security> securities;

	@Before
	public void testSetUpStocks() {
		securities = new HashMap<String, Security>();
		securities.put( "AAPL", new Security( "AAPL", "NASDAQ" ) );
		securities.put( "RIMM", new Security( "RIMM", "TSE" ) );
		securities.put( "BB", new Security( "BB", "TSE", securities.get( "RIMM" ) ) );
		securities.put( "ORCL", new Security( "ORCL", "NYSE" ) );

		Calendar cal = Calendar.getInstance();

		cal.set( 2013, Calendar.NOVEMBER, 21, 13, 00, 00 );
		Position oldPosition = new Position( 8000, 514.86f, cal.getTime() );
		
		cal.set( 2013, Calendar.DECEMBER, 4, 19, 59, 00 );
		source.add( securities.get( "AAPL" ), new Position( 10000, 565.00f, cal.getTime(), oldPosition ) );

		cal.set( Calendar.MINUTE, 57 );
		source.add( securities.get( "BB" ), new Position( 3000, 38.94f, cal.getTime() ) );

		cal.set( 2013, Calendar.DECEMBER, 4, 16, 0, 0 );
		source.add( securities.get( "ORCL" ), new Position( 5000, 35.07f, cal.getTime() ) );
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
	public void testTranslateMapKeyWithSourceClass() {
		PositionBySymbol pbs = Translate.to( PositionBySymbol.class ).from( source );

		assertThat( pbs.size(), equalTo( 3 ) );
		assertNotNull( pbs.getPosition( "AAPL" ) );
		assertThat( pbs.getPosition( "AAPL" ).getLastKnownValue(), is( closeTo( 5650000d, 1d ) ) );
		assertThat( pbs.getPosition( "BB" ).getLastKnownValue(), is( closeTo( 116820d, 1d ) ) );
		assertThat( pbs.getPosition( "ORCL" ).getLastKnownValue(), is( closeTo( 175350d, 1d ) ) );
	}

	@Test
	public void testTranslateMapKeyWithSourceKeyExpressionAndSourceClass() {
		MarketHoldings mh = Translate.to( MarketHoldings.class ).from( source );

		assertThat( mh.size(), equalTo( 3 ) );
		assertNotNull( mh.getPosition( "NASDAQ" ) );
		assertThat( mh.getPosition( "NASDAQ" ).getLastKnownValue(), is( closeTo( 5650000d, 1d ) ) );
		assertThat( mh.getPosition( "TSE" ).getLastKnownValue(), is( closeTo( 116820d, 1d ) ) );
		assertThat( mh.getPosition( "NYSE" ).getLastKnownValue(), is( closeTo( 175350d, 1d ) ) );
	}

	@Test
	public void testTranslateMapKeyWithSourceKeyExpression() {
		// given
		Security bb = securities.get( "BB" );
		Security rimm = bb.getPreviousSecurity();

		// when
		RenamedHoldings rh = Translate.to( RenamedHoldings.class ).from( source );

		// then
		assertThat( rh.size(), equalTo( 1 ) );
		assertNotNull( rh.getPosition( rimm ) );
		assertThat( rh.getPosition( rimm ), is( equalTo( source.getPosition( bb ) ) ) );
	}

	@Test
	public void testTranslateMapValueWithSourceValueExpression() {
		// when
		PortfolioValue pv = Translate.to( PortfolioValue.class ).from( source );

		// then
		assertThat( pv.size(), is( equalTo( 3 ) ) );
		assertThat( pv.getValue( securities.get( "BB" ) ), is( closeTo( 116820d, 1d ) ) );
	}

	@Test
	public void testTranslateMapValueWithValueClass() {
		SecurityPrices prices = Translate.to( SecurityPrices.class ).from( source );
		assertThat( prices.size(), is( equalTo( 3 ) ) );
		assertThat( prices.getPrice( securities.get( "AAPL" ) ), hasToString( "$565.0 at 2013-Dec-04 19:59" ) );
		assertThat( prices.getPrice( securities.get( "BB" ) ), hasToString( "$38.94 at 2013-Dec-04 19:57" ) );
		assertThat( prices.getPrice( securities.get( "ORCL" ) ), hasToString( "$35.07 at 2013-Dec-04 16:00" ) );
	}

	@Test
	public void testTranslateMapValueWithValueSourceAndValueClass() {
		LastSecurityPrices prices = Translate.to( LastSecurityPrices.class ).from( source );
		assertThat( prices.size(), is( equalTo( 3 ) ) );
		assertThat( prices.getPrice( securities.get( "AAPL" ) ), hasToString( "$514.86 at 2013-Nov-21 13:00" ) );
		assertThat( prices.getPrice( securities.get( "BB" ) ), is( nullValue() ) );
		assertThat( prices.getPrice( securities.get( "ORACL" ) ), is( nullValue() ) );
	}

	@Test
	public void testTranslateMapKeyAndValueWithSourceAndClass() {
		PreviousMarketPrices prices = Translate.to( PreviousMarketPrices.class ).from( source );
		assertThat( prices.size(), is( equalTo( 3 ) ) );
		assertThat( prices.getPrice( "NASDAQ" ), hasToString( "$514.86 at 2013-Nov-21 13:00" ) );
		assertThat( prices.getPrice( "TSE" ), is( nullValue() ) );
		assertThat( prices.getPrice( "NYSE" ), is( nullValue() ) );
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
		@MapProperty( keySource = "previousSecurity", nullKeys = false )
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

	public static class PortfolioValue {
		@MapProperty( valueSource = "lastKnownValue" )
		private Map<Security, Double> positions = new HashMap<Security, Double>();

		public Double getValue( Security security ) {
			return positions.get( security );
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

	private static class SecurityPrices {
		@MapProperty( source = "positions", valueClass = SecurityPrice.class )
		private Map<Security, SecurityPrice> prices = new HashMap<Security, SecurityPrice>();

		public int size() {
			return prices.size();
		}

		public SecurityPrice getPrice( Security security ) {
			return prices.get( security );
		}
	}

	private static class LastSecurityPrices {
		@MapProperty( source = "positions", valueSource = "previousPosition", valueClass = SecurityPrice.class )
		private Map<Security, SecurityPrice> prices = new HashMap<Security, SecurityPrice>();

		public int size() {
			return prices.size();
		}

		public SecurityPrice getPrice( Security security ) {
			return prices.get( security );
		}
	}

	private static class SecurityPrice {
		private static SimpleDateFormat format = new SimpleDateFormat( "YYYY-MMM-dd HH:mm" );

		@Property( source = "lastKnownPrice" )
		private float price;

		@Property( source = "pricingDate" )
		private Date dateOfPrice;

		public String toString() {
			return "$" + price + " at " + format.format( dateOfPrice );
		}
	}

	public static class PreviousMarketPrices {
		@MapProperty( source="positions", keyClass = String.class, keySource = "market", valueClass=SecurityPrice.class, valueSource="previousPosition" )
		private Map<String, SecurityPrice> prices = new HashMap<String, SecurityPrice>();

		public int size() {
			return prices.size();
		}

		public SecurityPrice getPrice( String market ) {
			return prices.get( market );
		}
	}
}
