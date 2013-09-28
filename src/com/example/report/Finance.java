package com.example.report;

public class Finance {
	//private variables
    int id;
    String date;
    int amount;
    String category;
    String description;
   
 
    // Empty constructor
    public Finance(){
 
    }


	public Finance(int id, String date, int amount, String category,
			String description) {
		super();
		this.id = id;
		this.date = date;
		this.amount = amount;
		this.category = category;
		this.description = description;
	}


	public Finance(String date, int amount, String category,
			String description) {
		super();
		this.date = date;
		this.amount = amount;
		this.category = category;
		this.description = description;
	}


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public int getAmount() {
		return amount;
	}


	public void setAmount(int amount) {
		this.amount = amount;
	}


	public String getCategory() {
		return category;
	}


	public void setCategory(String category) {
		this.category = category;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}



}
