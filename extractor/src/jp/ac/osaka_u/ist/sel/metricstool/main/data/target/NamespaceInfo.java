package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.io.Serializable;
import java.util.Arrays;

import jp.ac.osaka_u.ist.sel.metricstool.main.util.StringArrayComparator;


/**
 * ���O��Ԗ���\���N���X
 * 
 * @author higo
 */
@SuppressWarnings("serial")
public final class NamespaceInfo implements Comparable<NamespaceInfo>, Serializable {

    /**
     * ���O��ԃI�u�W�F�N�g������������D���O��Ԗ����^�����Ȃ���΂Ȃ�Ȃ��D
     * 
     * @param name
     */
    public NamespaceInfo(final String[] name) {

        if (null == name) {
            throw new IllegalArgumentException();
        }

        this.name = Arrays.<String>copyOf(name, name.length);
    }

    /**
     * ���O��Ԗ��̏������`���郁�\�b�h�D���݂͖��O��Ԃ�\�� String �N���X�� compareTo ��p���Ă���D
     * 
     * @param namespace ��r�Ώۖ��O��Ԗ�
     * @return ���O��Ԃ̏���
     */
    public int compareTo(final NamespaceInfo namespace) {

        if (null == namespace) {
            throw new NullPointerException();
        }

        return StringArrayComparator.SINGLETON.compare(this.getName(), namespace.getName());
    }

    /**
     * ���O��Ԃ̔�r���s���D�������ꍇ�� true�C�����łȂ��ꍇ false ��Ԃ�
     */
    @Override
    public boolean equals(final Object o) {

        if (null == o) {
            throw new NullPointerException();
        }

        if (!(o instanceof NamespaceInfo)) {
            return false;
        }

        // ���O��Ԃ̒����Ŕ�r
        final String[] name = this.getName();
        final String[] correspondName = ((NamespaceInfo) o).getName();
        if (name.length != correspondName.length) {
            return false;
        }

        // �e�v�f���ʂɔ�r
        for (int i = 0; i < name.length; i++) {
            if (!name[i].equals(correspondName[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * ���O��Ԃ̔�r���s��
     * 
     * @param namespace ��r�Ώۂ�String[] �i���O��Ԃ�\��String�̔z��j
     * @return �������ꍇ��true,�����łȂ��ꍇ��false
     */
    public boolean equals(final String[] namespace) {

        // ���O��Ԃ̒����Ŕ�r
        final String[] name = this.getName();
        if (name.length != namespace.length) {
            return false;
        }

        // �e�v�f���ʂɔ�r
        for (int i = 0; i < name.length; i++) {
            if (!name[i].equals(namespace[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * ���O��Ԗ���Ԃ�
     * 
     * @return ���O��Ԗ�
     */
    public String[] getName() {
        return Arrays.<String>copyOf(this.name, this.name.length);
    }

    /**
     * �s���Ȗ��O��Ԗ���\���萔
     */
    public final static NamespaceInfo UNKNOWN = new NamespaceInfo(new String[] { "unknown" });

    /**
     * ���O��Ԗ���Ԃ�
     * 
     * @param delimiter ���O�̋�؂蕶��
     * @return ���O��Ԃ��Ȃ��� String
     */
    public String getName(final String delimiter) {

        if (null == delimiter) {
            throw new NullPointerException();
        }

        String[] names = this.getName();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < names.length; i++) {
            buffer.append(names[i]);
            buffer.append(delimiter);
        }

        return buffer.toString();

    }

    /**
     * ���̖��O��Ԗ��̃n�b�V���R�[�h��Ԃ�
     * 
     * @return ���̖��O��Ԗ��̃n�b�V���R�[�h
     */
    @Override
    public int hashCode() {

        final String[] namespace = this.getName();
        int hash = 0;
        for (int i = 0; i < namespace.length; i++) {
            hash += namespace[i].hashCode();
        }

        return hash;
    }

    /**
     * ���O��Ԃ�\���ϐ�
     */
    private final String[] name;

}
