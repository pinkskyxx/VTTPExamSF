package sdf.mytaskTwo;
 
public class Items {
 
    private Integer prod_id;
    private String title;
    private Float price;
    private Float rating;
 
    public Items(Integer prod_id, String title, Float price, Float rating) {
        this.prod_id = prod_id;
        this.title = title;
        this.price = price;
        this.rating = rating;
    }
 
    public Integer getProd_id() {
        return prod_id;
    }
 
    public void setProd_id(Integer prod_id) {
        this.prod_id = prod_id;
    }
 
    public String getTitle() {
        return title;
    }
 
    public void setTitle(String title) {
        this.title = title;
    }
 
    public Float getPrice() {
        return price;
    }
 
    public void setPrice(Float price) {
        this.price = price;
    }
 
    public Float getRating() {
        return rating;
    }
 
    public void setRating(Float rating) {
        this.rating = rating;
    }
 
}
