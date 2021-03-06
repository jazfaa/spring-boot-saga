package com.saga.inventory.kafka.source;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;

import com.saga.inventory.kafka.channel.InventoryChannel;
import com.saga.inventory.kafka.message.ItemEvent;



@Component
public class ItemOutOfStockEventSource {

	@Autowired
	private InventoryChannel inventoryChannel;

	public void publishItemOutOfStockEvent(Long orderId,Long itemId) {

		ItemEvent message = new ItemEvent();
		message.setItemId(itemId);
		message.setOrderId(orderId);
		message.setAction(ItemEvent.Action.ITEMOUTOFSTOCK);
		message.setItemId(itemId);
		
		MessageChannel messageChannel = inventoryChannel.outboundInventory();
		messageChannel.send(MessageBuilder.withPayload(message)
				.setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
				.build());

	}

}
