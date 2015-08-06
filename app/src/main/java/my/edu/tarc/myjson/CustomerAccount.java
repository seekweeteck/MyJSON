package my.edu.tarc.myjson;

/**
 * Created by TARC on 8/6/2015.
 */
public class CustomerAccount {
    private String ca_id;
    private String ca_name;

    public CustomerAccount() {
    }

    public CustomerAccount(String ca_id, String ca_name) {
        this.ca_id = ca_id;
        this.ca_name = ca_name;
    }

    public String getCa_id() {
        return ca_id;
    }

    public void setCa_id(String ca_id) {
        this.ca_id = ca_id;
    }

    public String getCa_name() {
        return ca_name;
    }

    public void setCa_name(String ca_name) {
        this.ca_name = ca_name;
    }

    @Override
    public String toString() {
        return "CustomerAccount{" +
                "ca_id='" + ca_id + '\'' +
                ", ca_name='" + ca_name + '\'' +
                '}';
    }
}
