package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * ���O�����ł��Ȃ��^��\���N���X�D
 * 
 * @author higo
 * 
 */
@SuppressWarnings("serial")
public class UnknownTypeInfo implements TypeInfo {

    /**
     * ���̃N���X�̒P��I�u�W�F�N�g��Ԃ�
     * 
     * @return ���̃N���X�̒P��I�u�W�F�N�g
     */
    public static UnknownTypeInfo getInstance() {
        return SINGLETON;
    }

    /**
     * ���O�����ł��Ȃ��^�̖��O��Ԃ��D
     */
    public String getTypeName() {
        return UNKNOWN_STRING;
    }

    /**
     * ���������ǂ����̃`�F�b�N���s��
     */
    public boolean equals(final TypeInfo typeInfo) {

        if (null == typeInfo) {
            throw new NullPointerException();
        }

        return typeInfo instanceof UnknownTypeInfo;
    }

    /**
     * �O�̃N���X����A�N�Z�X�s�ɂ���
     */
    private UnknownTypeInfo() {
    }

    /**
     * �s���^�̌^����\���萔
     */
    public static final String UNKNOWN_STRING = "UNKNOWN";

    /**
     * ���̃N���X�̒P��I�u�W�F�N�g��ۑ����邽�߂̒萔
     */
    private static final UnknownTypeInfo SINGLETON = new UnknownTypeInfo();
}
