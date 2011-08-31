package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �^�p�����[�^�i�\�L�@���� <X super Y>�j��\���N���X
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public final class SuperTypeParameterInfo extends TypeParameterInfo {

    /**
     * �^�p�����[�^���C�h���N���X�^��^���ăI�u�W�F�N�g��������
     * 
     * @param ownerUnit ���̌^�p�����[�^�̏��L���j�b�g(�N���X or ���\�b�h)
     * @param name �^�p�����[�^��
     * @param index ���Ԗڂ̌^�p�����[�^����\��
     * @param extendsType ���N���X�^
     * @param superType �h���N���X�^
     */
    public SuperTypeParameterInfo(final TypeParameterizable ownerUnit, final String name,
            final int index, final TypeInfo extendsType, final TypeInfo superType) {

        super(ownerUnit, name, index);

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == superType) {
            throw new NullPointerException();
        }

        this.superType = superType;
    }

    /**
     * �h���N���X�^��Ԃ�
     * 
     * @return �h���N���X�^
     */
    public TypeInfo getSuperType() {
        return this.superType;
    }

    /**
     * �������h���N���X�^��ۑ�����
     */
    private final TypeInfo superType;
}
