package server;

import logger.LoggerManager;

public class ServerLogger {
    private ServerLogger() {}
    
    public static void trace(Object message) {
        LoggerManager.TRACE_FILE_LOGGER.trace(message);
        LoggerManager.TRACE_CONSOLE_LOGGER.trace(message);
    }
    
    public static void error(Object message) {
        LoggerManager.ERROR_FILE_LOGGER.error(message);
    }
}
