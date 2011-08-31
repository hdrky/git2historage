package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import java.util.EventObject;


/**
 * ��ԕω���\���C�x���g
 * 
 * @author kou-tngt
 *
 * @param <T>�@��ԕω��̃g���K�ƂȂ�v�f�̌^
 */
public class StateChangeEvent<T> extends EventObject {

    /**
     * ��ԕω��C�x���g�̎�ނ�\���}�[�J�[�C���^�t�F�[�X
     * 
     * @author kou-tngt
     *
     */
    public interface StateChangeEventType {
    }

    /**
     * ��ԕω��C�x���g�𔭍s�����\�[�X�C��ԕω��̎�ށC��ԕω��̃g���K�ƂȂ����v�f���w�肷��R���X�g���N�^.
     * 
     * @param source ��ԕω��C�x���g�𔭍s�����\�[�X
     * @param stateChangeType ��ԕω��̎��
     * @param trigger ��ԕω��̃g���K�ƂȂ����v�f
     * @throws NullPointerException source, stateChangeType, trigger �̂����ꂩ��null�������ꍇ
     */
    public StateChangeEvent(final StateManager source, final StateChangeEventType stateChangeType, final T trigger) {
        super(source);

        if (null == source) {
            throw new NullPointerException("source is null.");
        }
        if (null == stateChangeType) {
            throw new NullPointerException("stateChangeType is null.");
        }
        if (null == trigger) {
            throw new NullPointerException("trigger is null.");
        }

        this.source = source;
        this.stateChangeType = stateChangeType;
        this.trigger = trigger;
    }

    /**
     * �C�x���g�𔭍s�����\�[�X���擾����
     * @return �C�x���g�𔭍s�����\�[�X
     * @see java.util.EventObject#getSource()
     */
    @Override
    public StateManager getSource() {
        return this.source;
    }

    /**
     * �C�x���g�̎�ނ��擾����
     * @return�@�C�x���g�̎��
     */
    public StateChangeEventType getType() {
        return this.stateChangeType;
    }

    /**
     * �C�x���g�̃g���K�ƂȂ����v�f���擾����
     * @return�@�C�x���g�̃g���K�ƂȂ����v�f
     */
    public T getTrigger() {
        return this.trigger;
    }

    /**
     * �C�x���g�𔭍s�����\�[�X
     */
    private final StateManager source;

    /**
     * �C�x���g�̎��
     */
    private final StateChangeEventType stateChangeType;

    /**
     * �C�x���g�̃g���K�ƂȂ����v�f
     */
    private final T trigger;

}
