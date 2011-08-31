package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �^�p�����[�^��\�����ۃN���X
 * 
 * @author higo
 * 
 */
public class TypeParameterInfo {

    /**
     * �^�p�����[�^����^���ăI�u�W�F�N�g������������
     * 
     * @param ownerUnit �^�p�����[�^��錾���Ă��郆�j�b�g(�N���X or ���\�b�h)
     * @param name �^�p�����[�^��
     * @param index ���Ԗڂ̌^�p�����[�^����\��
     */
    public TypeParameterInfo(final TypeParameterizable ownerUnit, final String name, final int index) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == ownerUnit) || (null == name)) {
            throw new NullPointerException();
        }

        this.ownerUnit = ownerUnit;
        this.name = name;
        this.index = index;
        this.extendsTypes = new ArrayList<TypeInfo>();
    }

    /**
     * ���̌^�p�����[�^�������ŗ^����ꂽ�^�Ɠ��������ǂ����𔻒肷��
     * 
     * @param o ��r�Ώی^���
     * @return �������ꍇ�� true�C�������Ȃ��ꍇ�� false;
     */
    public boolean equals(final TypeInfo o) {

        if (null == o) {
            return false;
        }

        if (!(o instanceof TypeParameterInfo)) {
            return false;
        }

        return this.getName().equals(((TypeParameterInfo) o).getName());
    }

    /**
     * �^�p�����[�^��錾���Ă��郆�j�b�g(�N���X or ���\�b�h)��Ԃ�
     * 
     * @return  �^�p�����[�^��錾���Ă��郆�j�b�g(�N���X or ���\�b�h)
     */
    public final TypeParameterizable getOwnerUnit() {
        return this.ownerUnit;
    }

    /**
     * �^�p�����[�^����Ԃ�
     * 
     * @return �^�p�����[�^��
     */
    public final String getName() {
        return this.name;
    }

    /**
     * �^���i���ۂɂ͌^�p�����[�^���j��Ԃ�
     * 
     * @return �^��
     */
    public final String getTypeName() {

        StringBuilder sb = new StringBuilder(this.name);
        if (this.hasExtendsType()) {
            sb.append(" extends ");

            for (final TypeInfo extendsType : this.getExtendsTypes()) {
                sb.append(extendsType.getTypeName());
                sb.append(" & ");
            }

            sb.delete(sb.length() - 3, sb.length());
        }

        return sb.toString();
    }

    /**
     * �^�p�����[�^�̃C���f�b�N�X��Ԃ�
     * 
     * @return�@�^�p�����[�^�̃C���f�b�N�X
     */
    public final int getIndex() {
        return this.index;
    }

    /**
     * ���N���X�^��List��Ԃ�
     * 
     * @return ���N���X�^��List
     */
    public final List<TypeInfo> getExtendsTypes() {
        return Collections.unmodifiableList(this.extendsTypes);
    }

    public void addExtendsType(final TypeInfo extendsType) {

        if (null == extendsType) {
            throw new IllegalArgumentException();
        }

        this.extendsTypes.add(extendsType);
    }

    /**
     * * ���N���X�������ǂ�����Ԃ�
     * 
     * @return ���N���X�����ꍇ�� true,�����Ȃ��ꍇ�� false
     */
    public final boolean hasExtendsType() {
        return 0 != this.extendsTypes.size();
    }

    /**
     * �^�p�����[�^�����L���Ă��郆�j�b�g��ۑ����邽�߂̕ϐ�
     */
    private final TypeParameterizable ownerUnit;

    /**
     * �^�p�����[�^����ۑ����邽�߂̕ϐ�
     */
    private final String name;

    /**
     * ���̌^�p�����[�^�����Ԗڂ̂��̂���\���ϐ�
     */
    private final int index;

    /**
     * ���N���X�^��List��ۑ����邽�߂̕ϐ�
     */
    private List<TypeInfo> extendsTypes;
}
