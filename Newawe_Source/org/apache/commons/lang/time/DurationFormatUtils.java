package org.apache.commons.lang.time;

import com.astuetz.pagerslidingtabstrip.C0302R;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import mf.org.apache.xml.serialize.OutputFormat.Defaults;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.http.HttpStatus;

public class DurationFormatUtils {
    static final Object f3298H;
    public static final String ISO_EXTENDED_FORMAT_PATTERN = "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'";
    static final Object f3299M;
    static final Object f3300S;
    static final Object f3301d;
    static final Object f3302m;
    static final Object f3303s;
    static final Object f3304y;

    static class Token {
        private int count;
        private Object value;

        static boolean containsTokenWithValue(Token[] tokens, Object value) {
            for (Token value2 : tokens) {
                if (value2.getValue() == value) {
                    return true;
                }
            }
            return false;
        }

        Token(Object value) {
            this.value = value;
            this.count = 1;
        }

        Token(Object value, int count) {
            this.value = value;
            this.count = count;
        }

        void increment() {
            this.count++;
        }

        int getCount() {
            return this.count;
        }

        Object getValue() {
            return this.value;
        }

        public boolean equals(Object obj2) {
            if (!(obj2 instanceof Token)) {
                return false;
            }
            Token tok2 = (Token) obj2;
            if (this.value.getClass() != tok2.value.getClass() || this.count != tok2.count) {
                return false;
            }
            if (this.value instanceof StringBuffer) {
                return this.value.toString().equals(tok2.value.toString());
            }
            if (this.value instanceof Number) {
                return this.value.equals(tok2.value);
            }
            if (this.value == tok2.value) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return this.value.hashCode();
        }

        public String toString() {
            return StringUtils.repeat(this.value.toString(), this.count);
        }
    }

    public static String formatDurationHMS(long durationMillis) {
        return formatDuration(durationMillis, "H:mm:ss.SSS");
    }

    public static String formatDurationISO(long durationMillis) {
        return formatDuration(durationMillis, ISO_EXTENDED_FORMAT_PATTERN, false);
    }

    public static String formatDuration(long durationMillis, String format) {
        return formatDuration(durationMillis, format, true);
    }

    public static String formatDuration(long durationMillis, String format, boolean padWithZeros) {
        Token[] tokens = lexx(format);
        int days = 0;
        int hours = 0;
        int minutes = 0;
        int seconds = 0;
        int milliseconds = 0;
        if (Token.containsTokenWithValue(tokens, f3301d)) {
            days = (int) (durationMillis / DateUtils.MILLIS_PER_DAY);
            durationMillis -= ((long) days) * DateUtils.MILLIS_PER_DAY;
        }
        if (Token.containsTokenWithValue(tokens, f3298H)) {
            hours = (int) (durationMillis / DateUtils.MILLIS_PER_HOUR);
            durationMillis -= ((long) hours) * DateUtils.MILLIS_PER_HOUR;
        }
        if (Token.containsTokenWithValue(tokens, f3302m)) {
            minutes = (int) (durationMillis / DateUtils.MILLIS_PER_MINUTE);
            durationMillis -= ((long) minutes) * DateUtils.MILLIS_PER_MINUTE;
        }
        if (Token.containsTokenWithValue(tokens, f3303s)) {
            seconds = (int) (durationMillis / 1000);
            durationMillis -= ((long) seconds) * 1000;
        }
        if (Token.containsTokenWithValue(tokens, f3300S)) {
            milliseconds = (int) durationMillis;
        }
        return format(tokens, 0, 0, days, hours, minutes, seconds, milliseconds, padWithZeros);
    }

