package cm.kamix.app.localstorage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.util.Pair;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cm.kamix.app.models.Contact;
import cm.kamix.app.models.Funding;
import cm.kamix.app.models.Payment;
import cm.kamix.app.models.Transfert;
import cm.kamix.app.models.User;
import cm.kamix.app.models.Withdrawal;

public class DAO {
    private SQLiteDatabase database;
    private DbHelper helper;

    public DAO(Context context){
        helper = new DbHelper(context);
    }

    private void open(){
        database = helper.getWritableDatabase();
    }

    private void close(){
        database.close();
    }

    private ContentValues userToContentValue(User user){
        ContentValues values = new ContentValues();
        values.put(DbHelper.DB_USERS__ID, user.getId());
        values.put(DbHelper.DB_USERS__FIRSTNAME, user.getFirstname());
        values.put(DbHelper.DB_USERS__LASTNAME, user.getLastname());
        values.put(DbHelper.DB_USERS__EMAIL, user.getEmail());
        values.put(DbHelper.DB_USERS__EMAIL_VERIFIED, user.isEmailVerified()?1:0);
        values.put(DbHelper.DB_USERS__SECRET_CODE, user.getSecretCode());
        values.put(DbHelper.DB_USERS__ADDRESS, user.getAddress());
        values.put(DbHelper.DB_USERS__ID_DOCUMENT, user.getIdDocument());
        values.put(DbHelper.DB_USERS__PHOTO, user.getPhoto());
        values.put(DbHelper.DB_USERS__BIRTHDATE, user.getBirthdate()==null?"":user.getBirthdate().toString());
        values.put(DbHelper.DB_USERS__REGISTRATION_DATE, user.getRegistrationDate());
        values.put(DbHelper.DB_USERS__ACTIVATION_DATE, user.getActivationDate());
        values.put(DbHelper.DB_USERS__BALANCE, user.getBalance());
        values.put(DbHelper.DB_USERS__CURRENCY, user.getCurrency());
        values.put(DbHelper.DB_USERS__COUNTRY_CODE, user.getCountryCode());
        values.put(DbHelper.DB_USERS__COUNTRY, user.getCountry());
        values.put(DbHelper.DB_USERS__CITY, user.getCity());
        values.put(DbHelper.DB_USERS__FIDELITY_POINTS, user.getFidelityPoints());
        values.put(DbHelper.DB_USERS__STATUS, user.getStatus());
        return values;
    }

    private void insertUser(User user){
        ContentValues values = userToContentValue(user);
        database.insert(DbHelper.DB_USERS_NAME, null, values);
    }

    private void updateUser(User user){
        ContentValues values = userToContentValue(user);

        database.update(DbHelper.DB_USERS_NAME, values, DbHelper.DB_USERS__ID + " = '" + user.getId() + "'", null);
    }

    private User getUser(){
        open();
        User user = null;
        Cursor cursor = database.query(DbHelper.DB_USERS_NAME, DbHelper.COLUMNS_DB_USERS, /*DbHelper.DB_USERS__ID + " = '" + userId + "'",*/null, null,
                null, null, null);
        if (cursor.moveToFirst()){
            do{
                user = new User();
                user.setId(cursor.getString(0));
                user.setFirstname(cursor.getString(1));
                user.setLastname(cursor.getString(2));
                user.setEmail(cursor.getString(3));
                user.setEmailVerified(cursor.getInt(4)==1);
                user.setSecretCode(cursor.getString(5));
                user.setAddress(cursor.getString(6));
                user.setIdDocument(cursor.getString(7));
                user.setPhoto(cursor.getString(8));
                user.setBirthdate((cursor.getString(9)==null || cursor.getString(9).isEmpty())?null:new Date(cursor.getString(9)));
                user.setRegistrationDate(cursor.getLong(10));
                user.setActivationDate(cursor.getLong(11));
                user.setBalance(cursor.getDouble(12));
                user.setCurrency(cursor.getString(13));
                user.setCountryCode(cursor.getString(14));
                user.setCountry(cursor.getString(15));
                user.setCity(cursor.getString(16));
                user.setFidelityPoints(cursor.getDouble(17));
                user.setStatus(cursor.getInt(18));
            } while (cursor.moveToNext());
        }
        return user;
    }

