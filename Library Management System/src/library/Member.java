package library;

public class Member {
    private String id;
    private String name;
    private String email;
    private String phone;
    private int itemsCheckedOut;

    public Member(String id, String name, String email, String phone, int itemsCheckedOut) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.itemsCheckedOut = itemsCheckedOut;
    }

    // Getters and setters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public int getItemsCheckedOut() { return itemsCheckedOut; }

    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setItemsCheckedOut(int count) { this.itemsCheckedOut = count; }
}

