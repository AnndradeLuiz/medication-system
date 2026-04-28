package com.luiz.medication_system.services.utils;

public class CnsValidator {

    public static boolean isValid(String cns) {
        if (cns == null || cns.trim().length() != 15) {
            return false;
        }
        char firstDigit = cns.charAt(0);
        if (firstDigit == '1' || firstDigit == '2') {
            return validateCnsStartingWith1Or2(cns);
        } else if (firstDigit == '7' || firstDigit == '8' || firstDigit == '9') {
            return validateCnsStartingWith789(cns);
        }
        return false;
    }

    private static boolean validateCnsStartingWith1Or2(String cns) {
        String pis = cns.substring(0, 11);
        int soma = 0;
        for (int i = 0; i < 11; i++) {
            soma += Integer.parseInt(pis.substring(i, i + 1)) * (15 - i);
        }
        int resto = soma % 11;
        int dv = 11 - resto;
        if (dv == 11) dv = 0;
        if (dv == 10) {
            soma += 2;
            resto = soma % 11;
            dv = 11 - resto;
            return cns.equals(pis + "001" + dv);
        } else {
            return cns.equals(pis + "000" + dv);
        }
    }

    private static boolean validateCnsStartingWith789(String cns) {
        int soma = 0;
        for (int i = 0; i < 15; i++) {
            soma += Integer.parseInt(cns.substring(i, i + 1)) * (15 - i);
        }
        return soma % 11 == 0;
    }

}
