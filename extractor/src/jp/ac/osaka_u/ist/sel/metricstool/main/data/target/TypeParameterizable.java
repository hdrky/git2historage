package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.List;


/**
 * �^�p�����[�^����`�\�ł��邱�Ƃ�\���C���^�[�t�F�[�X
 */
public interface TypeParameterizable {

    /**
     * �^�p�����[�^��`��ǉ�����
     * 
     * @param typeParameter �ǉ�����^�p�����[�^��`
     */
    void addTypeParameter(TypeParameterInfo typeParameter);

    /**
     * �����ŗ^����ꂽ�C���f�b�N�X�̌^�p�����[�^��Ԃ�
     * 
     * @param index �^�p�����[�^���w�肷�邽�߂̃C���f�b�N�X
     * @return �����ŗ^����ꂽ�C���f�b�N�X�̌^�p�����[�^
     */
    TypeParameterInfo getTypeParameter(int index);

    /**
     * �����ŗ^����ꂽ�^���������̃��j�b�g�Œ�`����Ă��邩��Ԃ�
     * @param typeParameter
     * @return
     */
    boolean isDefined(TypeParameterInfo typeParameter);
    
    /**
     * �^�p�����[�^�̃��X�g��Ԃ�
     * 
     * @return �^�p�����[�^�̃��X�g
     */
    List<TypeParameterInfo> getTypeParameters();

    /**
     * �O���ɂ���C�^�p�����[�^���`�\�ȃ��j�b�g��Ԃ��D
     * 
     * @return �O���ɂ���C�^�p�����[�^���`�\�ȃ��j�b�g
     */
    TypeParameterizable getOuterTypeParameterizableUnit();
}
