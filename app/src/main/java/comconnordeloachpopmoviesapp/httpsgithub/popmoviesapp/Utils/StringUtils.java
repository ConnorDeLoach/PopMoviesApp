package comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Utility class for parsing and formatting Strings
 */
public final class StringUtils {

    private StringUtils() {
    }

    /**
     * Format String array in proper SQLite 'WHERE = ?' syntax
     *
     * @param data String array
     * @return formatted where arguments for sqlite database
     */
    public static String SQLiteWhereArgs(String[] data) {
        String args = "";
        for (String s : data) {
            args += s + ", ";
        }
        return args.substring(0, args.length() - 2);
    }

    /**
     * Parse and return a formatted movie release date
     *
     * @param date from moviesdb json
     * @return formatted date for sqlite database
     */
    public static String getEasyDate(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy", Locale.US);
        try {
            date = formatter.format(formatter.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getReviewFormat(String[] reviews) {
        String formattedReview = "";
        for (String string : reviews) {
            String author = string.substring(0, string.indexOf(":") + 1);
            String review = string.substring(string.indexOf(":") + 1, string.length());

            formattedReview += author + "\t" + review + "\n\n";
        }
        // Truncate last \n
        formattedReview = formattedReview.substring(0, formattedReview.length() - 2);

        return formattedReview;
    }
}
