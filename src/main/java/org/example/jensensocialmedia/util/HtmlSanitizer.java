package org.example.jensensocialmedia.util;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

public final class HtmlSanitizer {
    // Use Safelist.none() to strip all HTML, or Safelist.basic() / relaxed() to allow limited tags.
    private static final Safelist ALLOWED = Safelist.none();

    public static String sanitize(String input) {
        if (input == null) return null;
        // cleans and returns plain text (strips tags). Preserves text content.
        return Jsoup.clean(input, ALLOWED);
    }
}
