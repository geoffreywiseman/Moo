package com.codiform.moo.session;

import com.codiform.moo.domain.OrdinalDto;
import com.codiform.moo.domain.TestFactory;
import com.codiform.moo.translator.ObjectTranslator;
import com.codiform.moo.translator.TranslatorFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@RunWith( MockitoJUnitRunner.class )
public class TranslationSessionTest {

	@Mock
	private TranslatorFactory translatorFactory;

	@Mock
	private TranslationCache cache;
	
	@Mock
	private TestFactory factory;

	private TestableTranslationSession session;

	@Before
	public void setUpSession() {
		session = new TestableTranslationSession( translatorFactory );
		session.setTranslationCache( cache );
		session.cacheTranslationTargetFactory( TestFactory.class, factory );
	}

	@Test
	public void testGetTranslationChecksCacheThenTranslates() {
		OrdinalDto translation = new OrdinalDto( 1, "First" );
		ObjectTranslator<OrdinalDto> translator = mockTranslator( OrdinalDto.class );
		Mockito.when( translatorFactory.getTranslator( OrdinalDto.class ) ).thenReturn( translator );
		Mockito.when( factory.getTranslationTargetInstance( "One", OrdinalDto.class ) ).thenReturn( translation );
		
		session.getTranslation( "One", TestFactory.class, OrdinalDto.class );
		
		Mockito.verify( cache ).getTranslation( "One", OrdinalDto.class );
		Mockito.verify( factory ).getTranslationTargetInstance( "One", OrdinalDto.class );
	}

	@Test
	public void testStoresMissingTranslationInCache() {
		ObjectTranslator<OrdinalDto> translator = mockTranslator( OrdinalDto.class );
		Mockito.when( translatorFactory.getTranslator( OrdinalDto.class ) ).thenReturn( translator );

		session.getTranslation( "One", OrdinalDto.class );
		
		Mockito.verify( cache ).getTranslation( "One", OrdinalDto.class );
		Mockito.verify( cache ).putTranslation( eq( "One" ), any( OrdinalDto.class ) );
	}

	@Test
	public void testGetTranslationReturnsTranslatedValue() {
		Integer translation = 1;
		ObjectTranslator<Integer> translator = mockTranslator( Integer.class );
		Mockito.when( translatorFactory.getTranslator( Integer.class ) ).thenReturn( translator );
		Mockito.when( factory.getTranslationTargetInstance( "One", Integer.class ) ).thenReturn(translation);

		Integer result = session.getTranslation( "One", TestFactory.class, Integer.class );
		Assert.assertSame( translation, result );
	}

	@SuppressWarnings( "unchecked" )
	private <T> ObjectTranslator<T> mockTranslator( Class<T> translationType ) {
		return Mockito.mock( ObjectTranslator.class );
	}

	@Test
	public void testGetTranslationDoesntTranslateIfFoundInCache() {
		Mockito.when( cache.getTranslation( "One", Integer.class ) ).thenReturn( 1 );

		session.getTranslation( "One", Integer.class );
		Mockito.verify( cache ).getTranslation( "One", Integer.class );
		Mockito.verifyZeroInteractions( translatorFactory );
	}

	@Test
	public void testGetTranslationReturnsCachedValue() {
		Integer translation = 1;
		Mockito.when( cache.getTranslation( "One", Integer.class ) ).thenReturn( translation );

		Integer result = session.getTranslation( "One", Integer.class );
		Assert.assertSame( translation, result );
	}
}
