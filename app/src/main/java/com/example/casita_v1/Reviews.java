package com.example.casita_v1;

public class Reviews {
 String user, review, ratings, venue;

 public Reviews(String user, String review, String ratings, String venue) {
  this.user = user;
  this.review = review;
  this.ratings = ratings;
  this.venue = venue;

 }

 public Reviews() {
 }


 public String getUser() {
  return user;
 }

 public void setUser(String user) {
  this.user = user;
 }

 public String getReview() {
  return review;
 }

 public void setReview(String review) {
  this.review = review;
 }

 public String getRatings() {
  return ratings;
 }

 public void setRatings(String ratings) {
  this.ratings = ratings;
 }

 public String getVenue() {
  return venue;
 }

 public void setVenue(String venue) {
  this.venue = venue;
 }
}