    private void insertUserMobile(String mobile, boolean verified){
        ContentValues values = new ContentValues();
        values.put(DbHelper.DB_MOBILES__MOBILE, mobile);
        values.put(DbHelper.DB_MOBILES__VERIFIED, verified);
        database.insert(DbHelper.DB_MOBILES_NAME, null, values);
    }

    private void updateUserMobile(String oldMobile, String newMobile, boolean verified){
        ContentValues values = new ContentValues();
        values.put(DbHelper.DB_MOBILES__MOBILE, newMobile);
        values.put(DbHelper.DB_MOBILES__VERIFIED, verified);
        database.update(DbHelper.DB_MOBILES_NAME, values, DbHelper.DB_MOBILES__MOBILE + " = '" + oldMobile + "'", null);
    }

    private void deleteUserMobile(String mobile){
        database.delete(DbHelper.DB_MOBILES_NAME, DbHelper.DB_MOBILES__MOBILE + " = '" + mobile + "'", null);
    }

    private void deleteUserMobiles(){
        database.delete(DbHelper.DB_MOBILES_NAME, null, null);
    }

    private List<Pair<String, Boolean>> getUserMobiles(){
        Cursor cursor = database.query(DbHelper.DB_MOBILES_NAME, DbHelper.COLUMNS_DB_MOBILES, null, null, null, null, null);
        List<Pair<String, Boolean>> mobiles = null;
        if (cursor.moveToFirst()){
            mobiles = new ArrayList<>();
            do{
                Pair<String, Boolean> mobile = new Pair<>(cursor.getString(0), cursor.getInt(1)==1);
                mobiles.add(mobile);
            } while (cursor.moveToNext());
        }
        return mobiles;
    }

    private void insertUserContact(Contact contact){
        ContentValues values = new ContentValues();
        values.put(DbHelper.DB_CONTACTS__MOBILE, contact.getMobile());
        values.put(DbHelper.DB_CONTACTS__NAME, contact.getName());
        database.insert(DbHelper.DB_CONTACTS_NAME, null, values);
    }

    private void deleteUserContacts(){
        database.delete(DbHelper.DB_CONTACTS_NAME, null, null);
    }

    private List<Contact> getUserContacts(){
        Cursor cursor = database.query(DbHelper.DB_CONTACTS_NAME, DbHelper.COLUMNS_DB_CONTACTS, null, null, null, null, null);
        List<Contact> contacts = null;
        if (cursor.moveToFirst()){
            contacts = new ArrayList<>();
            do{
                Contact contact = new Contact();
                contact.setMobile(cursor.getString(0));
                contact.setName(cursor.getString(1));
                contacts.add(contact);
            } while (cursor.moveToNext());
        }
        return contacts;
    }

    private ContentValues withdrawalToContentvalues(Withdrawal withdrawal){
        ContentValues values = new ContentValues();
        values.put(DbHelper.DB_WITHDRAWAL__ID, withdrawal.getId());
        values.put(DbHelper.DB_WITHDRAWAL__USER, withdrawal.getUserId());
        values.put(DbHelper.DB_WITHDRAWAL__AMOUNT, withdrawal.getAmount());
        values.put(DbHelper.DB_WITHDRAWAL__FEES, withdrawal.getFees());
        values.put(DbHelper.DB_WITHDRAWAL__WITHDRAWAL_DATE, withdrawal.getDate());
        values.put(DbHelper.DB_WITHDRAWAL__MOBILE_NUMBER, withdrawal.getMobileNumber());
        values.put(DbHelper.DB_WITHDRAWAL__STATUS, withdrawal.isStatus()?1:0);
        values.put(DbHelper.DB_WITHDRAWAL__STATUS_CODE, withdrawal.getStatus_code());
        values.put(DbHelper.DB_WITHDRAWAL__MESSAGE, withdrawal.getMessage());
        values.put(DbHelper.DB_WITHDRAWAL__TRANSACTION_WCU_UID, withdrawal.getTransactionWcuUid());
        values.put(DbHelper.DB_WITHDRAWAL__TRANSACTION_WCU_TOKEN, withdrawal.getTransactionWcuToken());
        return values;
    }

