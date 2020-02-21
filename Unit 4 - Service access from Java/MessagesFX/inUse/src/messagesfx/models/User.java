package messagesfx.models;

import javafx.scene.image.ImageView;
import messagesfx.services.ServiceUtils;

public class User {

    private String _id;
    private String name;
    private String password;
    private String image;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public User(String name, String password, String image) {
        this.name = name;
        this.password = password;
        this.image = image;
    }

    public User(String _id, String name, String password, String image) {
        this._id = _id;
        this.name = name;
        this.password = password;
        this.image = image;
    }

    public ImageView getImageView() {
        ImageView imgView = new ImageView(ServiceUtils.SERVER + "/" + image);
        imgView.setFitHeight(30);
        imgView.setPreserveRatio(true);
        return imgView;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "ID: " + _id + ", name: " + name +
                ", password: " + password + ", image: " + image;
    }
}
