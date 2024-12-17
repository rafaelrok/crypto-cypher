package br.com.rafaelvieira.cryptocypher.logging;

import java.io.PrintWriter;
import java.io.Writer;

public class ExceptionWriter  extends PrintWriter {
    /**
     * Creates a new PrintWriter, without automatic line flushing.
     *
     * @param out A character-output stream
     */
    public ExceptionWriter(Writer out) {
        super(out);
    }

    private String wrapAroundWithNewlines(String stringWithoutNewlines) {
        return ("\n" + stringWithoutNewlines + "\n");
    }

    /*
     * Convert a stacktrace into a string
     */
    public String getExceptionAsString(Throwable throwable) {
        throwable.printStackTrace(this);

        String exception = super.out.toString();

        return (wrapAroundWithNewlines(exception));
    }
}