    private void insertWithdrawal(Withdrawal withdrawal){
        ContentValues values = withdrawalToContentvalues(withdrawal);
        database.insert(DbHelper.DB_WITHDRAWAL_NAME, null, values);
    }

    private void updateWithdrawal(Withdrawal withdrawal){
        ContentValues values = withdrawalToContentvalues(withdrawal);
        database.update(DbHelper.DB_WITHDRAWAL_NAME, values, DbHelper.DB_WITHDRAWAL__ID + " = '" + withdrawal.getId() + "'", null);
    }

    private void deleteWithdrawal(String withdrawalId){
        database.delete(DbHelper.DB_WITHDRAWAL_NAME, DbHelper.DB_WITHDRAWAL__ID + " = '" + withdrawalId + "'", null);
    }

    private void deleteAllWithdrawals(){
        database.delete(DbHelper.DB_WITHDRAWAL_NAME, null, null);
    }

    private List<Withdrawal> getWithdrawals(){
        Cursor cursor = database.query(DbHelper.DB_WITHDRAWAL_NAME, DbHelper.COLUMNS_WITHDRAWALS, null, null, null, null, null, null);
        List<Withdrawal> withdrawals = null;
        if (cursor.moveToFirst()){
            withdrawals = new ArrayList<>();
            do{
                Withdrawal withdrawal = new Withdrawal();
                withdrawal.setId(cursor.getString(0));
                withdrawal.setUserId(cursor.getString(1));
                withdrawal.setAmount(cursor.getDouble(2));
                withdrawal.setFees(cursor.getDouble(3));
                withdrawal.setDate(cursor.getLong(4));
                withdrawal.setMobileNumber(cursor.getString(5));
                withdrawal.setStatus(cursor.getInt(6)==1);
                withdrawal.setStatus_code(cursor.getInt(7));
                withdrawal.setMessage(cursor.getString(8));
                withdrawal.setTransactionWcuUid(cursor.getString(9));
                withdrawal.setTransactionWcuToken(cursor.getString(10));
            } while (cursor.moveToNext());
        }
        return withdrawals;
    }

    private boolean isWithdrawalExists(String id){
        Cursor cursor = database.query(DbHelper.DB_WITHDRAWAL_NAME, DbHelper.COLUMNS_WITHDRAWALS, DbHelper.DB_WITHDRAWAL__ID + " = '" + id + "'", null, null, null, null, null);
        return cursor.moveToFirst();
    }


    private ContentValues fundingToContentvalues(Funding funding){
        ContentValues values = new ContentValues();
        values.put(DbHelper.DB_FUNDING__ID, funding.getId());
        values.put(DbHelper.DB_FUNDING__USER, funding.getUserId());
        values.put(DbHelper.DB_FUNDING__AMOUNT, funding.getAmount());
        values.put(DbHelper.DB_FUNDING__FEES, funding.getFees());
        values.put(DbHelper.DB_FUNDING__FUNDING_DATE, funding.getDate());
        values.put(DbHelper.DB_FUNDING__MOBILE_NUMBER, funding.getMobileNumber());
        values.put(DbHelper.DB_FUNDING__STATUS, funding.isStatus()?1:0);
        values.put(DbHelper.DB_FUNDING__STATUS_CODE, funding.getStatus_code());
        values.put(DbHelper.DB_FUNDING__MESSAGE, funding.getMessage());
        values.put(DbHelper.DB_FUNDING__TRANSACTION_WCU_UID, funding.getTransactionWcuUid());
        values.put(DbHelper.DB_FUNDING__TRANSACTION_WCU_TOKEN, funding.getTransactionWcuToken());
        return values;
    }

