package Food.Survey.Model;

public class Survey {
    private String imageName;
    private int     rating;
    public Survey(String imageName, int rating) {
        this.imageName = imageName;
        this.rating = rating;
    }
    public String getImageName() {
        return imageName;
    }
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
    public int getRating() {
        return rating;
    }
    public void setRating(int rating) {
        this.rating = rating;
    }
}
