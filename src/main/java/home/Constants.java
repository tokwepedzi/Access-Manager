package home;

public class Constants {

    /*-------------------------  SQL DATABASE TABLE NAMES  -----------------------------------*/
    public static String USERS_TABLE = "users_tb";
    public static String MEMBERSHIP_TABLE ="membership_tb";
    public static String PACKAGES_TABLE ="packages_tb";

    public static String SHORT_TERM_PACKAGES_TABLE ="shorttermpackages_tb";

    public static String SUBSCRIPTIONS_TABLE ="subscriptions_tb";
    public static String SECURITY_LOGS_TABLE ="securitylogs_tb";
    public static String OVERRIDE_REASONS_TABLE = "overridereasons_tb";
    public static String WEEKLY_MEMBERSHIP_TABLE ="weeklymembership_tb";
    public static String ACC_STATUS_ACTIVE ="ACTIVE";
    public static String ACC_STATUS_PAUSED ="PAUSED";
    public static String ACC_STATUS_SUSPENDED ="SUSPENDED";

    /*-------------------------------------  COLORS  -----------------------------------------*/
    public static String MY_COLOR_RED ="#C80815";
    public static String MY_COLOR_GREEN ="#228B22";



    public static String MEMBERSHIP_FORM_MAIN_HEADING_a ="MEMBERSHIP AGREEMENT BETWEEN ";
    public static String MEMBERSHIP_FORM_MAIN_HEADING_b ="(HERETO REFERRED TO AS THE COMPANY)";
    public static String PRINCIPAL_MEMBER_HEADING ="PRINCIPAL MEMBER (HERETO REFERRED TO AS THE MEMBER)";
    public static String SEC_MEMBER_HEADING ="SECONDARY MEMBERS";
    public static String MEMBERSHIP_DECLARATION_a ="I hereby instruct and authorise ";
    public static String MEMBERSHIP_DECLARATION_b ="to draw money from my bank account equivalent to the amount stipulated" +
            "for the membership. I request and authorise ";
    public static String MEMBERSHIP_DECLARATION_c = "to resubmit my debit order at a date set by the company in the event " +
            "that the debit order returns unpaid. The debit order will commence on the days agreed by";
    public static String MEMBERSHIP_DECLARATION_d = " I agree to pay any bank charges related to this debit order instruction. ";
    public static String TERMS_AND_CONDITIONS = "                                        See back side for Terms and Conditions                     ";
    public static int MAX_DAYS_IN_MONTH =31;
    public static String INDEMNITY_PARAGRAPH_1 = "I acknowledge that I am entering XPRESSIONS WELLNESS CENTRE facilities on my own accord and I agree " +
            "and acknowledge that XPRESSIONS WELLNESS CENTRE will not be held liable for any loss or death suffered " +
            "by me whatsoever including gross negligence while viewing the facilities.";

    public static String INDEMNITY_PARAGRAPH_2 ="\nI acknowledge that I am in good health to participate in the use of the facilities, and other activities that " +
            "XPRESSIONS WELLNESS CENTRE has to offer";

    public static String INDEMNITY_PARAGRAPH_3 ="\nI acknowledge the risk of physical injuries to myself and other gym users if I had not gone through the " +
            "proper introduction of the facilities.";
    public static String INDEMNITY_PARAGRAPH_4="\nI agree to be accompanied by a sales consultant when viewing the facilities";
    public static String INDEMNITY_PARAGRAPH_5="\nI agree and adhere to all rules and regulations set out by XPRESSIONS WELLNESS CENTRE.";

