package jp.ac.osaka_u.ist.sel.metricstool.main.data.metric;


/**
 * �o�^����Ă��Ȃ����g���N�X���ɃA�N�Z�X�����ꍇ�ɃX���[������O
 * 
 * @author higo
 *
 */
public class MetricNotRegisteredException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -5771740792340202250L;

    /**
     * 
     */
    public MetricNotRegisteredException() {
        super();
    }

    /**
     * 
     * @param message
     * @param cause
     */
    public MetricNotRegisteredException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 
     * @param message
     */
    public MetricNotRegisteredException(String message) {
        super(message);
    }

    /**
     * 
     * @param cause
     */
    public MetricNotRegisteredException(Throwable cause) {
        super(cause);
    }

}
