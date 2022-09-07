package com.ecom.mobile.accessories.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecom.mobile.accessories.entites.Products;
import com.ecom.mobile.accessories.repository.ProductsRepository;
import com.ecom.mobile.accessories.util.SecurityUtil;
import com.ecom.mobile.accessories.util.Util;

@Controller
@RequestMapping("/products")
@PreAuthorize("hasAuthority('" + SecurityUtil.MANAGE_PRODUCTS + "')")
public class ProductsController {

	@Autowired
	ProductsRepository productsRepository;

	@GetMapping
	public String products(Model model) {
		model.addAttribute("list", productsRepository.findAll());
		return "seller/products";
	}

	@GetMapping("/new")
	public String addProducts(Model model) {
		model.addAttribute("product", new Products());
//		File dir = new File("./images/products");
//		for (File file : dir.listFiles()) {
//            if (file.isDirectory()) {
//                System.out.println("Directory: " + file.getAbsolutePath());
//            } else {
//                System.out.println("File: " + file.getAbsolutePath());
//            }
//        }
		return "seller/addProd";
	}

	@PostMapping("/new")
	public String addProducts(@ModelAttribute("product") Products prod, RedirectAttributes redirectAttributes) {
		try {

			Products p = productsRepository.findTopByOrderByIdDesc();
			if (p != null && p.getCode() != null) {
				int code = Integer.parseInt(p.getCode()) + 1;
				prod.setCode(code + "");
			} else {
				redirectAttributes.addFlashAttribute(SecurityUtil.STATUS_ERROR, SecurityUtil.STATUS_REMARK_FAILED);
				return "redirect:/products";
			}
			productsRepository.save(prod);
			redirectAttributes.addFlashAttribute(SecurityUtil.STATUS_INFO, SecurityUtil.STATUS_REMARK_CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute(SecurityUtil.STATUS_ERROR, SecurityUtil.STATUS_REMARK_FAILED);
		}
		return "redirect:/products";
	}

	@GetMapping("/upload")
	public String uploadFile() {
		return "seller/prodUpload";
	}

	@PostMapping("/upload")
	public String uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			readFromExcel(file);
			redirectAttributes.addFlashAttribute(SecurityUtil.STATUS_INFO, SecurityUtil.STATUS_REMARK_UPLOADED);
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute(SecurityUtil.STATUS_ERROR, SecurityUtil.STATUS_REMARK_FAILED);
		}
		return "redirect:/products";
	}

	private void readFromExcel(MultipartFile file) throws IOException {
		try (XSSFWorkbook myWorkBook = new XSSFWorkbook(file.getInputStream())) {
			XSSFSheet mySheet = myWorkBook.getSheetAt(0);
			Iterator<Row> rowIterator = mySheet.iterator();
			rowIterator.next();
			List<Products> list = new ArrayList<>();
			Products p = productsRepository.findTopByOrderByIdDesc();
			int code = 10001;
			if (p != null) {
				code = Integer.parseInt(p.getCode()) + 1;
			}
			StringBuilder sb = new StringBuilder();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();

				int rowNum = row.getRowNum();
				String brand = getVal(row.getCell(1));
				String prodName = getVal(row.getCell(2));
				String colours = getVal(row.getCell(4));
				int cost = getCost(getVal(row.getCell(5)));
				String desc = getVal(row.getCell(6));
				String img = getVal(row.getCell(7));
				String category = getVal(row.getCell(8));
				String quantity = getVal(row.getCell(9));

				System.out.println("row Number::" + rowNum + "\t brand::" + brand + "\t\t prodName::" + prodName
						+ "\t\t colours::" + colours + "\t\t cost::" + cost + "\t\t desc::" + desc + "\t\t img::" + img
						+ "\t\t category::" + category + "\t\t quantity::" + quantity);

				if (!Util.isEmpty(prodName) && cost != 0 && !Util.isEmpty(quantity) && !Util.isEmpty(category)
						&& !Util.isEmpty(img)) {
					Products prod = new Products(prodName, cost, brand, img, colours, desc, category, quantity);
					prod.setCode(code + "");
					code++;
					list.add(prod);
				} else {
					sb.append(rowNum + ",");
				}

				if (list.size() == 50) {
					productsRepository.saveAll(list);
					list.clear();
				}
			}

			if (list.size() < 50) {
				productsRepository.saveAll(list);
				list.clear();
			}
			System.out.println(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getCost(String cost) {
		try {
			if (!Util.isEmpty(cost)) {
				if (cost.contains(".")) {
					return (int) Math.round(Double.parseDouble(cost));
				} else {
					return Integer.parseInt(cost);
				}
			}
		} catch (NumberFormatException e) {
			return 0;
		}
		return 0;
	}

	public String getVal(Cell cell) {
		if (cell != null) {
			switch (cell.getCellType()) {
			case STRING:
				return cell.getStringCellValue();
			case NUMERIC:
				return cell.getNumericCellValue() + "";
			default:
				break;
			}
		}
		return null;
	}

}