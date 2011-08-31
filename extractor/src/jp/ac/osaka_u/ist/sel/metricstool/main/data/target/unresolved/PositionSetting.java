package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.Position;


/**
 * �t�@�C�����ł̈ʒu�����Z�b�g���邽�߂̃C���^�[�t�F�[�X
 * 
 * @author higo
 */
public interface PositionSetting extends Position {

    /**
     * �J�n�s���Z�b�g����
     * 
     * @param line �J�n�s
     */
    void setFromLine(int line);

    /**
     * �J�n����Z�b�g����
     * 
     * @param column �J�n��
     */
    void setFromColumn(int column);

    /**
     * �I���s���Z�b�g����
     * 
     * @param line �I���s
     */
    void setToLine(int line);

    /**
     * �I������Z�b�g����
     * 
     * @param column �I����
     */
    void setToColumn(int column);
}
