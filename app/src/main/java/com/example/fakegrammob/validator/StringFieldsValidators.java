package com.example.fakegrammob.validator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static android.text.TextUtils.isEmpty;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringFieldsValidators {
    public static boolean isContainEmptyString(String... strings) {
        for (final String string : strings) {
            if (isEmpty(string)) {
                return true;
            }
        }
        return false;
    }

}
