package jp.ac.osaka_u.ist.sel.metricstool.main.io;



/**
 * �d����������Ă��Ȃ��ڑ����d�ɒ��낤�Ƃ������ɔ�������.
 * ��̓I�ɂ́C�i���񍐗p�̐ڑ��𓯂��v���O�C������Q��낤�Ƃ����ꍇ�Ȃǂł���.
 * @author kou-tngt
 *
 */
public class AlreadyConnectedException extends ConnectionException {

    /**
     * 
     */
    private static final long serialVersionUID = 7388323759983838646L;

    public AlreadyConnectedException() {
        super();
    }

    public AlreadyConnectedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyConnectedException(String message) {
        super(message);
    }

    public AlreadyConnectedException(Throwable cause) {
        super(cause);
    }

}