    public static String formatDurationWords(long durationMillis, boolean suppressLeadingZeroElements, boolean suppressTrailingZeroElements) {
        String tmp;
        String duration = formatDuration(durationMillis, "d' days 'H' hours 'm' minutes 's' seconds'");
        if (suppressLeadingZeroElements) {
            duration = new StringBuffer().append(" ").append(duration).toString();
            tmp = StringUtils.replaceOnce(duration, " 0 days", StringUtils.EMPTY);
            if (tmp.length() != duration.length()) {
                duration = tmp;
                tmp = StringUtils.replaceOnce(duration, " 0 hours", StringUtils.EMPTY);
                if (tmp.length() != duration.length()) {
                    tmp = StringUtils.replaceOnce(tmp, " 0 minutes", StringUtils.EMPTY);
                    duration = tmp;
                    if (tmp.length() != duration.length()) {
                        duration = StringUtils.replaceOnce(tmp, " 0 seconds", StringUtils.EMPTY);
                    }
                }
            }
            if (duration.length() != 0) {
                duration = duration.substring(1);
            }
        }
        if (suppressTrailingZeroElements) {
            tmp = StringUtils.replaceOnce(duration, " 0 seconds", StringUtils.EMPTY);
            if (tmp.length() != duration.length()) {
                duration = tmp;
                tmp = StringUtils.replaceOnce(duration, " 0 minutes", StringUtils.EMPTY);
                if (tmp.length() != duration.length()) {
                    duration = tmp;
                    tmp = StringUtils.replaceOnce(duration, " 0 hours", StringUtils.EMPTY);
                    if (tmp.length() != duration.length()) {
                        duration = StringUtils.replaceOnce(tmp, " 0 days", StringUtils.EMPTY);
                    }
                }
            }
        }
        return StringUtils.replaceOnce(StringUtils.replaceOnce(StringUtils.replaceOnce(StringUtils.replaceOnce(new StringBuffer().append(" ").append(duration).toString(), " 1 seconds", " 1 second"), " 1 minutes", " 1 minute"), " 1 hours", " 1 hour"), " 1 days", " 1 day").trim();
    }

    public static String formatPeriodISO(long startMillis, long endMillis) {
        return formatPeriod(startMillis, endMillis, ISO_EXTENDED_FORMAT_PATTERN, false, TimeZone.getDefault());
    }

    public static String formatPeriod(long startMillis, long endMillis, String format) {
        return formatPeriod(startMillis, endMillis, format, true, TimeZone.getDefault());
    }

    public static String formatPeriod(long startMillis, long endMillis, String format, boolean padWithZeros, TimeZone timezone) {
        Token[] tokens = lexx(format);
        Calendar start = Calendar.getInstance(timezone);
        start.setTime(new Date(startMillis));
        Calendar end = Calendar.getInstance(timezone);
        end.setTime(new Date(endMillis));
        int milliseconds = end.get(14) - start.get(14);
        int seconds = end.get(13) - start.get(13);
        int minutes = end.get(12) - start.get(12);
        int hours = end.get(11) - start.get(11);
        int days = end.get(5) - start.get(5);
        int months = end.get(2) - start.get(2);
        int years = end.get(1) - start.get(1);
        while (milliseconds < 0) {
            milliseconds += DateUtils.MILLIS_IN_SECOND;
            seconds--;
        }
        while (seconds < 0) {
            seconds += 60;
            minutes--;
        }
        while (minutes < 0) {
            minutes += 60;
            hours--;
        }
        while (hours < 0) {
            hours += 24;
            days--;
        }
        if (Token.containsTokenWithValue(tokens, f3299M)) {
            while (days < 0) {
                days += start.getActualMaximum(5);
                months--;
                start.add(2, 1);
            }
            while (months < 0) {
                months += 12;
                years--;
            }
            if (!(Token.containsTokenWithValue(tokens, f3304y) || years == 0)) {
                while (years != 0) {
                    months += years * 12;
                    years = 0;
                }
            }
        } else {
            if (!Token.containsTokenWithValue(tokens, f3304y)) {
                int target = end.get(1);
                if (months < 0) {
                    target--;
                }
                while (start.get(1) != target) {
                    days += start.getActualMaximum(6) - start.get(6);
                    if ((start instanceof GregorianCalendar) && start.get(2) == 1 && start.get(5) == 29) {
                        days++;
                    }
                    start.add(1, 1);
                    days += start.get(6);
                }
                years = 0;
            }
            while (start.get(2) != end.get(2)) {
                days += start.getActualMaximum(5);
                start.add(2, 1);
            }
            months = 0;
            while (days < 0) {
                days += start.getActualMaximum(5);
                months--;
                start.add(2, 1);
            }
        }
        if (!Token.containsTokenWithValue(tokens, f3301d)) {
            hours += days * 24;
            days = 0;
        }
        if (!Token.containsTokenWithValue(tokens, f3298H)) {
            minutes += hours * 60;
            hours = 0;
        }
        if (!Token.containsTokenWithValue(tokens, f3302m)) {
            seconds += minutes * 60;
            minutes = 0;
        }
        if (!Token.containsTokenWithValue(tokens, f3303s)) {
            milliseconds += seconds * DateUtils.MILLIS_IN_SECOND;
            seconds = 0;
        }
        return format(tokens, years, months, days, hours, minutes, seconds, milliseconds, padWithZeros);
    }

