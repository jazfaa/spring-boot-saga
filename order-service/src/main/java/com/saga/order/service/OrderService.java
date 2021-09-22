package com.saga.order.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.saga.order.entity.Order;
import com.saga.order.kafka.source.OrderNotProcessedEventSource;
import com.saga.order.kafka.source.OrderPlacedEventSource;
import com.saga.order.model.PlaceOrderRequest;
import com.saga.order.model.PlaceOrderResponse;
import com.saga.order.repository.OrderRepository;

@Service
public class OrderService {

	private static final Logger logger = LoggerFactory
			.getLogger(OrderService.class);
	
	@Autowired
	private OrderPlacedEventSource orderPlacedEventSource;
	
	@Autowired
	private OrderNotProcessedEventSource OoderNotProcessedEventSource;

	@Autowired
	OrderRepository orderRepository;

	public PlaceOrderResponse createOrder(PlaceOrderRequest orderRequest) {
		Order order = new Order();

		// order service should call inventory service to get item name by item id
		order.setItemName(orderRequest.getItemName());

		// order service should call customer service to get customer name by id
		order.setCustomerName(orderRequest.getCustomerNm());

//		Random random = new Random();
//		Long orderId = Math.abs(random.nextLong());
//
//		order.setOrderId(orderId);

		// order service should assign some valid order id
		// order.setId(234L);

		// order service should save order */

		orderRepository.save(order);

		PlaceOrderResponse response = new PlaceOrderResponse();
		response.setMessage("order placed successfully");
		response.setOrderId(order.getOrderId());

		logger.info("going to place orderPlacedEvent for order :" + order.getOrderId());
		orderPlacedEventSource.publishOrderEvent(order.getOrderId());

		return response;
	}
	
	public void compensateOrder(Long orderId) {
		/*delete order record for given order id from database*/
		
		/*publish OoderNotProcessedEvent*/ 
		
		orderRepository.deleteById(orderId);
		OoderNotProcessedEventSource.publishOrderNotProcessedEvent(orderId);
	}


}
