package com.ziffytech.Config;

/*
 * Created by  on 10/18/16.
 * All end points for api is given here....
 *
 * Please find api code in  application/controllers/api.php  file here.. all function releated end points is given..
 * /index.php/api/login   mean  public function login() is for login.
 */

// 9527239199

public class ApiParams
{


 /************************Thyro API******************************/
 public static String Thyrocare = "thyrocare_api";

 public  static String GET_THYRO_5_PACKAGES = ConstValue.BASE_URL+Thyrocare+"/get_popular_package";
 public  static String GETALLTHYROPACK = ConstValue.BASE_URL+Thyrocare+"/all_thyrocare_packages";
 public static String PACK_DETAIL = ConstValue.BASE_URL+Thyrocare+"/package_details";
 public static String GETTIMESLOT = ConstValue.BASE_URL+Thyrocare+"/get_time_slot";

 /*****************Cart Api Thyro**************************/

 public static String ADDTOCART = ConstValue.BASE_URL+Thyrocare+"/add_to_card";
 public static String REMOVETOCART = ConstValue.BASE_URL+Thyrocare+"/remove_card";
 public static String REMOVEFROMCART = ConstValue.BASE_URL+Thyrocare+"/remove_card";
 public static String CARTDETAIL = ConstValue.BASE_URL+Thyrocare+"/card_details";
 public static String BILLINGDETAIL = ConstValue.BASE_URL+Thyrocare+"/billing_details_api";

 public static final String VER_CODE ="https://www.ziffytech.com/admin/api/version_app" ;


 // https://www.ziffytech.com/ZiffyDummy/admin//index.php/Api/my_appointments
 //https://www.ziffytech.com/ZiffyDummy/admin/index.php/Api/login

 public static String PARM_RESPONCE = "responce";
 public static String PARM_DATA = "data";
 public static String PARM_ERROR = "error";
 public static String API_V = "Api";
 // public  static  String CATEGORY_LIST = ConstValue.BASE_URL+"/index.php/"+API_V+"/get_categories";
 public  static String BUSINESS_LIST = ConstValue.BASE_URL+"/index.php/"+API_V+"/get_business";
 public static String BUSINESS_SERVICES = ConstValue.BASE_URL+"/index.php/"+API_V+"/get_services";
 public static String BUSINESS_PHOTOS = ConstValue.BASE_URL+"/index.php/"+API_V+"/get_photos";
 public static String BUSINESS_REVIEWS = ConstValue.BASE_URL+"/index.php/"+API_V+"/get_reviews";
 public static String ADD_BUSINESS_REVIEWS = ConstValue.BASE_URL+"/index.php/"+API_V+"/add_business_review";

 public static String TIME_SLOT_URL = ConstValue.BASE_URL+API_V+"/get_time_slot";
 public static String GET_LOCALITY = ConstValue.BASE_URL+API_V+"/get_all_doctors";
 public static String DOCTOR_DAYS_URL = ConstValue.BASE_URL+API_V+"/get_doctor_schedule";
 public static String PAYMENT_URL = ConstValue.BASE_URL+"/index.php/payorder/paypal";
 public  static String GET_DOCTORS_BY_NAME = ConstValue.BASE_URL+API_V+"/get_doctors_by_name";
 // public  static String GET_DOCTORS_BY_FILTER = ConstValue.BASE_URL+API_V+"/filter_doctor";
 public  static String GET_DOCTORS_BY_FILTER = ConstValue.BASE_URL+"api_n/filter_doctor";

 public static final String GET_PRESCRIBED_MEDICINE =ConstValue.BASE_PHARMACY+"Pharmacy_api/get_prescription_details" ;

 public  static String GET_OTP_SMS = ConstValue.BASE_URL+API_V+"/app_smsziffy";

 public  static String GET_CHAT_USER_LIST = ConstValue.BASE_URL+API_V+"/get_book_doctor_list";
 public  static String GET_ALL_DOCTORS = ConstValue.BASE_URL+API_V+"/get_all_doctors";
 public static String GET_DOCTORS = ConstValue.BASE_URL+API_V+"/get_doctor_by_categories";

 /*public static  String LAB_URL = ConstValue.BASE_URL+"Lab_api/get_lab_all";*/
 public static String LAB_URL = ConstValue.BASE_URL+"Lab_api/filter_lab_Latlong";

 public static String GET_PROMOCODE= ConstValue.BASE_URL+API_V+"/get_promo_code";
 public static String APPLY_PROMO_CODE= ConstValue.BASE_URL+API_V+"/select_promocode";

