package cm.kamix.app.localstorage;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cm.kamix.app.models.Funding;
import cm.kamix.app.models.Payment;
import cm.kamix.app.models.Transfert;
import cm.kamix.app.models.User;
import cm.kamix.app.models.Withdrawal;

public class DbHelper extends SQLiteOpenHelper{

    public static final int DB_VERSION = 2;
    public static final String DB_NAME = "kamix_local_db";

    public static final String DB_USERS_NAME = User.class.getSimpleName().toLowerCase()+"s";
    public static final String DB_USERS__ID = "id";
    public static final String DB_USERS__FIRSTNAME = "firstname";
    public static final String DB_USERS__LASTNAME = "lastname";
    public static final String DB_USERS__EMAIL = "email";
    public static final String DB_USERS__EMAIL_VERIFIED = "email_verified";
    public static final String DB_USERS__SECRET_CODE = "secret_code";
    public static final String DB_USERS__ADDRESS = "address";
    public static final String DB_USERS__ID_DOCUMENT = "id_document";
    public static final String DB_USERS__PHOTO = "photo";
    public static final String DB_USERS__BIRTHDATE = "birthdate";
    public static final String DB_USERS__REGISTRATION_DATE = "registration_date";
    public static final String DB_USERS__ACTIVATION_DATE = "activation_date";
    public static final String DB_USERS__BALANCE = "balance";
    public static final String DB_USERS__CURRENCY = "currency";
    public static final String DB_USERS__COUNTRY_CODE = "country_code";
    public static final String DB_USERS__COUNTRY = "country";
    public static final String DB_USERS__CITY = "city";
    public static final String DB_USERS__FIDELITY_POINTS = "fidelity_points";
    public static final String DB_USERS__STATUS = "status";
    public static final String[] COLUMNS_DB_USERS = {DB_USERS__ID, DB_USERS__FIRSTNAME, DB_USERS__LASTNAME, DB_USERS__EMAIL, DB_USERS__EMAIL_VERIFIED,
            DB_USERS__SECRET_CODE, DB_USERS__ADDRESS, DB_USERS__ID_DOCUMENT, DB_USERS__PHOTO, DB_USERS__BIRTHDATE, DB_USERS__REGISTRATION_DATE,
            DB_USERS__ACTIVATION_DATE, DB_USERS__BALANCE, DB_USERS__CURRENCY, DB_USERS__COUNTRY_CODE, DB_USERS__COUNTRY, DB_USERS__CITY,
            DB_USERS__FIDELITY_POINTS, DB_USERS__STATUS};
    public static final String CREATE_USERS  = "CREATE TABLE IF NOT EXISTS " + DB_USERS_NAME + " (" +
            DB_USERS__ID + " TEXT, " +
            DB_USERS__FIRSTNAME + " TEXT, " +
            DB_USERS__LASTNAME + " TEXT, " +
            DB_USERS__EMAIL + " TEXT, " +
            DB_USERS__EMAIL_VERIFIED + " INT, " +
            DB_USERS__SECRET_CODE + " TEXT, " +
            DB_USERS__ADDRESS + " TEXT, " +
            DB_USERS__ID_DOCUMENT + " TEXT, " +
            DB_USERS__PHOTO + " TEXT, " +
            DB_USERS__BIRTHDATE + " TEXT, " +
            DB_USERS__REGISTRATION_DATE + " TEXT, " +
            DB_USERS__ACTIVATION_DATE + " TEXT, " +
            DB_USERS__BALANCE + " REAL, " +
            DB_USERS__CURRENCY + " TEXT, " +
            DB_USERS__COUNTRY_CODE + " TEXT, " +
            DB_USERS__COUNTRY + " TEXT, " +
            DB_USERS__CITY + " TEXT, " +
            DB_USERS__FIDELITY_POINTS + " REAL, " +
            DB_USERS__STATUS + " INT" +
            ")";

