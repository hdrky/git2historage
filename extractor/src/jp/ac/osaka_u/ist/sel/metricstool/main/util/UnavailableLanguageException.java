package jp.ac.osaka_u.ist.sel.metricstool.main.util;


/**
 * 
 * @author higo
 * 
 * ���p�\�łȂ��v���O���~���O���ꂪ�w�肳�ꂽ�ꍇ�ɗp������D
 * 
 */
public class UnavailableLanguageException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 834525430720739558L;

    public UnavailableLanguageException() {
        super();
    }

    public UnavailableLanguageException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnavailableLanguageException(String message) {
        super(message);
    }

    public UnavailableLanguageException(Throwable cause) {
        super(cause);
    }
}