 /*https://www.ziffytech.com/ZiffyDummy/admin/Lab_api/filter_lab_Latlong*/
 public  static String ADD_FRIEND = ConstValue.BASE_URL+"Chat/add_chat_friend";
 public  static String ADD_MEMBER = ConstValue.BASE_URL+API_V+"/add_user_member";
 public  static String BOOK_LAB = ConstValue.BASE_URL+"Lab_api/"+"add_appointment_for_lab";
 public  static String GET_ALL_TESTS = ConstValue.BASE_URL+"/Lab_api/get_tests_all";
 public  static String GET_ALL_LABS = ConstValue.BASE_URL+"/index.php/Lab_api/get_lab_all";
 public  static String GET_PACKAGES = ConstValue.BASE_URL+"/Lab_api/get_package_all";
 public  static String GET_TRANS_HISTORY = ConstValue.BASE_URL+API_V+"/get_transaction_history";
 public  static String GET_EVENT_HEALTH_CHECKUP = ConstValue.BASE_URL+API_V+"/event_health_freecheck";
 public  static String GET_LABS= ConstValue.BASE_URL+"/Lab_api/filter_lab";
 public static String LOGIN_URL = ConstValue.BASE_URL+API_V+"/login";
 //public static String REGISTER_URL = ConstValue.BASE_URL+"/index.php/"+API_V+"/signupnow";
 public static String REGISTER_URL = ConstValue.BASE_URL+"/index.php/"+API_V+"/signup";
 public static String BOOKAPPOINTMENT_URL = ConstValue.BASE_URL+"/index.php/"+API_V+"/add_appointment";
 public static String CHANGE_PASSWORD_URL = ConstValue.BASE_URL+"/index.php/"+API_V+"/change_password";
 public static String FORGOT_PASSWORD_URL = ConstValue.BASE_URL+"/index.php/"+API_V+"/forgot_password_mail_for_user";
 public static String USERDATA_URL = ConstValue.BASE_URL+"/index.php/"+API_V+"/get_userdata";
 public static String UPDATEPROFILE_URL = ConstValue.BASE_URL+"/index.php/"+API_V+"/update_profile";
 // public static String MYAPPOINTMENTS_URL = ConstValue.BASE_URL+"/index.php/"+API_V+"/my_appointments";

 public static String MYAPPOINTMENTS_URL = ConstValue.BASE_URL+"api_n/my_appointments";
 //   public static String PRESCRIPTION_URL_APP = ConstValue.BASE_PHARMACY+"admin/onlineprescription/medication_history_list_api/";

 public static String CANCELAPPOINTMENTS_URL = ConstValue.BASE_URL+"/index.php/"+API_V+"/cancel_appointment";
 public static String REGISTER_FCM_URL = ConstValue.BASE_URL+"/index.php/"+API_V+"/register_fcm";
 public static String ORDER_MEDICINE = ConstValue.BASE_URL+"Api/add_user_medicine";
 public static String SOCIAL_REGISTER = ConstValue.BASE_URL+"/index.php/"+API_V+"/socialRegister";
 public static String RATE_DOCTOR = ConstValue.BASE_URL+"/index.php/"+API_V+"/add_doctor_review";
 public static String DELETE_MEDICATION = ConstValue.BASE_URL+"/index.php/"+API_V+"/delete_medicine_record";
 public static String SOCIAL_REGISTER1 = ConstValue.BASE_URL+"Api/socialRegister";
 public static String FACEBOOK_LOGIN = ConstValue.BASE_URL+"api/facebook_login";
 public static String User_Details = ConstValue.BASE_URL+"Api/update_user_info";
 public static String AUTOSRCHDOCTOER = ConstValue.BASE_URL+"Api/autosearch_doctor";
 public static String AddMember = ConstValue.BASE_URL+"Api/add_user_member";
 public static String autosrch = ConstValue.BASE_URL+"Api/autosearch";
 public static String get_member = ConstValue.BASE_URL+"Api/get_member";
 public static String delete_member = ConstValue.BASE_URL+"Api/delete_member";
 public static String GET_DOCTOR_QUES = ConstValue.BASE_URL+"Api/get_doctor_previous_reply";
 public static String GET_MY_QUES = ConstValue.BASE_URL+"Api/get_patient_previous_reply";
 public static String GET_DOCTOR_REPLY = ConstValue.BASE_URL+"Api/get_patient_query1";
 public  static String PERSON_DETAIL_URL = ConstValue.BASE_URL+API_V+"/update_personal_details";
 public  static String Medical_DETAIL_URL = ConstValue.BASE_URL+API_V+"/update_medical_details";
 public  static String LIFESTYLE_DETAIL_URL = ConstValue.BASE_URL+API_V+"/update_lifestyle_details";
 public  static String BOOKING_URL = ConstValue.BASE_URL+API_V+"/add_appointment";
 public  static String VACCINATION_DETAILS = ConstValue.BASE_URL+"Lab_api"+"/get_vaccination_details";

 public  static String GET_ALL_REPORTS = ConstValue.BASE_URL+"Lab_api"+"/medication_detial_for_patient_api";
 public  static String GET_TEST_REPORTS = ConstValue.BASE_URL+"Lab_api"+"/blood_report_list_for_apinew";
 // http://13.127.253.100:90/admin/Lab_api/blood_report_list_for_apinew