    public static final String DB_MOBILES_NAME = "mobiles";
    public static final String DB_MOBILES__MOBILE = "mobile";
    public static final String DB_MOBILES__VERIFIED = "verified";
    public static final String[] COLUMNS_DB_MOBILES = {DB_MOBILES__MOBILE, DB_MOBILES__VERIFIED};
    public static final String CREATE_MOBILES = "CREATE TABLE IF NOT EXISTS " + DB_MOBILES_NAME + " (" +
            DB_MOBILES__MOBILE + " TEXT, " +
            DB_MOBILES__VERIFIED + " INT" +
            ")";

    public static final String DB_CONTACTS_NAME = "contacts";
    public static final String DB_CONTACTS__MOBILE = "mobile";
    public static final String DB_CONTACTS__NAME = "name";
    public static final String[] COLUMNS_DB_CONTACTS = {DB_CONTACTS__MOBILE, DB_CONTACTS__NAME};
    public static final String CREATE_CONTACTS = "CREATE TABLE IF NOT EXISTS " + DB_CONTACTS_NAME + " (" +
            DB_CONTACTS__MOBILE + " TEXT, " +
            DB_CONTACTS__NAME + " TEXT" +
            ")";

    public static final String DB_WITHDRAWAL_NAME = Withdrawal.class.getSimpleName().toLowerCase()+"s";
    public static final String DB_WITHDRAWAL__ID = "id";
    public static final String DB_WITHDRAWAL__USER = "user";
    public static final String DB_WITHDRAWAL__AMOUNT = "amount";
    public static final String DB_WITHDRAWAL__FEES = "fees";
    public static final String DB_WITHDRAWAL__WITHDRAWAL_DATE = "withdrawal_date";
    public static final String DB_WITHDRAWAL__MOBILE_NUMBER = "mobile_number";
    public static final String DB_WITHDRAWAL__STATUS = "status";
    public static final String DB_WITHDRAWAL__STATUS_CODE = "status_code";
    public static final String DB_WITHDRAWAL__MESSAGE = "message";
    public static final String DB_WITHDRAWAL__TRANSACTION_WCU_UID = "transaction_wcu_uid";
    public static final String DB_WITHDRAWAL__TRANSACTION_WCU_TOKEN = "transaction_wcu_token";
    public static final String[] COLUMNS_WITHDRAWALS = {DB_WITHDRAWAL__ID, DB_WITHDRAWAL__USER, DB_WITHDRAWAL__AMOUNT, DB_WITHDRAWAL__FEES,
            DB_WITHDRAWAL__WITHDRAWAL_DATE, DB_WITHDRAWAL__MOBILE_NUMBER, DB_WITHDRAWAL__STATUS, DB_WITHDRAWAL__STATUS_CODE,
            DB_WITHDRAWAL__MESSAGE, DB_WITHDRAWAL__TRANSACTION_WCU_UID, DB_WITHDRAWAL__TRANSACTION_WCU_TOKEN};
    public static final String CREATE_WITHDRAWALS = "CREATE TABLE IF NOT EXISTS " + DB_WITHDRAWAL_NAME + " (" +
            DB_WITHDRAWAL__ID + " TEXT, " +
            DB_WITHDRAWAL__USER + " TEXT, " +
            DB_WITHDRAWAL__AMOUNT + " REAL, " +
            DB_WITHDRAWAL__FEES + " REAL, " +
            DB_WITHDRAWAL__WITHDRAWAL_DATE + " TEXT, " +
            DB_WITHDRAWAL__MOBILE_NUMBER + " TEXT, " +
            DB_WITHDRAWAL__STATUS + " INT, " +
            DB_WITHDRAWAL__STATUS_CODE + " INT, " +
            DB_WITHDRAWAL__MESSAGE + " TEXT, " +
            DB_WITHDRAWAL__TRANSACTION_WCU_UID + " TEXT, " +
            DB_WITHDRAWAL__TRANSACTION_WCU_TOKEN + " TEXT" +
            ")";


