package com.ecom.mobile.accessories.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ecom.mobile.accessories.entites.OrderPlaced;
import com.ecom.mobile.accessories.entites.User;
import com.ecom.mobile.accessories.repository.OrderRepository;
import com.ecom.mobile.accessories.repository.UserRepository;
import com.ecom.mobile.accessories.services.EMailService;
import com.ecom.mobile.accessories.util.SecurityUtil;
import com.ecom.mobile.accessories.util.Util;

@Controller
@RequestMapping("/orders")
//@Secured(SecurityUtil.MANAGE_ORDERS)
//@Secured("hasRole('MANAGE_ORDERS')")
@PreAuthorize("hasAuthority('MANAGE_ORDERS')")
public class OrdersController {

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	EMailService eMailService;

	@Autowired
	UserRepository userRepository;

	@Value("${mob.email.from:test@gmail.com}")
	String fromEmail;

	@Value("${mob.email.pw:12345678}")
	String pa;

	@GetMapping
	@Secured(SecurityUtil.MANAGE_ORDERS)
	public String getAllOrders(Model model) {
		model.addAttribute("ordres", orderRepository.findAll());
		List<OrderPlaced> pending = new ArrayList<>();
		List<OrderPlaced> confirmed = new ArrayList<>();
		List<OrderPlaced> dispatched = new ArrayList<>();
		for (OrderPlaced o : orderRepository.findAll()) {
			switch (o.getoStatus()) {
			case PENDING:
				pending.add(o);
				break;
			case CONFIRMED:
				confirmed.add(o);
				break;
			case DISPATCHED:
				dispatched.add(o);
				break;
			default:
				break;
			}
		}
		model.addAttribute("pending", pending);
		model.addAttribute("confirmed", confirmed);
		model.addAttribute("dispatched", dispatched);
		return "/seller/orders";
	}

	@GetMapping("/approve/{id}")
	public String approveOrder(@PathVariable int id) {
		Optional<OrderPlaced> o = orderRepository.findById(id);
		if (o.isPresent()) {
			if (Util.isEmpty(o.get().getTxnid())) {
				return "redirect:/orders";
			}
			if (o.get().getoStatus().equals(OrderPlaced.orderStatus.PENDING)) {
				o.get().setoStatus(OrderPlaced.orderStatus.CONFIRMED);
				orderRepository.save(o.get());
				sendEmailOnConfirmation(o.get());
				return "redirect:/orders";
			}
		}
		return "redirect:/orders";
	}

	@GetMapping("/dispatched/{id}")
	public String dispatched(@PathVariable int id) {
		Optional<OrderPlaced> o = orderRepository.findById(id);
		if (o.isPresent()) {
			if (Util.isEmpty(o.get().getTxnid())) {
				return "redirect:/orders";
			}
			if (o.get().getoStatus().equals(OrderPlaced.orderStatus.CONFIRMED)) {
				o.get().setoStatus(OrderPlaced.orderStatus.DISPATCHED);
				orderRepository.save(o.get());
				sendEmailOnDispatch(o.get());
				return "redirect:/orders";
			}
		}
		return "redirect:/orders";
	}

	public void sendEmailOnConfirmation(OrderPlaced o) {
		Optional<User> user = userRepository.findById(o.getUserId());
		if (!user.isPresent()) {
			return;
		}
		String body = "Hi {username},\r\n" + "\r\n"
				+ "Your order with order id: {orderid} was confirmed. We will be dispacting your order shortly.\r\n"
				+ "\r\n" + "Thanks for supporting us.\r\n" + "\r\n" + "Happy Shopping!!\r\n" + "\r\n" + "Regards,\r\n"
				+ "Heed";

		body = body.replace("{username}", user.get().getName());
		body = body.replace("{orderid}", o.getOrderId());
		eMailService.sentEmail(fromEmail, user.get().getEmail(), pa, "Order Confirmed", body, null);
	}
	
	public void sendEmailOnDispatch(OrderPlaced o) {
		Optional<User> user = userRepository.findById(o.getUserId());
		if (!user.isPresent()) {
			return;
		}
		String body = "Hi {username},\r\n" + "\r\n"
				+ "Your order with order id: {orderid} was dispatched. You will be receiving your order within few days\r\n"
				+ "\r\n" + "Thanks for supporting us.\r\n" + "\r\n" + "Happy Shopping!!\r\n" + "\r\n" + "Regards,\r\n"
				+ "Heed";

		body = body.replace("{username}", user.get().getName());
		body = body.replace("{orderid}", o.getOrderId());
		eMailService.sentEmail(fromEmail, user.get().getEmail(), pa, "Order Confirmed", body, null);
	}
}
