package com.codiform.moo;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.codiform.moo.configuration.Configuration;
import com.codiform.moo.session.TranslationSession;
import com.codiform.moo.source.TranslationSource;

@RunWith(MockitoJUnitRunner.class)
public class MooTest {

	@Mock
	private TranslationSession session;

	@Mock
	private Configuration configuration;

	private Moo moo = new TestMoo(configuration);

	private Integer source = new Integer(24);

	@Test
	public void testMooTranslateCanCreateCurriedTranslate() {
		Assert.assertNotNull(moo.translate(String.class));
	}

	@Test
	public void testMooTranslateTranslatesInNewSession() {
		moo.translate(source, String.class);
		Mockito.verify(session).getTranslation(source, String.class);
	}

	@Test
	public void testMooTranslateEachListTranslatesEachInNewSession() {
		List<Integer> sources = Arrays.asList(1, 2, 3);
		moo.translateEach(sources, String.class);

		Mockito.verify(session).getEachTranslation(sources, String.class);
	}

	@Test
	public void testMooTranslateEachSetTranslatesEachInNewSession() {
		Set<Integer> sources = new HashSet<Integer>( Arrays.asList(1, 2, 3) );
		moo.translateEach(sources, String.class);

		Mockito.verify(session).getEachTranslation(sources, String.class);
	}

	@Test
	public void testMooTranslateEachCollectionTranslatesEachInNewSession() {
		Collection<Integer> sources = new HashSet<Integer>( Arrays.asList(1, 2, 3) );
		moo.translateEach(sources, String.class);

		Mockito.verify(session).getEachTranslation(sources, String.class);
	}

	private class TestMoo extends Moo {

		public TestMoo(Configuration configuration) {
			super(configuration);
		}

		@Override
		protected TranslationSource newSession() {
			return session;
		}

	}
}