    public static final String DB_FUNDING_NAME = Funding.class.getSimpleName().toLowerCase()+"s";
    public static final String DB_FUNDING__ID = "id";
    public static final String DB_FUNDING__USER = "user";
    public static final String DB_FUNDING__AMOUNT = "amount";
    public static final String DB_FUNDING__FEES = "fees";
    public static final String DB_FUNDING__FUNDING_DATE = "funding_date";
    public static final String DB_FUNDING__MOBILE_NUMBER = "mobile_number";
    public static final String DB_FUNDING__STATUS = "status";
    public static final String DB_FUNDING__STATUS_CODE = "status_code";
    public static final String DB_FUNDING__MESSAGE = "message";
    public static final String DB_FUNDING__TRANSACTION_WCU_UID = "transaction_wcu_uid";
    public static final String DB_FUNDING__TRANSACTION_WCU_TOKEN = "transaction_wcu_token";
    public static final String[] COLUMNS_FUNDING = {DB_FUNDING__ID, DB_FUNDING__USER, DB_FUNDING__AMOUNT, DB_FUNDING__FEES,
            DB_FUNDING__FUNDING_DATE, DB_FUNDING__MOBILE_NUMBER, DB_FUNDING__STATUS, DB_FUNDING__STATUS_CODE,
            DB_FUNDING__MESSAGE, DB_FUNDING__TRANSACTION_WCU_UID, DB_FUNDING__TRANSACTION_WCU_TOKEN};
    public static final String CREATE_FUNDINGS = "CREATE TABLE IF NOT EXISTS " + DB_FUNDING_NAME + " (" +
            DB_FUNDING__ID + " TEXT, " +
            DB_FUNDING__USER + " TEXT, " +
            DB_FUNDING__AMOUNT + " REAL, " +
            DB_FUNDING__FEES + " REAL, " +
            DB_FUNDING__FUNDING_DATE + " TEXT, " +
            DB_FUNDING__MOBILE_NUMBER + " TEXT, " +
            DB_FUNDING__STATUS + " INT, " +
            DB_FUNDING__STATUS_CODE + " INT, " +
            DB_FUNDING__MESSAGE + " TEXT, " +
            DB_FUNDING__TRANSACTION_WCU_UID + " TEXT, " +
            DB_FUNDING__TRANSACTION_WCU_TOKEN + " TEXT" +
            ")";

    public static final String DB_TRANSFERT_NAME = Transfert.class.getSimpleName().toLowerCase()+"s";
    public static final String DB_TRANSFERT__ID = "id";
    public static final String DB_TRANSFERT__SENDER = "sender";
    public static final String DB_TRANSFERT__RECEIVER = "receiver";
    public static final String DB_TRANSFERT__AMOUNT = "amount";
    public static final String DB_TRANSFERT__FEES = "fees";
    public static final String DB_TRANSFERT__TRANSFERT_DATE = "withdrawal_date";
    public static final String DB_TRANSFERT__STATUS = "status";
    public static final String DB_TRANSFERT__STATUS_CODE = "status_code";
    public static final String DB_TRANSFERT__MESSAGE = "message";
    public static final String DB_TRANSFERT__RECEIVER_NAME = "receiver_name";
    public static final String DB_TRANSFERT__RECEIVER_MOBILE = "receiver_mobile";
    public static final String[] COLUMNS_TRANSFERTS = {DB_TRANSFERT__ID, DB_TRANSFERT__SENDER, DB_TRANSFERT__RECEIVER,  DB_TRANSFERT__AMOUNT,
            DB_TRANSFERT__FEES, DB_TRANSFERT__TRANSFERT_DATE, DB_TRANSFERT__STATUS, DB_TRANSFERT__STATUS_CODE, DB_TRANSFERT__MESSAGE, DB_TRANSFERT__RECEIVER_NAME, DB_TRANSFERT__RECEIVER_MOBILE};
    public static final String CREATE_TRANSFERTS = "CREATE TABLE IF NOT EXISTS " + DB_TRANSFERT_NAME + " (" +
            DB_TRANSFERT__ID + " TEXT, " +
            DB_TRANSFERT__SENDER + " TEXT, " +
            DB_TRANSFERT__RECEIVER + " TEXT, " +
            DB_TRANSFERT__AMOUNT + " REAL, " +
            DB_TRANSFERT__FEES + " REAL, " +
            DB_TRANSFERT__TRANSFERT_DATE + " TEXT, " +
            DB_TRANSFERT__STATUS + " INT, " +
            DB_TRANSFERT__STATUS_CODE + " INT, " +
            DB_TRANSFERT__MESSAGE + " TEXT, " +
            DB_TRANSFERT__RECEIVER_NAME + " TEXT, " +
            DB_TRANSFERT__RECEIVER_MOBILE + " TEXT" +
            ")";

