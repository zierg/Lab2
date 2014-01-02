package logger;

import org.apache.log4j.Logger;

public class LoggerManager {
    public static final Logger TRACE_FILE_LOGGER = Logger.getLogger("TraceFileLogger");
    public static final Logger TRACE_CONSOLE_LOGGER = Logger.getLogger("TraceConsoleLogger");
    public static final Logger ERROR_FILE_LOGGER = Logger.getLogger("ErrorFileLogger");
    public static final Logger ERROR_CONSOLE_LOGGER = Logger.getLogger("ErrorConsoleLogger");
    
    private LoggerManager() {}
}