    private void insertFunding(Funding funding){
        ContentValues values = fundingToContentvalues(funding);
        database.insert(DbHelper.DB_FUNDING_NAME, null, values);
    }

    private void updateFunding(Funding funding){
        ContentValues values = fundingToContentvalues(funding);
        database.update(DbHelper.DB_FUNDING_NAME, values, DbHelper.DB_FUNDING__ID + " = '" + funding.getId() + "'", null);
    }

    private void deleteFunding(String fundingId){
        database.delete(DbHelper.DB_FUNDING_NAME, DbHelper.DB_FUNDING__ID + " = '" + fundingId + "'", null);
    }

    private void deleteAllFundings(){
        database.delete(DbHelper.DB_FUNDING_NAME, null, null);
    }

    private List<Funding> getFundings(){
        Cursor cursor = database.query(DbHelper.DB_FUNDING_NAME, DbHelper.COLUMNS_FUNDING, null, null, null, null, null, null);
        List<Funding> fundings = null;
        if (cursor.moveToFirst()){
            fundings = new ArrayList<>();
            do{
                Funding funding = new Funding();
                funding.setId(cursor.getString(0));
                funding.setUserId(cursor.getString(1));
                funding.setAmount(cursor.getDouble(2));
                funding.setFees(cursor.getDouble(3));
                funding.setDate(cursor.getLong(4));
                funding.setMobileNumber(cursor.getString(5));
                funding.setStatus(cursor.getInt(6)==1);
                funding.setStatus_code(cursor.getInt(7));
                funding.setMessage(cursor.getString(8));
                funding.setTransactionWcuUid(cursor.getString(9));
                funding.setTransactionWcuToken(cursor.getString(10));
            } while (cursor.moveToNext());
        }
        return fundings;
    }

    private boolean isFundingExists(String id){
        Cursor cursor = database.query(DbHelper.DB_FUNDING_NAME, DbHelper.COLUMNS_FUNDING, DbHelper.DB_FUNDING__ID + " = '" + id + "'", null, null, null, null, null);
        return cursor.moveToFirst();
    }


    private ContentValues transfertToContentvalues(Transfert transfert){
        ContentValues values = new ContentValues();
        values.put(DbHelper.DB_TRANSFERT__ID, transfert.getId());
        values.put(DbHelper.DB_TRANSFERT__SENDER, transfert.getSenderId());
        values.put(DbHelper.DB_TRANSFERT__RECEIVER, transfert.getReceiverId());
        values.put(DbHelper.DB_TRANSFERT__AMOUNT, transfert.getAmount());
        values.put(DbHelper.DB_TRANSFERT__FEES, transfert.getFees());
        values.put(DbHelper.DB_TRANSFERT__TRANSFERT_DATE, transfert.getDate());
        values.put(DbHelper.DB_TRANSFERT__STATUS, transfert.isStatus()?1:0);
        values.put(DbHelper.DB_TRANSFERT__STATUS_CODE, transfert.getStatus_code());
        values.put(DbHelper.DB_TRANSFERT__MESSAGE, transfert.getMessage());
        values.put(DbHelper.DB_TRANSFERT__RECEIVER_NAME, transfert.getReceiverName());
        values.put(DbHelper.DB_TRANSFERT__RECEIVER_MOBILE, transfert.getReceiverMobile());
        return values;
    }

    private void insertTransfert(Transfert transfert){
        ContentValues values = transfertToContentvalues(transfert);
        database.insert(DbHelper.DB_TRANSFERT_NAME, null, values);
    }

    private void updateTransfert(Transfert transfert){
        ContentValues values = transfertToContentvalues(transfert);
        database.update(DbHelper.DB_TRANSFERT_NAME, values, DbHelper.DB_TRANSFERT__ID + " = '" + transfert.getId() + "'", null);
    }

