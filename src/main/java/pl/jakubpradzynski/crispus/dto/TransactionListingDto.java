package pl.jakubpradzynski.crispus.dto;

public class TransactionListingDto {

    private String account;
    private String place;
    private String category;
    private Double value;
    private Double biggerThan;
    private Double smallerThan;
    private String date;
    private String beforeDate;
    private String afterDate;

    public TransactionListingDto() {
    }

    public TransactionListingDto(String account, String place, String category, Double value, Double biggerThan, Double smallerThan, String date, String beforeDate, String afterDate) {
        this.account = account;
        this.place = place;
        this.category = category;
        this.value = value;
        this.biggerThan = biggerThan;
        this.smallerThan = smallerThan;
        this.date = date;
        this.beforeDate = beforeDate;
        this.afterDate = afterDate;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Double getBiggerThan() {
        return biggerThan;
    }

    public void setBiggerThan(Double biggerThan) {
        this.biggerThan = biggerThan;
    }

    public Double getSmallerThan() {
        return smallerThan;
    }

    public void setSmallerThan(Double smallerThan) {
        this.smallerThan = smallerThan;
    }



    @Override
    public String toString() {
        return "TransactionListingDto{" +
                "account='" + account + '\'' +
                ", place='" + place + '\'' +
                ", category='" + category + '\'' +
                ", value=" + value +
                ", biggerThan=" + biggerThan +
                ", smallerThan=" + smallerThan +
                '}';
    }
}
