package com.ecom.mobile.accessories.controller;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecom.mobile.accessories.entites.Feedback;
import com.ecom.mobile.accessories.entites.OrderPlaced;
import com.ecom.mobile.accessories.entites.Products;
import com.ecom.mobile.accessories.entites.Role;
import com.ecom.mobile.accessories.entites.User;
import com.ecom.mobile.accessories.entites.UserAddress;
import com.ecom.mobile.accessories.repository.AddressRepository;
import com.ecom.mobile.accessories.repository.FeedbackRepository;
import com.ecom.mobile.accessories.repository.OrderRepository;
import com.ecom.mobile.accessories.repository.ProductsRepository;
import com.ecom.mobile.accessories.repository.RoleRepository;
import com.ecom.mobile.accessories.repository.UserRepository;
import com.ecom.mobile.accessories.services.EMailService;
import com.ecom.mobile.accessories.util.SecurityUtil;
import com.ecom.mobile.accessories.util.Util;
import com.ecom.mobile.accessories.vo.DataVo;
import com.ecom.mobile.accessories.vo.MandateVoucherReq;
import com.ecom.mobile.accessories.vo.OrderVo;
import com.ecom.mobile.accessories.vo.PageContentVo;
import com.ecom.mobile.accessories.vo.RegisterVo;
import com.ecom.mobile.accessories.vo.ResultVo;
import com.ecom.mobile.accessories.vo.SearchData;
import com.google.gson.Gson;

@Controller
public class HomeController {

	public static final Map<String, RegisterVo> regRecord = new HashMap<>();

	@Autowired
	EMailService eMailService;

	@Value("${mob.email.from:test@gmail.com}")
	String fromEmail;

	@Value("${mob.email.pw:12345678}")
	String pa;

	@Value("${mob.email.sub:mobilecover email verification}")
	String subject;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder paEncoder;

	@Autowired
	ProductsRepository productsRepository;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	AddressRepository addressRepository;

	@Autowired
	FeedbackRepository feedbackRepository;

	public static final Map<String, String> sliders = new HashMap<>();

	@GetMapping("/Home")
	public String home(Model model) {
		model.addAttribute("sliders", sliders);
		model.addAttribute("cat", productsRepository.getDistinctCategory());
		model.addAttribute("brand", productsRepository.getDistinctBrand());
		return "index";
	}

//	@GetMapping("/MyAccount")
//	public String myAccount() {
//		return "myAccount";
//	}

	@GetMapping("/index/updateSlider")
	public @ResponseBody String updateSlider(@RequestParam String imgData) {
		String[] imgs = imgData.split(",");
		if (imgs.length > 0) {
			sliders.clear();
		}
		for (String s : imgs) {
			sliders.put(s, s);
		}
		return "updated";
	}

	@GetMapping("/MyOrders")
	public String myOrders(Model model) {
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		User u = userRepository.findByNameIgnoreCase(name);
		if (u != null) {
			model.addAttribute("list", orderRepository.findByUserId(u.getId()));
		} else {
			model.addAttribute("list", new ArrayList<>());
		}
		return "myOrders";
	}

	@GetMapping("/index/Feedback")
	public String feedBack(Model model) {
		model.addAttribute("feedback", new Feedback());
		return "feedBack";
	}

	@PostMapping(value = "/index/Feedback")
	public String createBank(@ModelAttribute("feedback") Feedback feedback, RedirectAttributes redirectAttributes) {
		try {
			feedbackRepository.save(feedback);
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute(SecurityUtil.STATUS_ERROR, SecurityUtil.STATUS_REMARK_FAILED);
		}
		return "redirect:/Home";
	}

