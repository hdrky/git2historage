package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;


/**
 * void �^��\���N���X�D
 * 
 * @author higo
 * 
 */
@SuppressWarnings("serial")
public final class VoidTypeInfo implements TypeInfo, UnresolvedTypeInfo<VoidTypeInfo> {

    /**
     * ���̃N���X�̒P��I�u�W�F�N�g��Ԃ�
     * 
     * @return ���̃N���X�̒P��I�u�W�F�N�g
     */
    public static VoidTypeInfo getInstance() {
        return SINGLETON;
    }

    /**
     * void �^�̖��O��Ԃ��D
     */
    public String getTypeName() {
        return this.name;
    }

    /**
     * ���������ǂ����̃`�F�b�N���s��
     */
    public boolean equals(final TypeInfo typeInfo) {

        if (null == typeInfo) {
            throw new NullPointerException();
        }

        return typeInfo instanceof VoidTypeInfo;
    }

    /**
     * ���O��������Ă��邩�ǂ�����Ԃ�
     * 
     * @return ��� true ��Ԃ�
     */
    public boolean alreadyResolved() {
        return true;
    }

    /**
     * ���O�������ꂽ����Ԃ�
     * 
     * @return �������g��Ԃ�
     */
    public VoidTypeInfo getResolved() {
        return this;
    }

    /**
     * ������void�����������C�����ςݎQ�Ƃ�Ԃ��D
     * 
     * @param usingClass �������������̒�`���s���Ă���N���X
     * @param usingMethod �������������̒�`���s���Ă��郁�\�b�h
     * @param classInfoManager �p����N���X�}�l�[�W��
     * @param fieldInfoManager �p����t�B�[���h�}�l�[�W��
     * @param methodInfoManager �p���郁�\�b�h�}�l�[�W��
     * @return �����ς�void���
     */
    public VoidTypeInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {
        return this;
    }

    /**
     * void �^�̌^����\���萔
     */
    public static final String VOID_STRING = "void";

    /**
     * �����Ȃ��R���X�g���N�^
     */
    private VoidTypeInfo() {
        this.name = VOID_STRING;
    }

    /**
     * ���̃N���X�̒P��I�u�W�F�N�g��ۑ����邽�߂̒萔
     */
    private static final VoidTypeInfo SINGLETON = new VoidTypeInfo();

    /**
     * ���̌^�̖��O��ۑ����邽�߂̕ϐ�
     */
    private final String name;
}