    private void deleteTransfert(String transfertId){
        database.delete(DbHelper.DB_TRANSFERT_NAME, DbHelper.DB_TRANSFERT__ID + " = '" + transfertId + "'", null);
    }

    private void deleteAllTransferts(){
        database.delete(DbHelper.DB_TRANSFERT_NAME, null, null);
    }

    private List<Transfert> getTransferts(){
        Cursor cursor = database.query(DbHelper.DB_TRANSFERT_NAME, DbHelper.COLUMNS_TRANSFERTS, null, null, null, null, null, null);
        List<Transfert> transferts = null;
        if (cursor.moveToFirst()){
            transferts = new ArrayList<>();
            do{
                Transfert transfert = new Transfert();
                transfert.setId(cursor.getString(0));
                transfert.setSenderId(cursor.getString(1));
                transfert.setReceiverId(cursor.getString(2));
                transfert.setAmount(cursor.getDouble(3));
                transfert.setFees(cursor.getDouble(4));
                transfert.setDate(cursor.getLong(5));
                transfert.setStatus(cursor.getInt(6)==1);
                transfert.setStatus_code(cursor.getInt(7));
                transfert.setMessage(cursor.getString(8));
                transfert.setReceiverName(cursor.getString(9));
                transfert.setReceiverMobile(cursor.getString(10));
            } while (cursor.moveToNext());
        }
        return transferts;
    }

    private boolean isTransfertExists(String id){
        Cursor cursor = database.query(DbHelper.DB_TRANSFERT_NAME, DbHelper.COLUMNS_TRANSFERTS, DbHelper.DB_TRANSFERT__ID + " = '" + id + "'", null, null, null, null, null);
        return cursor.moveToFirst();
    }


    private ContentValues paymentToContentvalues(Payment payment){
        ContentValues values = new ContentValues();
        values.put(DbHelper.DB_PAYMENT__ID, payment.getId());
        values.put(DbHelper.DB_PAYMENT__CUSTOMER, payment.getCustomerId());
        values.put(DbHelper.DB_PAYMENT__MERCHANT, payment.getMerchantId());
        values.put(DbHelper.DB_PAYMENT__AMOUNT, payment.getAmount());
        values.put(DbHelper.DB_PAYMENT__QR_CODE, payment.getQrCode());
        values.put(DbHelper.DB_PAYMENT__FEES, payment.getFees());
        values.put(DbHelper.DB_PAYMENT__PAYMENT_DATE, payment.getDate());
        values.put(DbHelper.DB_PAYMENT__STATUS, payment.isStatus()?1:0);
        values.put(DbHelper.DB_PAYMENT__STATUS_CODE, payment.getStatus_code());
        values.put(DbHelper.DB_PAYMENT__MESSAGE, payment.getMessage());
        values.put(DbHelper.DB_PAYMENT__MERCHANT_NAME, payment.getMerchantName());
        return values;
    }

    private void insertPayment(Payment payment){
        ContentValues values = paymentToContentvalues(payment);
        database.insert(DbHelper.DB_PAYMENT_NAME, null, values);
    }

    private void updatePayment(Payment payment){
        ContentValues values = paymentToContentvalues(payment);
        database.update(DbHelper.DB_PAYMENT_NAME, values, DbHelper.DB_PAYMENT__ID + " = '" + payment.getId() + "'", null);
    }

    private void deletePayment(String paymentId){
        database.delete(DbHelper.DB_PAYMENT_NAME, DbHelper.DB_PAYMENT__ID + " = '" + paymentId + "'", null);
    }

    private void deleteAllPayments(){
        database.delete(DbHelper.DB_PAYMENT_NAME, null, null);
    }

