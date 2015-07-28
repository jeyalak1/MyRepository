package com.harris.mywebapp;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class TransactionForm
 */
/**
 * @author Jeyashree
 *
 */

@WebServlet(urlPatterns = { "/account" })
public class TransactionForm extends HttpServlet {
	private static final long serialVersionUID = 1L;

	ArrayList<Transaction> transactionList = new ArrayList<Transaction>();
	private static Map<Long, Transaction> transactions = new HashMap<Long, Transaction>();
	Map<Long, Transaction> transactionMap;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.getWriter().print("Transaction Form");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/plain");
		HashSet<Fee> feeSet = new HashSet<Fee>();
		HashSet<Payment> paymentSet = new HashSet<Payment>();


		double feeValue;
		double paymentValue;
		Double sumOfPayments = 0.0;
		Double sumOfFees = 0.0;

		// Get all the values for the fees entered by the user in the
		// transaction page
		String[] feeAmt = request.getParameterValues("feeAmount");

		// Iterate through the list of fee values and set it to an array of Fee
		// objects
		if (feeAmt != null) {
			
			for (int feeind = 0; feeind < feeAmt.length; feeind++) {
				feeValue = Double.parseDouble(feeAmt[feeind]);
				Fee fees = new Fee();
				fees.setAmount(feeValue);
				fees.setName("F" + feeind);
				feeSet.add(fees);
				sumOfFees += feeValue;

			}
		}
		

		// Iterate through the list of payment values and set it to an array of
		// Payment objects
		String[] paymentAmt = request.getParameterValues("paymentAmount");
		if (paymentAmt != null) {
		
			for (int payind = 0; payind < paymentAmt.length; payind++) {
				paymentValue = Double.parseDouble(paymentAmt[payind]);
				Payment payments = new Payment();
				payments.setAmount(paymentValue);
				paymentSet.add(payments);
				sumOfPayments += paymentValue;

			}

		}

		// Creates all transactions based on the fee values and payment values
		// entered by the user
		 List transactions = createAllTransactions(feeSet, paymentSet,
				sumOfFees, sumOfPayments);

		// Create a map of transactions mapped to an id
		if (transactions != null) {
			transactionMap = createTransactionMap(transactions);
		}
		Iterator<Long> iter = transactionMap.keySet().iterator();
		while (iter.hasNext()) {
			Long key = iter.next();
			Transaction value = transactionMap.get(key);

			if (value != null && value.getPayment() != null) {
				value.getFee().getAmount();
				value.getPayment().getAmount();
				// Set the transaction key to the request
				request.setAttribute(key.toString(), value);

			}

		}