    static String format(Token[] tokens, int years, int months, int days, int hours, int minutes, int seconds, int milliseconds, boolean padWithZeros) {
        StrBuilder buffer = new StrBuilder();
        boolean lastOutputSeconds = false;
        for (Token token : tokens) {
            Object value = token.getValue();
            int count = token.getCount();
            if (value instanceof StringBuffer) {
                buffer.append(value.toString());
            } else if (value == f3304y) {
                buffer.append(padWithZeros ? StringUtils.leftPad(Integer.toString(years), count, '0') : Integer.toString(years));
                lastOutputSeconds = false;
            } else if (value == f3299M) {
                buffer.append(padWithZeros ? StringUtils.leftPad(Integer.toString(months), count, '0') : Integer.toString(months));
                lastOutputSeconds = false;
            } else if (value == f3301d) {
                buffer.append(padWithZeros ? StringUtils.leftPad(Integer.toString(days), count, '0') : Integer.toString(days));
                lastOutputSeconds = false;
            } else if (value == f3298H) {
                buffer.append(padWithZeros ? StringUtils.leftPad(Integer.toString(hours), count, '0') : Integer.toString(hours));
                lastOutputSeconds = false;
            } else if (value == f3302m) {
                buffer.append(padWithZeros ? StringUtils.leftPad(Integer.toString(minutes), count, '0') : Integer.toString(minutes));
                lastOutputSeconds = false;
            } else if (value == f3303s) {
                buffer.append(padWithZeros ? StringUtils.leftPad(Integer.toString(seconds), count, '0') : Integer.toString(seconds));
                lastOutputSeconds = true;
            } else if (value == f3300S) {
                if (lastOutputSeconds) {
                    milliseconds += DateUtils.MILLIS_IN_SECOND;
                    buffer.append((padWithZeros ? StringUtils.leftPad(Integer.toString(milliseconds), count, '0') : Integer.toString(milliseconds)).substring(1));
                } else {
                    buffer.append(padWithZeros ? StringUtils.leftPad(Integer.toString(milliseconds), count, '0') : Integer.toString(milliseconds));
                }
                lastOutputSeconds = false;
            }
        }
        return buffer.toString();
    }

    static {
        f3304y = "y";
        f3299M = "M";
        f3301d = "d";
        f3298H = "H";
        f3302m = "m";
        f3303s = "s";
        f3300S = "S";
    }

    static Token[] lexx(String format) {
        char[] array = format.toCharArray();
        ArrayList list = new ArrayList(array.length);
        boolean inLiteral = false;
        StringBuffer buffer = null;
        Token previous = null;
        for (char ch : array) {
            if (!inLiteral || ch == '\'') {
                Object value = null;
                switch (ch) {
                    case Tokens.EXPRTOKEN_AXISNAME_FOLLOWING /*39*/:
                        if (!inLiteral) {
                            buffer = new StringBuffer();
                            list.add(new Token(buffer));
                            inLiteral = true;
                            break;
                        }
                        buffer = null;
                        inLiteral = false;
                        break;
                    case Defaults.LineWidth /*72*/:
                        value = f3298H;
                        break;
                    case C0302R.styleable.Theme_textAppearanceListItemSmall /*77*/:
                        value = f3299M;
                        break;
                    case C0302R.styleable.Theme_colorPrimaryDark /*83*/:
                        value = f3300S;
                        break;
                    case HttpStatus.SC_CONTINUE /*100*/:
                        value = f3301d;
                        break;
                    case C0302R.styleable.Theme_switchStyle /*109*/:
                        value = f3302m;
                        break;
                    case 's':
                        value = f3303s;
                        break;
                    case 'y':
                        value = f3304y;
                        break;
                    default:
                        if (buffer == null) {
                            buffer = new StringBuffer();
                            list.add(new Token(buffer));
                        }
                        buffer.append(ch);
                        break;
                }
                if (value != null) {
                    if (previous == null || previous.getValue() != value) {
                        Token token = new Token(value);
                        list.add(token);
                        previous = token;
                    } else {
                        previous.increment();
                    }
                    buffer = null;
                }
            } else {
                buffer.append(ch);
            }
        }
        return (Token[]) list.toArray(new Token[list.size()]);
    }
}
