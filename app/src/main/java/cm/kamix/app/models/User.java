package cm.kamix.app.models;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class User implements Serializable{
    @SerializedName("_id") @Expose private String id;
    @SerializedName("firstname") @Expose private String firstname;
    @SerializedName("lastname") @Expose private String lastname;
    @SerializedName("email") @Expose private String email;
    @SerializedName("email_verified") @Expose private boolean emailVerified;
    @SerializedName("mobiles") @Expose private List<String> mobiles;
    @SerializedName("mobiles_verified") @Expose private List<Boolean> mobilesVerified;
    @SerializedName("secret_code") @Expose private String secretCode;
    @SerializedName("address") @Expose private String address;
    @SerializedName("id_document") @Expose private String idDocument;
    @SerializedName("photo") @Expose private String photo;
    @SerializedName("birth_date") @Expose private Date birthdate;
    @SerializedName("registration_date") @Expose private long registrationDate;
    @SerializedName("activation_date") @Expose private long activationDate;
    @SerializedName("balance") @Expose private double balance;
    @SerializedName("currency") @Expose private String currency;
    @SerializedName("country_code") @Expose private String countryCode;
    @SerializedName("country") @Expose private String country;
    @SerializedName("city") @Expose private String city;
    @SerializedName("contacts_list") @Expose private List<Contact> contacts;
    @SerializedName("withdrawals") @Expose private List<Withdrawal> withdrawals;
    @SerializedName("fundings") @Expose private List<Funding> fundings;
    @SerializedName("transferts") @Expose private List<Transfert> transferts;
    @SerializedName("payments") @Expose private List<Payment> payments;
    @SerializedName("fidelity_points") @Expose private double fidelityPoints;
    @SerializedName("status") @Expose private int status;

    public User() {
    }

    public boolean noName(){
        return ((firstname==null || TextUtils.isEmpty(firstname)) && (lastname==null || TextUtils.isEmpty(lastname)));
    }

    public String name(){
        String name = "";
        if (firstname!=null && !TextUtils.isEmpty(firstname)) name = name + firstname ;
        if (lastname!=null && !TextUtils.isEmpty(lastname)) {
            if (name.isEmpty()) name = name + lastname;
            else name = name + " " + lastname;
        }
        return name;
    }

    public List<String> getMobiles() {
        return mobiles;
    }

    public void setMobiles(List<String> mobiles) {
        this.mobiles = mobiles;
    }

    public List<Boolean> getMobilesVerified() {
        return mobilesVerified;
    }

    public void setMobilesVerified(List<Boolean> mobilesVerified) {
        this.mobilesVerified = mobilesVerified;
    }

    public List<Withdrawal> getWithdrawals() {
        return withdrawals;
    }

    public void setWithdrawals(List<Withdrawal> withdrawals) {
        this.withdrawals = withdrawals;
    }

    public List<Funding> getFundings() {
        return fundings;
    }

    public void setFundings(List<Funding> fundings) {
        this.fundings = fundings;
    }

    public List<Transfert> getTransferts() {
        return transferts;
    }

    public void setTransferts(List<Transfert> transferts) {
        this.transferts = transferts;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getSecretCode() {
        return secretCode;
    }

    public void setSecretCode(String secretCode) {
        this.secretCode = secretCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdDocument() {
        return idDocument;
    }

    public void setIdDocument(String idDocument) {
        this.idDocument = idDocument;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public long getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(long registrationDate) {
        this.registrationDate = registrationDate;
    }

    public long getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(long activationDate) {
        this.activationDate = activationDate;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public double getFidelityPoints() {
        return fidelityPoints;
    }

    public void setFidelityPoints(double fidelityPoints) {
        this.fidelityPoints = fidelityPoints;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
