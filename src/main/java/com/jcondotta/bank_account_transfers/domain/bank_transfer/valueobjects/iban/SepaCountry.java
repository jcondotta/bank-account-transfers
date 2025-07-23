package com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.iban;

public enum SepaCountry {
    AT, // Austria
    BE, // Belgium
    BG, // Bulgaria
    CH, // Switzerland
    CY, // Cyprus
    CZ, // Czech Republic
    DE, // Germany
    DK, // Denmark
    EE, // Estonia
    ES, // Spain
    FI, // Finland
    FR, // France
    GB, // United Kingdom
    GR, // Greece
    HR, // Croatia
    HU, // Hungary
    IE, // Ireland
    IS, // Iceland
    IT, // Italy
    LI, // Liechtenstein
    LT, // Lithuania
    LU, // Luxembourg
    LV, // Latvia
    MC, // Monaco
    MT, // Malta
    NL, // Netherlands
    NO, // Norway
    PL, // Poland
    PT, // Portugal
    RO, // Romania
    SE, // Sweden
    SI, // Slovenia
    SK, // Slovakia
    SM, // San Marino
    VA; // Vatican City

    public static boolean isSepaCountry(String countryCode) {
        try {
            SepaCountry.valueOf(countryCode);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
