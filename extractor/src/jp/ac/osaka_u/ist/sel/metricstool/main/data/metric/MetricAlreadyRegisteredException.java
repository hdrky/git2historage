package jp.ac.osaka_u.ist.sel.metricstool.main.data.metric;


/**
 * ���ɓo�^���Ă��郁�g���N�X�����ēx�o�^���悤�Ƃ����ꍇ�ɁC�X���[������O
 * 
 * @author higo 
 */
public class MetricAlreadyRegisteredException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -763049740565557645L;

    /**
     * 
     */
    public MetricAlreadyRegisteredException() {
        super();
    }

    /**
     * 
     * @param message
     * @param cause
     */
    public MetricAlreadyRegisteredException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 
     * @param message
     */
    public MetricAlreadyRegisteredException(String message) {
        super(message);
    }

    /**
     * 
     * @param cause
     */
    public MetricAlreadyRegisteredException(Throwable cause) {
        super(cause);
    }

}
