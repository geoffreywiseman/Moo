package com.codiform.moo;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.codiform.moo.configuration.Configuration;
import com.codiform.moo.curry.Translate;
import com.codiform.moo.domain.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class MapPropertyTranslationTest {

	private Portfolio source = new Portfolio();
	private Map<String, Security> securities;

	@Before
	public void testSetUpStocks() {
		securities = new HashMap<>();
		securities.put( "AAPL", new Security( "AAPL", "NASDAQ" ) );
		securities.put( "RIMM", new Security( "RIMM", "TSE" ) );
		securities.put( "BB", new Security( "BB", "TSE", securities.get( "RIMM" ) ) );
		securities.put( "ORCL", new Security( "ORCL", "NYSE" ) );

		ZoneId toronto = ZoneId.of( "America/Toronto" );

		source.add( securities.get( "ORCL" ), new Position( 5000, 35.07f,
				ZonedDateTime.of( 2013, 12, 4, 16, 0, 0, 0, toronto ) ) );

		Position oldPosition = new Position( 8000, 514.86f,
				ZonedDateTime.of( 2013, 11, 21, 13, 0, 0, 0, toronto ) );

		source.add( securities.get( "AAPL" ), new Position( 10000, 565.00f,
				ZonedDateTime.of( 2013, 12, 4, 19, 59, 0, 0, toronto ),
				oldPosition ) );

		source.add( securities.get( "BB" ), new Position( 3000, 38.94f,
				ZonedDateTime.of( 2013, 12, 4, 19, 57, 0, 0, toronto ) ) );
	}

	@Test
	public void testTranslateWithoutAnnotationsCopiesMap() {
		PortfolioCopy copy = Translate.to( PortfolioCopy.class ).from( source );

		Assert.assertNotSame( source.getPositions(), copy.getPositions() );
		Assert.assertEquals( source.getPositions(), copy.getPositions() );
	}

	@Test
	public void testTranslateWithoutAnnotationsCopiesMapWithNullKey() {
		Position nullPosition = new Position( 0, 0.0f, null );
		source.add( null, nullPosition );

		PortfolioCopy copy = Translate.to( PortfolioCopy.class ).from( source );

		Assert.assertNotSame( source.getPositions(), copy.getPositions() );
		Assert.assertEquals( source.getPositions(), copy.getPositions() );

		Position nullCopy = copy.getPositions().get( null );
		Assert.assertNotNull( nullCopy );
		Assert.assertEquals( nullPosition, nullCopy );
	}

	@Test
	public void testTranslateWithoutDefensiveCopyUsesSameMap() {
		Configuration configuration = new Configuration();
		configuration.setPerformingDefensiveCopies( false );

		PortfolioCopy copy = new Moo( configuration ).translate( source, PortfolioCopy.class );

		Assert.assertEquals( source.getPositions(), copy.getPositions() );
		Assert.assertSame( source.getPositions(), copy.getPositions() );
	}

	@Test
	public void testTranslateMapKeyWithSourceClass() {
		PositionBySymbol pbs = Translate.to( PositionBySymbol.class ).from( source );

		assertThat( pbs.size(), equalTo( 3 ) );
		Assert.assertNotNull( pbs.getPosition( "AAPL" ) );
		assertThat( pbs.getPosition( "AAPL" ).getLastKnownValue(), is( closeTo( 5650000d, 1d ) ) );
		assertThat( pbs.getPosition( "BB" ).getLastKnownValue(), is( closeTo( 116820d, 1d ) ) );
		assertThat( pbs.getPosition( "ORCL" ).getLastKnownValue(), is( closeTo( 175350d, 1d ) ) );
	}

	@Test
	public void testTranslateMapKeyWithSourceKeyExpressionAndSourceClass() {
		MarketHoldings mh = Translate.to( MarketHoldings.class ).from( source );

		assertThat( mh.size(), equalTo( 3 ) );
		Assert.assertNotNull( mh.getPosition( "NASDAQ" ) );
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
		Assert.assertNotNull( rh.getPosition( rimm ) );
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
		assertThat( prices.getPrice( securities.get( "AAPL" ) ), hasToString( "$565.0 at 2013-12-04 19:59" ) );
		assertThat( prices.getPrice( securities.get( "BB" ) ), hasToString( "$38.94 at 2013-12-04 19:57" ) );
		assertThat( prices.getPrice( securities.get( "ORCL" ) ), hasToString( "$35.07 at 2013-12-04 16:00" ) );
	}

	@Test
	public void testTranslateMapValueWithValueSourceAndValueClass() {
		LastSecurityPrices prices = Translate.to( LastSecurityPrices.class ).from( source );
		assertThat( prices.size(), is( equalTo( 3 ) ) );
		assertThat( prices.getPrice( securities.get( "AAPL" ) ), hasToString( "$514.86 at 2013-11-21 13:00" ) );
		assertThat( prices.getPrice( securities.get( "BB" ) ), is( nullValue() ) );
		assertThat( prices.getPrice( securities.get( "ORACL" ) ), is( nullValue() ) );
	}

	@Test
	public void testTranslateMapKeyAndValueWithSourceAndClass() {
		PreviousMarketPrices prices = Translate.to( PreviousMarketPrices.class ).from( source );
		assertThat( prices.size(), is( equalTo( 3 ) ) );
		assertThat( prices.getPrice( "NASDAQ" ), hasToString( "$514.86 at 2013-11-21 13:00" ) );
		assertThat( prices.getPrice( "TSE" ), is( nullValue() ) );
		assertThat( prices.getPrice( "NYSE" ), is( nullValue() ) );
	}

	@Test
	public void testTranslateMapType() {
		SymbolSortedPortfolio sorted = Translate.to( SymbolSortedPortfolio.class ).from( source );
		assertThat( sorted.size(), is( equalTo( 3 ) ) );

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