 /****************************************ONLINE PHARMACY*********************************************/
 public  static String TEST_APP_CANCEL= ConstValue.BASE_URL+"/api_n/cancel_lab_test_appointment";
 public static String GET_MEDICAL_HISTORY=ConstValue.BASE_PHARMACY+"Pharmacy_api/list_of_prescription";
 public static String GET_PRESCRIPTION_WEBVIEW=ConstValue.BASE_PHARMACY+"onlineprescription/updateView_for_api/";
 public static String MEDICINE_ORDER = ConstValue.BASE_PHARMACY+"Pharmacy_api/order_medicine_api";
 public static String MEDICINE_HISTORY = ConstValue.BASE_PHARMACY+"Pharmacy_api/order_medicine_history";


 /**PATIENT CONSULT API****/

 /* https://www.ziffytech.com/admin/api/facebook_login */

 public static String GET_QUES = ConstValue.BASE_URL+"Api/patient_question_list";
 public static String ADD_QUES = ConstValue.BASE_URL+"Api/post_patient_question";
 public static String GET_REPLIES = ConstValue.BASE_URL+"Api/get_patient_replies";
 public static String ADD_REPLY = ConstValue.BASE_URL+"Api/patient_reply_question";
 public  static String MAIN_CATEGORY_LIST = ConstValue.BASE_URL+"Api/get_main_categories";
 public  static String CATEGORY_LIST = ConstValue.BASE_URL+"/index.php/"+API_V+"/get_sub_categories";
 public static String MEDICATION_ADD = ConstValue.BASE_URL+"Api/add_medicine_record";
 public static String GET_MEDICATION_ALL = ConstValue.BASE_URL+"Api/get_medicine_record";
 public  static String PREF_NAME = "hairkut.pref";
 public static String PREF_CATEGORY = "pref_category";
 public static String COMMON_KEY = "user_id";
 //public static String PRICE_CART = "price_cart";
 public static String PREF_ERROR = "error_stack";
 public static String USER_DATA = "user_data";

 /************************PAYUMONEY API******************************/
 public static String SURL = ConstValue.BASE_URL+"payment/payment_responce";
 public static String FURL = ConstValue.BASE_URL+"payment/payment_responce";
 public static String GET_HASH = ConstValue.BASE_URL+"payment/generate_hash";

 public static String USER_SALUTATION = "salutation";
 public static String USER_FULLNAME = "user_fullname";
 public static String USER_LOCATION = "location";
 public static String USER_EMAIL = "user_email";
 public static String USER_PHONE = "user_phone";
 public static String USER_DOB = "dob";
 public static String USER_BLOOD_GROUP = "blood_group";
 public static String USER_GENDER = "gender";
 public static String USER_MARITAL_STATUS = "marital_status";
 public static String USER_HEIGHT = "height";
 public static String USER_WEIGHT = "weight";
 public static String USER_EMERGENCY_CONTACT = "emergency_contact";
 public static String PINCODE = "pin_code";
 public static String ADDRESS = "Address";
 public static String CITY = "User_city";
 public static String Time = "time";
 public static String SOCIAL_ID = "Social_id";
 public static String SOCIAL_TYPE = "Social_Type";
 public static String IMAGE = "Imager";
 public static String Name = "Name";
 public static String CONTACT = "contact";
 public static String BIRTHDAY = "birthday";
 public static String WEIGHT = "WEIGHT";
 public static String HEIGHT = "HEIGHT";
 public static String GENDER = "GENDER";
 public static String MARITAL_STATUS = "MARITAL_STATUS";
 public static String EMERGENCY_CONTACT = "EMERGENCY_CONTACT";
 public static String LOCALITY = "MARITAL_STATUS";
 public static String BLOOD_GROUP = "BLOOD_GROUP";
 public static String ALERGIES = "ALERGIES";
 public static String SURGERY = "SURGERY";
 public static String C_MEDICATION = "C_MEDICATION";
 public static String P_MEDICATION = "P_MEDICATION";
 public static String INJURIES = "INJURIES";
 public static String CHRONIC = "CHRONIC";

 public static String VERSION = "patient_ver_code";

 public static String CURRENT_CITY = "city";

 public static String USER_JSON_DATA = "user_data";

 public static String ZIFFY_WALLET_AMT="ziffy_wallet_amt";

 public static String CHAT_WITH = "chat_with";

 public static String GET_PHARMACY = "CHRONIC";
 public static String PHARMA_NOTIFICATION = "";
 public static String LAB_NOTIFICATION = "";

 public static String GET_CONTACTS = ConstValue.BASE_URL+API_V+"/get_emergency_number";
 public  static String GET_CITY = ConstValue.BASE_URL+API_V+"/get_city";
 public  static String GET_REMAINDERS = ConstValue.BASE_URL+API_V+"/get_remainder_prescription";




 /*https://www.ziffytech.com/admin/Api/get_emergency_number";*/
}
//https://www.ziffytech.com/ZiffyDummy/admin/Api/get_emergency_number