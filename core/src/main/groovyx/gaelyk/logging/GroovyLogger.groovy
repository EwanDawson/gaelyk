/*
 * Copyright 2009-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package groovyx.gaelyk.logging

import java.util.logging.*

/**
 * Simple wrapper for <code>java.util.logging.Logger</code>.
 * This logger can log correct class/method names even if used in Groovy.
 * <p>
 * Implementation idea found <a href="http://d.hatena.ne.jp/ksky/20100517/p1">here</a>.
 *
 * @author ksky@jggug.org (original implementation)
 * @author Guillaume Laforge (minor groovyfications and comments)
 */
class GroovyLogger {
    /** list of packages we're not interested in */
    static final EXCLUDE_LIST = [
            // ignore the GroovyLogger class itself
            GroovyLogger.class.name,
            // ignore the Groovy classes
            "groovy.", "org.codehaus.groovy.", "gjdk.groovy.",
            // ignore the JDK classes
            "java.", "javax.", "sun.",
            // Ignore Google App Engine logs
            "com.google.",
    ]

    /** The underlying logger used for logging */
    Logger logger

    GroovyLogger(String name) {
        logger = Logger.getLogger(name)
    }


     /**
      * @return the name of the logger 
      */
    String getName() { logger.name }

    /**
     * Log a message
     *
     * @param level the level of logging
     * @param msg the message to log
     */
    void log(Level level, String msg) {
        def stack = Thread.currentThread().stackTrace
        // find the proper caller class and method
        def caller = stack.find { elem ->
            EXCLUDE_LIST.every { !elem.className.startsWith(it) }
        }
        logger.logp(level, logger.name, caller.methodName, msg)
    }

    void severe (String msg) { log(Level.SEVERE,  msg) }
    void warning(String msg) { log(Level.WARNING, msg) }
    void info   (String msg) { log(Level.INFO,    msg) }
    void config (String msg) { log(Level.CONFIG,  msg) }
    void fine   (String msg) { log(Level.FINE,    msg) }
    void finer  (String msg) { log(Level.FINER,   msg) }
    void finest (String msg) { log(Level.FINEST,  msg) }
}