    public static String ALL_THE_LEGAL_STUFF_PARAGRAPH = "The member agrees that the agreement was fully completed by him/her and the information contained herein is true and correct " +
            "(The company reserves the right to cancel such contract if the information is deemed to be incorrect and or false). " +
            "The member is physically and mentally fit to normal exercise and acknowledge that the Company will not be held responsible for any loss or injury " +
            "suffered by him/her through negligence and/or omission on the part of the company and it's employees for any reason whatsoever. " +
            "In the event of a minor, his/her guardian assisting him/her consents to agree to the terms and conditions of this agreement. " +
            "This agreement contains the terms and conditions between the parties and no representation, variation or cancellation will be in force unless presented in writing and signed by both parties. The member agrees that he/she has read the terms and conditions in all its entirety. " +
            "Should the member pay by debit order this agreement will come into effect on the start date for the period described and will continue indefinitely. " +
            "unless terminated by the member on a clear ONE (1) month calender written notice of which member forfeits all joining fees ad card fees. "+"membership on or before or FIFTEEN (15) days after the expiry of their agreement and will not have to pay a joining fee again. " +
            "After which a joining fee is payable. In the event of non payment the Company reserves the right to cancel the membership and collect all monies due " +
            "in terms of this agreement including interest and collection fees.\n"+"The Company reserves the right to increase the membership within the minimum of TEN (10)% after the initial period is stipulated above. " +
            "The membership may be transferred to any person of choice off which all relevant transfer fees are payable at point of transfer. " +
            "The membership can be frozen for a minimum period of ONE (1) month and a maximum of SIX (6) Months of which a freezing fee is payable. " +
            "The membership may be terminated prior to the initial duration of the agreement of which a THIRTY (30)% cancellation fee is payable on cancellation. ";

    public static String RULES_AND_REGULATIONS_PARAGRAPH =" MEMBERSHIP CARDS \n * Membership cards are exclusive use for members only.\n * If a membership card is lost a new card will be issued at the prevailing price.\n * Membership cards are exclusive use for members only and may not be " +
            "transferred to another person. \n* Any person found in violation will be summarily be suspended or their contract terminated immediately.\n DRESS CODE \n " +
            "* Suitable clothing as well as training shoes must be worn at all times\nSWEAT TOWELS\n" +
            "* Members must use a sweat towel at all times while exercising\n and wipe the equipment after use\n" +
            "LOST PROPERTY\n" +
            "* Lost property will be donated to a charity if not collected in 14 days.\n" +
            "AGE RESTRICTIONS\n" +
            "* The age restriction of minors are between 12 Years and 18 Years.\n" +
            "* Minors need to be accompanied by an adult at all times.\n" +
            "* Minors are not permitted unless the parents are member\n VALUABLES\n" +
            "* The company will not be held liable for any loss, theft or damage to personal property of the member at the club.\n" +
            "CIRCUITS, CARDIOVASCULAR EQUIPMENT, STRENGTH EQUIPMENT\n" +
            "* Members must make sure that they are familiarised with the use of the equipment.\n" +
            "* Member must fill in a Physical Readiness Questionnaire(PRQ) before starting an exercise program.\n" +
            "MISCELLANEOUS\n" +
            "* No alcohol or smoking is permitted in the club.\n" +
            "* The illegal use of drugs including steroids is strictly prohibited in the club.\n" +
            "* No firearms are permitted in the club.\n" +
            "RIGHT OF ADMISSION RESERVED\n" +
            "* The club herby reserves the right to enter into or grant access to any person a membership to the club at its sole and absolute discretion\n\n" +
            "LIMITATIONS OF LIABILITIES\n" +
            " I agree and acknowledge that XPressions Wellness Centre will not be liable for death suffered by me,or guests " +
            "whatsoever including but not limited to. Any negligent (including gross negligent)act and or omission or breach of contract on the part " +
            "of XPressions Wellness Centre, its directors , employees, contractors, independent consultants or other members. I agree and acknowledge that" +
            " I enter upon and exit the premises (inclusive of the parking areas) and use the equipment and facilities entirely at my own risk. In addition, " +
            "I agree that XPressions Wellness Centre will not be liable for any loss or damage suffered by me and or other members as a result of theft on the" +
            " part of its employees, independent contractors, consultants or any other member. I and/or my estate hereby indemnify XPressions Wellness Centre" +
            " against any claim by any person arising directly or indirectly from my death, injury, loss or damage suffered by a member, allegedly caused or " +
            "contributed to an act or omission by XPressions Wellness Centre, its directors, employees, consultants and agents.";
    /*-------------------------------------  HOST PC NAME----------------------------------------*/

    //DESKTOP-PU2PGO7/Profile%20pictures)
   public static  String PATH_TO_PICS = "\\Gym Proctor\\Profile pictures\\";
    public static  String DEFAULT_USER_PIC = "user_icon.png";
}
