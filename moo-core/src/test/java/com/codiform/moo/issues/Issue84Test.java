package com.codiform.moo.issues;

import java.util.concurrent.atomic.AtomicInteger;

import com.codiform.moo.annotation.Access;
import com.codiform.moo.annotation.AccessMode;
import com.codiform.moo.curry.Translate;
import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("unused")
public class Issue84Test {

	@Test
	public void testTranslation() {
		CreateOrderCommand source = new CreateOrderCommand.Builder(new OrderId(), "First Order" ).withAdvertisementText( "Advertisement Text" ).build();
		CreateOrderCommand copy = Translate.to(CreateOrderCommand.class).from(source);
		Assert.assertNotSame( source, copy );
		Assert.assertEquals( source.advertisementText, copy.advertisementText );
	}

	@Access(AccessMode.FIELD)
	public static class CreateOrderCommand {
		private OrderId orderId;
		private String name;
		private String advertisementText;

		// For Moo
		private CreateOrderCommand() {
		}
		
		// For Builder
		private CreateOrderCommand( OrderId orderId, String name, String advertisementText ) {
			this.orderId = orderId;
			this.name = name;
			this.advertisementText = advertisementText;
		}

		public static class Builder {
			private OrderId orderId;
			private String name;
			private String advertisementText;

			public Builder(OrderId orderId, String name) {
				this.orderId = orderId;
				this.name = name;
			}

			public Builder withAdvertisementText(String advertisementText) {
				this.advertisementText = advertisementText;
				return this;
			}

			public CreateOrderCommand build() {
				return new CreateOrderCommand(orderId, name, advertisementText);
			}
		}

	}

	@Access(AccessMode.FIELD)
	public static class OrderId {
		private static AtomicInteger sequence = new AtomicInteger();
		private int id;

		public OrderId() {
			id = sequence.incrementAndGet();
		}
	}
}
