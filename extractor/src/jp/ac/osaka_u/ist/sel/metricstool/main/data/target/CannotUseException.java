package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * External�ȃ��j�b�g�ł͗��p�ł��Ȃ����\�b�h���Ăяo�����Ƃ��ɓ��������O
 * 
 * @author higo
 *
 */
public final class CannotUseException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 5029193932968214475L;

    /**
     * 
     */
    public CannotUseException() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * 
     * @param message
     * @param cause
     */
    public CannotUseException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * 
     * @param message
     */
    public CannotUseException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    /**
     * 
     * @param cause
     */
    public CannotUseException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }
}