		request.setAttribute("transactionMap", transactionMap);
		request.getRequestDispatcher("/account.jsp").forward(request, response);
		
	}

	/**
	 * Method to create all transactions based on the fees and payments entered
	 * by the user
	 * 
	 * @param feelist
	 * @param paymentlist
	 * @param feeSum
	 * @param paymentSum
	 * @return
	 */
	private List createAllTransactions(HashSet<Fee> feelist,
			HashSet<Payment> paymentlist, Double feeSum, Double paymentSum) {
		
		int pos = 0;

		double sumOfTransactions = 0.0;
		double sumOfFees = feeSum;
		double sumOfPayments = paymentSum;
		Double newFeeSum = 0.0;
		Double newPaymentSum = 0.0;
		Double paySum = 0.0;
		Iterator<Fee> feeIter = feelist.iterator();
				Iterator<Payment> paymentIter = paymentlist.iterator();
		List<Transaction> transactions = new ArrayList<Transaction>();
		int paycount = 0;
        int feeListSize = feelist.size();
        int paymentListSize = paymentlist.size();
        if(paymentListSize > feeListSize) {
        	Fee fee;
    		Fee fee1 = new Fee();
    		Payment payment;
    		Transaction trans;
             fee = (Fee)feeIter.next();
			 
		     payment = (Payment)paymentIter.next();
		    
		     trans = createTransaction(fee, payment, pos,
						paycount);
			// Creates a single transaction
			 transactions.add(trans);
			
			pos++;
        	while(paymentIter.hasNext()){
        		
        		while(feeIter.hasNext()){
        			if(fee.getAmount() > payment.getAmount()) {
           			 fee = (Fee)feeIter.next();
           			 trans = createTransaction(fee, payment, pos,
        						paycount);
           			}
           			else if(payment.getAmount() > fee.getAmount() && paymentIter.hasNext()) {
           		     payment = (Payment)paymentIter.next();
           		     trans = createTransaction(fee, payment, pos,
        						paycount);
           			}
        			// Creates a single transaction
        			 transactions.add(trans);
        			System.out.println("tran amt = "+ transactions.get(pos).getAmount());
        			pos++;
        			fee1 = fee;
        		}
        	    payment = (Payment)paymentIter.next();
        		trans = createTransaction(fee1, payment, pos,
						paycount);
        		transactions.add(trans);
        		System.out.println("tran amt = "+ transactions.get(pos).getAmount());
        		pos++;
        	}
        	
        	
        	
        }
        else  if(feeListSize > paymentListSize) {
        	Fee fee;
    		Payment payment1 = new Payment();
    		Payment payment;
    		Transaction trans;
    		 fee = (Fee)feeIter.next();
			 
		     payment = (Payment)paymentIter.next();
		    
		     trans = createTransaction(fee, payment, pos,
						paycount);
			// Creates a single transaction
			 transactions.add(trans);
			
			pos++;
    		while(feeIter.hasNext()){
        		
        		while(paymentIter.hasNext()){
        			
        			if(fee.getAmount() > payment.getAmount() && feeIter.hasNext()) {
        			 fee = (Fee)feeIter.next();
        			 trans = createTransaction(fee, payment, pos,
     						paycount);
        			}
        			else if(payment.getAmount() > fee.getAmount()) {
        		     payment = (Payment)paymentIter.next();
        		     trans = createTransaction(fee, payment, pos,
     						paycount);
        			}
        		     trans = createTransaction(fee, payment, pos,
        						paycount);
        			// Creates a single transaction
        			 transactions.add(trans);
        			System.out.println("tran amt = "+ transactions.get(pos).getAmount());
        			pos++;
        			payment1 = payment;
        		}
        		fee = (Fee)feeIter.next();
        		trans = createTransaction(fee, payment1, pos,
						paycount);
        		transactions.add(trans);
        		System.out.println("tran amt = "+ transactions.get(pos).getAmount());
        		pos++;
        	}
        	
        	
        	
        }
        Double excessAmt = sumOfPayments - sumOfFees;
    	if (paymentSum > feeSum) {
            Fee firstFee = (Fee) feelist.toArray()[0];
			Double total = firstFee.getAmount() + excessAmt;
			
			transactions.get(0).getFee().setAmount(total);

		}
		

		/*newFeeSum = feelist[pos].getAmount();
		newPaymentSum = paymentlist[paycount].getAmount();
		sumOfTransactions = transactions[pos].getAmount();

		Double excessAmt = sumOfPayments - sumOfFees;

		if (feesList == 1 && feesList < paymentsList) {

			if (paymentSum > feeSum) {

				Double total = feelist[0].getAmount() + excessAmt;
				transactions[0].getFee().setAmount(total);

			}

		}

		pos++;
		int newpos = 0;
		if (feesList >= paymentsList) {
			while (newFeeSum <= sumOfFees) {

				paySum = paymentlist[paycount].getAmount();

				if (newPaymentSum > sumOfTransactions && pos <= feesList
						&& newPaymentSum == sumOfPayments && feesList == 1
						&& paymentsList == 1) {

					break;
				}

				if (newPaymentSum > sumOfTransactions && pos <= feesList) {
					transactions[pos] = createTransaction(feelist, paymentlist,
							pos, paycount);
					newFeeSum += feelist[pos].getAmount();
					newPaymentSum = paySum;
					sumOfTransactions += transactions[pos].getAmount();
					pos++;
				}

				if (newPaymentSum >= sumOfTransactions && pos == feesList) {

					break;
				}

				if (sumOfTransactions >= newPaymentSum) {

					paycount++;

					if (paycount == feesList || paycount >= paymentsList) {
						break;
					}
					if (pos >= feesList && newFeeSum == sumOfFees) {
						break;
					}

					transactions[pos] = createTransaction(feelist, paymentlist,
							pos, paycount);

					newFeeSum += feelist[pos].getAmount();
					newPaymentSum = paymentlist[paycount].getAmount();
					sumOfTransactions = transactions[pos].getAmount();

				}

			}

			// When sum of payments exceeds the sum of fees,
			// the excess payment amount is applied to the first fee entered by
			// the user
			excessAmt = sumOfPayments - sumOfFees;
			if (paymentSum > feeSum) {

				Double total = feelist[0].getAmount() + excessAmt;
				transactions[0].getFee().setAmount(total);

			} else if (feeSum > paymentSum) {

				if (sumOfTransactions >= newPaymentSum) {
					Double excessamt2 = sumOfTransactions - newPaymentSum;
					Double diff = feelist[--pos].getAmount() - excessamt2;
					Double oldfeeAmt = 0.0;
					if (feelist[pos].getAmount() > diff) {
						transactions[pos].setAmount(diff);
						oldfeeAmt = feelist[pos].getAmount();
						if (paycount < paymentsList) {
							newpos = pos + 1;
							transactions[newpos] = createTransaction(feelist,
									paymentlist, pos, paycount);

						}

					}
				}
			}
		} else if (paymentsList > feesList) {
			while (newFeeSum <= sumOfFees) {

				paySum = paymentlist[paycount].getAmount();

				if (newPaymentSum > sumOfTransactions && pos <= feesList) {
					transactions[pos] = createTransaction(feelist, paymentlist,
							pos, paycount);
					newFeeSum += feelist[pos].getAmount();
					newPaymentSum = paySum;
					sumOfTransactions += transactions[pos].getAmount();
					pos++;
				}
				if (newPaymentSum >= sumOfTransactions && pos == feesList
						&& pos == paymentsList) {

					break;
				}

				if (sumOfTransactions >= newPaymentSum) {

					paycount++;

					if (paycount > paymentsList) {
						break;
					}
					if (pos <= feesList && newFeeSum == sumOfFees
							&& sumOfTransactions == newPaymentSum) {
						newpos = pos - 1;
						transactions[pos] = createTransaction(feelist,
								paymentlist, newpos, paycount);
						break;
					}

					if (pos >= feesList && newFeeSum == sumOfFees) {
						break;
					}

					transactions[pos] = createTransaction(feelist, paymentlist,
							pos, paycount);

					newFeeSum += feelist[pos].getAmount();
					newPaymentSum = paymentlist[paycount].getAmount();
					sumOfTransactions = transactions[pos].getAmount();

				}

			}

			// When sum of payments exceeds the sum of fees,
			// the excess payment amount is applied to the first fee entered by
			// the user
			excessAmt = sumOfPayments - sumOfFees;
			if (paymentSum > feeSum) {

				Double total = feelist[0].getAmount() + excessAmt;
				transactions[0].getFee().setAmount(total);

			}

		}*/

		return transactions;
	}

	/**
	 * This method creates a single transaction
	 * 
	 * @param fee
	 * @param payment
	 * @param count
	 * @return
	 */
	private Transaction createTransaction(Fee fee, Payment payment,
			int count, int paycount1) {

		Double feeamt = fee.getAmount();
		Double paymentamt = payment.getAmount();
		String feeName = fee.getName();
		// Get the minimum of fee amount and payment amount and set it to
		// transaction amount
		Double minAmt = Math.min(feeamt, paymentamt);
		Fee fee1 = new Fee();
		fee1.setAmount(feeamt);
		Payment payment1 = new Payment();
		payment1.setAmount(paymentamt);
		fee1.setName(feeName);
		Transaction tran = new Transaction();
		tran.setAmount(minAmt);
		tran.setFee(fee);
		tran.setPayment(payment);
		//tran.getFee().setName(fee.getName());
		return tran;
	}

	/**
	 * Method that creates a transactionMap to map the transactions to their key
	 * 
	 * @param transaction
	 * @return transaction map
	 */
	private Map<Long, Transaction> createTransactionMap(
			List<Transaction> transaction) {

		for (int i = 0; i < transaction.size(); i++) {

			transactions.put((long) i, (Transaction) transaction.get(i));
		}

		return transactions;

	}

}
