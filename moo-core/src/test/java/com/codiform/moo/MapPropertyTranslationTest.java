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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.codiform.moo.configuration.Configuration;
import com.codiform.moo.curry.Translate;
import com.codiform.moo.domain.LastSecurityPrices;
import com.codiform.moo.domain.Portfolio;
import com.codiform.moo.domain.PortfolioCopy;
import com.codiform.moo.domain.PortfolioValue;
import com.codiform.moo.domain.Position;
import com.codiform.moo.domain.PositionBySymbol;
import com.codiform.moo.domain.PreviousMarketPrices;
import com.codiform.moo.domain.Security;
import com.codiform.moo.domain.SecurityPrices;

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

		cal.set( 2013, Calendar.DECEMBER, 4, 16, 0, 0 );
		source.add( securities.get( "ORCL" ), new Position( 5000, 35.07f, cal.getTime() ) );

		cal.set( 2013, Calendar.NOVEMBER, 21, 13, 00, 00 );
		Position oldPosition = new Position( 8000, 514.86f, cal.getTime() );
		
		cal.set( 2013, Calendar.DECEMBER, 4, 19, 59, 00 );
		source.add( securities.get( "AAPL" ), new Position( 10000, 565.00f, cal.getTime(), oldPosition ) );

		cal.set( Calendar.MINUTE, 57 );
		source.add( securities.get( "BB" ), new Position( 3000, 38.94f, cal.getTime() ) );

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
	
	@Test
	public void testTranslateMapType() {
		SymbolSortedPortfolio sorted = Translate.to( SymbolSortedPortfolio.class ).from( source );
		assertThat( sorted.size(), is(  equalTo( 3 ) ));
		
		Iterator<Security> iterator = sorted.iterator();
		
		Security security = iterator.next();
		assertThat( security, is( equalTo( securities.get( "AAPL" ) ) ) );
		
		Position position = sorted.getPosition( security );
		assertThat( position.getLastKnownValue(), is( closeTo( 5650000d, 1d ) ) );

		security = iterator.next();
		assertThat( security, is( equalTo( securities.get( "BB" ) ) ) );
		
		position = sorted.getPosition( security );
		assertThat( position.getLastKnownValue(), is( closeTo( 116820d, 1d ) ) );

		security = iterator.next();
		assertThat( security, is( equalTo( securities.get( "ORCL" ) ) ) );
		
		position = sorted.getPosition( security );
		assertThat( position.getLastKnownValue(), is( closeTo( 175350d, 1d ) ) );
	}
}
