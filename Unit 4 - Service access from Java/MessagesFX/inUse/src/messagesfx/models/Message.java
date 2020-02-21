package messagesfx.models;

import javafx.scene.image.ImageView;
import messagesfx.services.ServiceUtils;

public class Message {

    private String _id;
    private String from;
    private String to;
    private String message;
    private String image;
    private String sent;

    public Message(String to, String message, String sent) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.sent = sent;
    }

    public Message(String to, String message, String image, String sent) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.image = image;
        this.sent = sent;
    }

    public Message(String _id, String from, String to, String message, String image, String sent) {
        this._id = _id;
        this.from = from;
        this.to = to;
        this.message = message;
        this.image = image;
        this.sent = sent;
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

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSent() {
        return sent;
    }

    public void setSent(String sent) {
        this.sent = sent;
    }

    @Override
    public String toString() {
        return "ID: " + _id + ", from: " + from + ", to: " + to +
                ", message: " + message + ", image: " + image + ", sent: " + sent;
    }
}