    private List<Payment> getPayments(){
        Cursor cursor = database.query(DbHelper.DB_PAYMENT_NAME, DbHelper.COLUMNS_PAYMENTS, null, null, null, null, null, null);
        List<Payment> payments = null;
        if (cursor.moveToFirst()){
            payments = new ArrayList<>();
            do{
                Payment payment = new Payment();
                payment.setId(cursor.getString(0));
                payment.setCustomerId(cursor.getString(1));
                payment.setMerchantId(cursor.getString(2));
                payment.setAmount(cursor.getDouble(3));
                payment.setQrCode(cursor.getString(4));
                payment.setFees(cursor.getDouble(5));
                payment.setDate(cursor.getLong(6));
                payment.setStatus(cursor.getInt(7)==1);
                payment.setStatus_code(cursor.getInt(8));
                payment.setMessage(cursor.getString(9));
                payment.setMerchantName(cursor.getString(10));
            } while (cursor.moveToNext());
        }
        return payments;
    }

    private boolean isPaymentExists(String id){
        Cursor cursor = database.query(DbHelper.DB_PAYMENT_NAME, DbHelper.COLUMNS_PAYMENTS, DbHelper.DB_PAYMENT__ID + " = '" + id + "'", null, null, null, null, null);
        return cursor.moveToFirst();
    }




    public void storeUser(User user){
        open();

        try{
            if (getUser()==null) insertUser(user);
            else updateUser(user);

            deleteUserMobiles();
            if (user.getMobiles()!=null) for (int i=0; i<user.getMobiles().size(); i++) insertUserMobile(user.getMobiles().get(i), user.getMobilesVerified().get(i));

            deleteUserContacts();
            if (user.getContacts()!=null) for (Contact contact: user.getContacts()) insertUserContact(contact);

            /*if (user.getWithdrawals()!=null) for (Withdrawal withdrawal: user.getWithdrawals()){
                if (!isWithdrawalExists(withdrawal.getId())) insertWithdrawal(withdrawal);
            }

            if (user.getFundings()!=null) for (Funding funding: user.getFundings()){
                if (!isFundingExists(funding.getId())) insertFunding(funding);
            }

            if (user.getTransferts()!=null) for (Transfert transfert: user.getTransferts()){
                if (!isTransfertExists(transfert.getId())) insertTransfert(transfert);
            }

            if (user.getPayments()!=null) for (Payment payment: user.getPayments()){
                if (!isPaymentExists(payment.getId())) insertPayment(payment);
            }*/

        } finally {
            close();
        }

    }

    public void storeTransactions(User user){
        open();
        try{
            if (user.getWithdrawals()!=null) for (Withdrawal withdrawal: user.getWithdrawals()){
                if (!isWithdrawalExists(withdrawal.getId())) insertWithdrawal(withdrawal);
            }

            if (user.getFundings()!=null) for (Funding funding: user.getFundings()){
                if (!isFundingExists(funding.getId())) insertFunding(funding);
            }

            if (user.getTransferts()!=null) for (Transfert transfert: user.getTransferts()){
                if (!isTransfertExists(transfert.getId())) insertTransfert(transfert);
            }

            if (user.getPayments()!=null) for (Payment payment: user.getPayments()){
                if (!isPaymentExists(payment.getId())) insertPayment(payment);
            }
        } finally {
            close();
        }
    }

    public User user(){
        open();
        User user = null;
        try{
            user = getUser();
            if (user==null) return null;
            List<Pair<String, Boolean>> mobiles = getUserMobiles();
            if (mobiles!=null){
                user.setMobiles(new ArrayList<String>());
                user.setMobilesVerified(new ArrayList<Boolean>());
                for (Pair<String, Boolean> pair: mobiles){
                    user.getMobiles().add(pair.first);
                    user.getMobilesVerified().add(pair.second);
                }
            }

            user.setContacts(getUserContacts());

            user.setWithdrawals(getWithdrawals());
            user.setFundings(getFundings());
            user.setTransferts(getTransferts());
            user.setPayments(getPayments());

        } finally {
            close();
        }
        return user;
    }

}

