//package com.zosh.model;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.FetchType;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import jakarta.persistence.Table;
//import lombok.Data;
//
//@Entity
//@Data
//@Table(name="notifications")
//public class Notification {
//	
//  	@Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//	  
//  	@ManyToOne(fetch = FetchType.LAZY)
//  	@JoinColumn(name = "user_id")
//  	private User user;
//  	
//  	@ManyToOne(fetch = FetchType.LAZY)
//  	@JoinColumn(name = "twit_id")
//  	private Twit twit;
//  	
//}
