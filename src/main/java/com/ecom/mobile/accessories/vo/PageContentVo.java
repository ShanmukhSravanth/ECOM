package com.ecom.mobile.accessories.vo;

import java.util.List;

import org.springframework.data.domain.Page;

import com.ecom.mobile.accessories.entites.Products;

public class PageContentVo {
	public Page<Products> productsPages;

	public List<Products> list;

	public int pageNum;

	public int pages;

	public PageContentVo(Page<Products> page) {
		this.productsPages = page;
		if (productsPages != null) {
			pages = productsPages.getTotalPages();
			pageNum = productsPages.getNumber();
			list = productsPages.getContent();
		}
	}

	public PageContentVo(List<Products> l, long count, int page, int size) {
		list = l;
		if (list != null && !list.isEmpty()) {
			pages = (size == 0 ? 1 : (int) Math.ceil((double) count / (double) size));
			pageNum = page;
		}
	}

}