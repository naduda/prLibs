package ua.pr.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class MyFormatter extends Formatter {	 
	private static final DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    public String format(LogRecord record) {
        StringBuilder builder = new StringBuilder(1000);
        builder.append(df.format(new Date(record.getMillis()))).append(" ");
        builder.append(ToolsPrLib.fixedLenthString(record.getLevel().getName(), 10));
        if (record.getLevel().equals(Level.INFO)) {
        	String info = formatMessage(record);
        	while (info.length() > 71) {
        		builder.append(ToolsPrLib.fixedLenthString(info, 71)).append("\n");
        		builder.append(ToolsPrLib.fixedLenthString("", 35));
        		info = info.substring(71);
			}
        	builder.append(ToolsPrLib.fixedLenthString(info, 71));
		} else {
			builder.append(formatMessage(record));
			builder.append("     [").append(record.getSourceClassName()).append("] ");
		}        
        builder.append("\n");
        return builder.toString();
    }
}
