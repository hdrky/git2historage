package jp.ac.osaka_u.ist.sel.metricstool.main.io;


import java.util.EventObject;

import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter.MESSAGE_TYPE;


/**
 * ���b�Z�[�W�C�x���g�N���X
 * 
 * @author kou-tngt
 *
 */
public class MessageEvent extends EventObject {

    /**
     * 
     */
    private static final long serialVersionUID = -4711363868655969016L;

    /**
     * �R���X�g���N�^
     * @param source ���b�Z�[�W���M��
     * @param messageType ���b�Z�[�W�̎��
     * @param message ���b�Z�[�W
     */
    public MessageEvent(final MessageSource source, final MESSAGE_TYPE messageType,
            final String message) {
        super(source);
        this.source = source;
        this.message = message;
        this.messageType = messageType;
    }

    /**
     * ���b�Z�[�W���擾���郁�\�b�h
     * @return ���b�Z�[�W
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * ���b�Z�[�W�̎�ނ��擾���郁�\�b�h
     * @return ���b�Z�[�W�̎��
     */
    public MESSAGE_TYPE getMessageType() {
        return this.messageType;
    }

    /**
     * ���b�Z�[�W���M�҂��擾���郁�\�b�h
     * @return ���b�Z�[�W���M��
     * @see java.util.EventObject#getSource()
     */
    @Override
    public MessageSource getSource() {
        return this.source;
    }

    /**
     * ���b�Z�[�W���M��
     */
    private final MessageSource source;

    /**
     * ���b�Z�[�W�̎��
     */
    private final MESSAGE_TYPE messageType;

    /**
     * ���b�Z�[�W
     */
    private final String message;

}
