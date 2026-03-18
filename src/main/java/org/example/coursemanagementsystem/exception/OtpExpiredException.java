package org.example.coursemanagementsystem.exception;

import static org.example.coursemanagementsystem.util.Constants.OTP_INVALID_OR_EXPIRED;

public class OtpExpiredException extends RuntimeException {

   public OtpExpiredException(){
       super(OTP_INVALID_OR_EXPIRED );
   }

    public OtpExpiredException(String message) {
        super(message);
    }
}
