package com.example.casita_v1;

public class DataStoreAdmin {
 String name, username, phone, pass, venueName, cityName, completeAdd, price, capacity, typeSelected, imageUrl, ratings, about;

 public DataStoreAdmin(String name, String username, String phone, String pass, String venueName, String cityName, String completeAdd
         , String price, String capacity, String typeSelected, String imageUrl, String ratings, String about) {
  this.name = name;
  this.username = username;
  this.phone = phone;
  this.pass = pass;
  this.venueName = venueName;
  this.cityName = cityName;
  this.completeAdd = completeAdd;
  this.price = price;
  this.capacity = capacity;
  this.typeSelected = typeSelected;
  this.imageUrl = imageUrl;
  this.ratings = ratings;
  this.about = about;

 }

 public String getAbout() {
  return about;
 }

 public void setAbout(String about) {
  this.about = about;
 }

 public String getRatings() {
  return ratings;
 }

 public void setRatings(String ratings) {
  this.ratings = ratings;
 }

 public String getImageUrl() {
  return imageUrl;
 }

 public void setImageUrl(String imageUrl) {
  this.imageUrl = imageUrl;
 }

 public String getTypeSelected() {
  return typeSelected;
 }

 public void setTypeSelected(String typeSelected) {
  this.typeSelected = typeSelected;
 }

 public void setVenueName(String venueName) {
  this.venueName = venueName;
 }

 public void setCityName(String cityName) {
  this.cityName = cityName;
 }

 public void setCompleteAdd(String completeAdd) {
  this.completeAdd = completeAdd;
 }


 public void setPrice(String price) {
  this.price = price;
 }

 public void setCapacity(String capacity) {
  this.capacity = capacity;
 }


 public DataStoreAdmin() {
 }

 public String getVenueName() {
  return venueName;
 }

 public String getCityName() {
  return cityName;
 }

 public String getCompleteAdd() {
  return completeAdd;
 }


 public String getPrice() {
  return price;
 }

 public String getCapacity() {
  return capacity;
 }


 public String getName() {
  return name;
 }

 public void setName(String name) {
  this.name = name;
 }

 public String getUsername() {
  return username;
 }

 public void setUsername(String username) {
  this.username = username;
 }


 public String getPhone() {
  return phone;
 }

 public void setPhone(String phone) {
  this.phone = phone;
 }

 public String getPass() {
  return pass;
 }

 public void setPass(String pass) {
  this.pass = pass;
 }
}



