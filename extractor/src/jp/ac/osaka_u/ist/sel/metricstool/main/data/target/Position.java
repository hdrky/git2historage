package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * �t�@�C�����ł̈ʒu��\�����߂̃C���^�[�t�F�[�X�D
 * 
 * @author higo
 */
public interface Position extends Comparable<Position>{

    /**
     * �J�n�s��Ԃ�
     * 
     * @return �J�n�s
     */
    int getFromLine();

    /**
     * �J�n���\��
     * 
     * @return �J�n��
     */
    int getFromColumn();

    /**
     * �I���s��Ԃ�
     * 
     * @return �I���s
     */
    int getToLine();

    /**
     * �I�����\��
     * 
     * @return �I����
     */
    int getToColumn();
}