    public static final String DB_PAYMENT_NAME = Payment.class.getSimpleName().toLowerCase()+"s";
    public static final String DB_PAYMENT__ID = "id";
    public static final String DB_PAYMENT__CUSTOMER = "customer";
    public static final String DB_PAYMENT__MERCHANT = "merchant";
    public static final String DB_PAYMENT__AMOUNT = "amount";
    public static final String DB_PAYMENT__QR_CODE = "qr_code";
    public static final String DB_PAYMENT__FEES = "fees";
    public static final String DB_PAYMENT__PAYMENT_DATE = "withdrawal_date";
    public static final String DB_PAYMENT__STATUS = "status";
    public static final String DB_PAYMENT__STATUS_CODE = "status_code";
    public static final String DB_PAYMENT__MESSAGE = "message";
    public static final String DB_PAYMENT__MERCHANT_NAME = "merchant_name";
    public static final String[] COLUMNS_PAYMENTS = {DB_PAYMENT__ID, DB_PAYMENT__CUSTOMER, DB_PAYMENT__MERCHANT,  DB_PAYMENT__AMOUNT, DB_PAYMENT__QR_CODE,
            DB_PAYMENT__FEES, DB_PAYMENT__PAYMENT_DATE, DB_PAYMENT__STATUS, DB_PAYMENT__STATUS_CODE, DB_PAYMENT__MESSAGE, DB_PAYMENT__MERCHANT_NAME};
    public static final String CREATE_PAYMENTS = "CREATE TABLE IF NOT EXISTS " + DB_PAYMENT_NAME + " (" +
            DB_PAYMENT__ID + " TEXT, " +
            DB_PAYMENT__CUSTOMER + " TEXT, " +
            DB_PAYMENT__MERCHANT + " TEXT, " +
            DB_PAYMENT__AMOUNT + " REAL, " +
            DB_PAYMENT__QR_CODE + " TEXT, " +
            DB_PAYMENT__FEES + " REAL, " +
            DB_PAYMENT__PAYMENT_DATE + " TEXT, " +
            DB_PAYMENT__STATUS + " INT, " +
            DB_PAYMENT__STATUS_CODE + " INT, " +
            DB_PAYMENT__MESSAGE + " TEXT, " +
            DB_PAYMENT__MERCHANT_NAME + " TEXT" +
            ")";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_USERS);
        sqLiteDatabase.execSQL(CREATE_CONTACTS);
        sqLiteDatabase.execSQL(CREATE_MOBILES);
        sqLiteDatabase.execSQL(CREATE_WITHDRAWALS);
        sqLiteDatabase.execSQL(CREATE_FUNDINGS);
        sqLiteDatabase.execSQL(CREATE_TRANSFERTS);
        sqLiteDatabase.execSQL(CREATE_PAYMENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DB_USERS_NAME);
        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DB_CONTACTS_NAME);
        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DB_MOBILES_NAME);
        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DB_WITHDRAWAL_NAME);
        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DB_FUNDING_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DB_TRANSFERT_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DB_PAYMENT_NAME);
        onCreate(sqLiteDatabase);
    }
}