	@GetMapping("/MyCart")
	public String myCart() {
		return "myCart";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/getUserAddress")
	public @ResponseBody List<UserAddress> getAddress() {

		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepository.findByNameIgnoreCase(name);
		if (user != null) {
			return user.getAddress();
		}
		return new ArrayList<>();
	}

	public int gen() {
		Random r = new Random(System.currentTimeMillis());
		return 10000 + r.nextInt(20000) + new SecureRandom().nextInt(100);
	}

	@PostMapping("/register")
	public String register(RegisterVo req, Model model) {

		RegisterVo val = regRecord.get(req.getEmail());
		if (val != null) {
			regRecord.remove(req.getEmail());
		}
		int code = gen();
		String body = "Hi,\r\n" + "\r\n"
				+ "We noticed that you recently attempted to use your Email account to register.\r\n" + "\r\n"
				+ "Please verify by pasting the code provided below:\r\n" + "\r\n" + code;
		req.setVcode(code + "");
		regRecord.put(req.getEmail(), req);
		eMailService.sentEmail(fromEmail, req.getEmail(), pa, subject, body, null);

		model.addAttribute("email", req.getEmail());

		return "validate";
	}

	public static Date getDateByString(String str, String format) {
		try {
			return (new SimpleDateFormat(format)).parse(str);
		} catch (Exception e) {
			return null;
		}
	}

	@PostMapping("/validate")
	public String validate(RegisterVo req, Model model, RedirectAttributes redirectAttributes) {
		RegisterVo val = regRecord.get(req != null ? req.getEmail() : null);
		if (val == null || req == null || req.getVcode() == null) {
			redirectAttributes.addFlashAttribute("ERROR", "Something went wrong please try again.");
			System.out.println("Something went wrong please try again.");
			return "redirect:/login";
		}

		if (req.getVcode().equals(val.getVcode())) {
			redirectAttributes.addFlashAttribute("INFO", "You have registered successfully.");
			User user = userRepository.findByEmail(req.getEmail());
			if (user != null) {
				redirectAttributes.addFlashAttribute("ERROR", "Something went wrong please try again.");
				return "redirect:/login";
			}
			user = new User();
			user.setName(val.getFullName());
			user.setEmail(req.getEmail());
			user.setPassword(paEncoder.encode(val.getPassword()));
			List<Role> roles = new ArrayList<>();
			roles.add(roleRepository.findByName(SecurityUtil.CUSTOMER));
			user.setRoles(roles);
			userRepository.save(user);
			return "redirect:/login";
		} else {
			redirectAttributes.addFlashAttribute("INFO", "Invalid verification code");
			model.addAttribute("email", req.getEmail());
			return "validate";
		}
	}

	@GetMapping(value = "/addToCart/{pcode}")
	public String addToCart(@PathVariable String pcode, Model model) {
		Products prod = productsRepository.findByCode(pcode);
		if (prod != null) {
			String name = SecurityContextHolder.getContext().getAuthentication().getName();
			User user = userRepository.findByNameIgnoreCase(name);
			List<Products> p = user.getCart();
			boolean idDup = false;
			for (Products products : p) {
				if (products.getCode().equals(pcode)) {
					idDup = true;
				}
			}
			if (!idDup) {
				p.add(prod);
				user.setCart(p);
				userRepository.save(user);
			}
		}
		System.out.println(pcode);
		return "redirect:/Home";
	}

	@GetMapping(value = "/getCartData")
	public @ResponseBody List<Products> getCartData() {
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepository.findByNameIgnoreCase(name);
		if (user != null) {
			return user.getCart();
		}
		return new ArrayList<>();
	}

	@GetMapping(value = "/index/Products/{pageNumber}")
	public @ResponseBody PageContentVo getProducts(@PathVariable Integer pageNumber, Model model) {
		Page<Products> page = productsRepository.findAll(PageRequest.of(pageNumber, 20));
		return new PageContentVo(page);
	}

	@GetMapping(value = "/index/contactus")
	public String contactus() {
		return "contactus";
	}

	@PostMapping(value = "/index/Products")
	public @ResponseBody PageContentVo getProducts(@RequestBody SearchData data) {
		PageContentVo vo = null;
		if (data.search != null) {
			Pageable pageable = PageRequest.of(data.page, 20);

			List<Products> list = new ArrayList<>();
			long count = 0;
			if (!Util.isEmpty(data.category) && !Util.isEmpty(data.brand)) {
				list.addAll(productsRepository.findAllByCategoryAndBrand(data.category, data.brand, pageable));
				count += productsRepository.countByCategoryAndBrand(data.category, data.brand);
			} else if (!Util.isEmpty(data.category)) {
				list.addAll(productsRepository.findAllByCategory(data.category, pageable));
				count += productsRepository.countByCategory(data.category);
			} else if (!Util.isEmpty(data.brand)) {
				list.addAll(productsRepository.findAllByBrand(data.brand, pageable));
				count += productsRepository.countByBrand(data.brand);
			} else if (!Util.isEmpty(data.search)) {
				String[] ss = data.search.split(" ");
				for (String s : ss) {
					list.addAll(productsRepository.findAllByNameLike("%" + s + "%", pageable));
					count += productsRepository.countByNameLike("%" + s + "%");
				}
			}

			System.out.println("list size::" + list.size());
			System.out.println(count);
			vo = new PageContentVo(list, count, data.page, 20);
		} else {
			Page<Products> page = productsRepository.findAll(PageRequest.of(data.page, 20));
			vo = new PageContentVo(page);
		}
		Gson g = new Gson();
		System.out.println(g.toJson(vo));
		return vo;
	}

	@PostMapping(value = "/placeOrder")
	public @ResponseBody ResultVo placeOrder(@RequestBody OrderVo data) {
		Gson g = new Gson();
		System.out.println(g.toJson(data));

		ResultVo vo = new ResultVo();

		OrderPlaced o = new OrderPlaced();
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		User u = userRepository.findByNameIgnoreCase(name);
		if (u != null) {
			o.setUserId(u.getId());
			o.setName(u.getName());
			getOrderData(data, o);
			o.setOrderId(Util.getOrderID());
			o.setMobile(data.address.mobile);
			o.setAddress(getAddress(data.address));
			o.setTxnid(data.txnid);
			orderRepository.save(o);

			if (u.getAddress().isEmpty()) {
				addressRepository.save(data.address);
				u.getAddress().add(data.address);
			}

			u.setCart(new ArrayList<>());
			userRepository.save(u);
		}

		vo.setStatus(true);
		vo.setMsg("Order palced successfully");

		return vo;
	}

	private void getOrderData(OrderVo data, OrderPlaced o) {
		long total = 0;
		int items = 0;
		StringBuilder orderData = new StringBuilder();
		for (DataVo d : data.data) {
			Products p = productsRepository.findByCode(d.pcode);
			if (p != null) {
				long amt = d.quantity * p.getCost();
				total += amt;
				items += d.quantity;
				if (!Util.isEmpty(orderData.toString())) {
					orderData.append(",");
				}
				orderData.append(d.pcode + "|" + d.quantity);
			}
		}
		o.setOrderData(orderData.toString());
		o.setItems(items);
		o.setTotal(total);
	}

	private String getAddress(UserAddress address) {
		StringBuilder sb = new StringBuilder();
		sb.append(address.fname);
		sb.append(", ");
		sb.append(address.hno);
		sb.append(", ");
		sb.append(address.area);
		sb.append(", ");
		sb.append(address.town);
		sb.append(" ");
		sb.append(address.pincode);
		sb.append(" ");
		sb.append(address.state);
		sb.append(", ");
		sb.append("Landmark: ");
		sb.append(address.lmark);

		address.setPlainAddress(sb.toString());

		return sb.toString();
	}

	@GetMapping(value = "/remove/{pcode}")
	public String placeOrder(@PathVariable String pcode) {
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepository.findByNameIgnoreCase(name);
		if (user != null) {
			List<Products> newProds = new ArrayList<>();
			List<Products> p = user.getCart();
			boolean isSaveReq = false;
			for (Products products : p) {
				if (!products.getCode().equals(pcode)) {
					newProds.add(products);
				} else {
					isSaveReq = true;
				}
			}
			if (isSaveReq) {
				user.setCart(newProds);
				userRepository.save(user);
			}
		}
		return "redirect:/MyCart";
	}

	public static void main(String[] args) {
		double base = 30000;
		int intrest = 6;
		int months = 9;
		for (int i = 1; i <= months; i++) {
			double val = base * intrest / 100;
			base += val;
			System.out.println(base);
		}
		int obtained = 300, total = 600;
		System.out.println("Percentage:" + (obtained * 100 / total));

	}
	
	@PostMapping(value = "/index/test")
	@ResponseBody
	public String test(@RequestBody MandateVoucherReq req) {
		System.out.println(req.batchId);
		return "done";
	}
}
