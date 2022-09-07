package com.ecom.mobile.accessories.entites;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "tbl_user")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Pattern(regexp = "^[A-Za-z0-9]*$", message = "*Must be Alphanumeric only")
	@Column(nullable = false, unique = true)
	@NotEmpty(message = "Name cannot be blank")
	private String name;

	@Column(nullable = false, unique = true)
	@NotEmpty(message = "Email cannot be blank")
	@Email(message = "Invalid email Id")
	private String email;

	@Column(nullable = false)
	@Size(min = 4)
	@NotEmpty(message = "Password cannot be blank")
	private String password;

	private boolean status = true;

	private Date createdDate = new Date();

	private Date lastLoginDate;

//	private String createdBy;

	private Date blockedDate;

	private String blockedBy;

	private String updatedBy;

	private Date updatedDate;

	private int loginAttempts;

	private int maxloginattempts = 5;

	private Date logintime;

	private Date logoutTime;

	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(name = "user_address", joinColumns = { @JoinColumn(name = "USER_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "ADDRESS_ID") })
	@LazyCollection(LazyCollectionOption.TRUE)
	private List<UserAddress> address;

	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(name = "user_role", joinColumns = { @JoinColumn(name = "USER_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "ROLE_ID") })
	@LazyCollection(LazyCollectionOption.TRUE)
	private List<Role> roles;

	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(name = "cart", joinColumns = { @JoinColumn(name = "USER_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "PROD_ID") })
	@LazyCollection(LazyCollectionOption.TRUE)
	private List<Products> cart;

	private Date passwordUpdatedDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public List<Products> getCart() {
		return cart;
	}

	public void setCart(List<Products> cart) {
		this.cart = cart;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

//	public String getCreatedBy() {
//		return createdBy;
//	}
//
//	public void setCreatedBy(String createdBy) {
//		this.createdBy = createdBy;
//	}

	public Date getBlockedDate() {
		return blockedDate;
	}

	public void setBlockedDate(Date blockedDate) {
		this.blockedDate = blockedDate;
	}

	public String getBlockedBy() {
		return blockedBy;
	}

	public void setBlockedBy(String blockedBy) {
		this.blockedBy = blockedBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Date getPasswordUpdatedDate() {
		return passwordUpdatedDate;
	}

	public void setPasswordUpdatedDate(Date passwordUpdatedDate) {
		this.passwordUpdatedDate = passwordUpdatedDate;
	}

	public int getLoginAttempts() {
		return loginAttempts;
	}

	public void setLoginAttempts(int loginAttempts) {
		this.loginAttempts = loginAttempts;
	}

	public int getMaxloginattempts() {
		return maxloginattempts;
	}

	public void setMaxloginattempts(int maxloginattempts) {
		this.maxloginattempts = maxloginattempts;
	}

	public Date getLogintime() {
		return logintime;
	}

	public void setLogintime(Date logintime) {
		this.logintime = logintime;
	}

	public Date getLogoutTime() {
		return logoutTime;
	}

	public void setLogoutTime(Date logoutTime) {
		this.logoutTime = logoutTime;
	}

	public List<UserAddress> getAddress() {
		return address;
	}

	public void setAddress(List<UserAddress> address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "" + name + "," + password + "," + email + "";
	}
}