package data;

public class MyMessage {

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    String message;

    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append("My Fancy Message Class => ");
        str.append("Message: " + message);
        return str.toString();
    }

}
