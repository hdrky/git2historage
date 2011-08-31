package jp.ac.osaka_u.ist.sel.metricstool.main.io;


import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter.MESSAGE_TYPE;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.ConcurrentHashSet;


/**
 * ���M���ꂽ���b�Z�[�W�����X�i�[�ɑ���͂���N���X
 * 
 * ���b�Z�[�W�^�C�v���ɃC���X�^���X���쐬����.
 * 
 * @author kou-tngt
 *
 */
public class MessagePool {

    /**
     * �^�C�v���Ƃ̃C���X�^���X��Ԃ����\�b�h
     * @param type �擾����C���X�^���X�̃^�C�v
     * @return type�ɑΉ�����C���X�^���X
     * @throws IllegalArgumentException type�p�̃C���X�^���X��������Ȃ������ꍇ
     */
    public static MessagePool getInstance(final MESSAGE_TYPE type) {
        for (final MessagePool instance : INSTANCES) {
            if (type == instance.getMessageType()) {
                return instance;
            }
        }
        //���b�Z�[�W�^�C�v���ɃC���X�^���X���p�ӂ��Ă���͂��Ȃ̂ŁC�����ɗ���̂͂��肦�Ȃ�
        assert (false) : "Illegal state : unknown message type " + type.name() + " is found.";

        throw new IllegalArgumentException("unknown message type " + type.name());
    }

    /**
     * ���X�i�[��ǉ�����
     * @param listener �ǉ����������X�i�[
     * @throws NullPointerException listener��null�̏ꍇ
     */
    public void addMessageListener(final MessageListener listener) {
        if (null == listener) {
            throw new NullPointerException("listner is null.");
        }
        synchronized (this) {
            this.listeners.add(listener);
        }
    }

    /**
     * ���̃C���X�^���X���Ή����郁�b�Z�[�W�^�C�v��Ԃ�
     * @return ���b�Z�[�W�^�C�v
     */
    public MESSAGE_TYPE getMessageType() {
        return this.messageType;
    }

    /**
     * ���X�i�[���폜����
     * @param listener �폜���郊�X�i�[
     */
    public void removeMessageListener(final MessageListener listener) {
        if (null != listener) {
            synchronized (this) {
                this.listeners.remove(listener);
            }
        }
    }

    /**
     * ���b�Z�[�W�𑗐M���郁�\�b�h
     * @param source ���b�Z�[�W���M��
     * @param message ���b�Z�[�W
     * @throws NullPointerException source�܂���message��null�̏ꍇ
     */
    public void sendMessage(final MessageSource source, final String message) {
        if (null == message) {
            throw new NullPointerException("message is null.");
        }
        if (null == source) {
            throw new NullPointerException("source is null.");
        }

        this.fireMessage(new MessageEvent(source, this.messageType, message));
    }

    /**
     * ���b�Z�[�W�C�x���g�����X�i�[�ɑ��M���郁�\�b�h
     * @param event ���M����C�x���g
     * @throws NullPointerException event��null�̏ꍇ
     */
    private void fireMessage(final MessageEvent event) {
        if (null == event) {
            throw new NullPointerException("event is null");
        }

        synchronized (this) {
            for (final MessageListener listener : this.listeners) {
                listener.messageReceived(event);
            }
        }
    }

    /**
     * ���b�Z�[�W�^�C�v�ɑΉ�����C���X�^���X���쐬����private�R���X�g���N�^
     * @param type
     */
    private MessagePool(final MESSAGE_TYPE type) {
        this.messageType = type;
    }

    /**
     * ���̃C���X�^���X�̃��b�Z�[�W�^�C�v
     */
    private final MESSAGE_TYPE messageType;

    /**
     * �o�^����Ă��郁�b�Z�[�W���X�i
     */
    private final Set<MessageListener> listeners = new ConcurrentHashSet<MessageListener>();

    /**
     * �C���X�^���X�Q
     */
    private static final MessagePool[] INSTANCES;

    static {
        //���b�Z�[�W�^�C�v���ɃC���X�^���X���쐬����
        final MESSAGE_TYPE[] types = MESSAGE_TYPE.values();
        final int size = types.length;
        INSTANCES = new MessagePool[size];
        for (int i = 0; i < size; i++) {
            INSTANCES[i] = new MessagePool(types[i]);
        }
    }

}
