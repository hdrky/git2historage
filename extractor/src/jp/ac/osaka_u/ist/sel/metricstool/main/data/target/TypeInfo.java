package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.io.Serializable;


/**
 * �u�^�v��\���C���^�[�t�F�[�X�D
 * 
 * @author higo
 * 
 */
public interface TypeInfo extends Serializable {

    /**
     * �^����Ԃ�
     * 
     * @return �^��
     */
    String getTypeName();

    /**
     * �������̃`�F�b�N
     * 
     * @param typeInfo ��r�ΏƃI�u�W�F�N�g
     * @return �������ꍇ��true�C�����łȂ��ꍇ��false
     */
    boolean equals(TypeInfo typeInfo);

}
