package com.harris.mywebapp;

/**
 * @author Jeyashree
 *
 */
public class Fee {

	private double amount;
	private String name;

	public Fee(double amount, String name) {
		super();
		this.amount = amount;
		this.name = name;
	}

	public Fee() {

	}

	/**
	 * @return
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * @param excessPaymentamt
	 */
	public void setAmount(double excessPaymentamt) {
		this.amount = excessPaymentamt;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

}
