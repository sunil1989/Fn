package validation;

public class Validation {
	  public static final boolean isValidPhoneNumber(CharSequence target) {
	        if (target.length()!=12) {
	            return false;
	        } else {
	            return android.util.Patterns.PHONE.matcher(target).matches();
	        }
	    }
}
