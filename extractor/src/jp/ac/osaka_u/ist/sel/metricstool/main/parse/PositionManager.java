package jp.ac.osaka_u.ist.sel.metricstool.main.parse;


/**
 * �C�ӂ̃I�u�W�F�N�g�Ɋւ��āC�J�n�s�C�J�n��C�I���s�C�I������Ǘ�����C���^�t�F�[�X.
 * @author kou-tngt
 *
 */
public interface PositionManager {

    /**
     * ����key�̊J�n�s��Ԃ�
     * @param key�@�J�n�s���擾�������v�f
     * @return�@�J�n�s
     */
    public int getStartLine(Object key);

    /**
     * ����key�̊J�n���Ԃ�
     * @param key�@�J�n����擾�������v�f
     * @return�@�J�n��
     */
    public int getStartColumn(Object key);

    /**
     * ����key�̏I���s��Ԃ�
     * @param key�@�I���s���擾�������v�f
     * @return�@�I���s
     */
    public int getEndLine(Object key);

    /**
     * ����key�̏I�����Ԃ�
     * @param key�@�I������擾�������v�f
     * @return�@�J�n��
     */
    public int getEndColumn(Object key);

    /**
     * ����key�̊J�n�s���Z�b�g����
     * @param key�@�J�n�s���Z�b�g����v�f
     * @param line�@�J�n�s
     */
    public void setStartLine(Object key, int line);

    /**
     * ����key�̊J�n����Z�b�g����
     * @param key�@�J�n����Z�b�g����v�f
     * @param column�@�J�n��
     */
    public void setStartColumn(Object key, int column);

    /**
     * ����key�̏I���s���Z�b�g����
     * @param key�@�I���s���Z�b�g����v�f
     * @param line�@�I���s
     */
    public void setEndLine(Object key, int line);

    /**
     * ����key�̏I������Z�b�g����
     * @param key�@�I������Z�b�g����v�f
     * @param column�@�I����
     */
    public void setEndColumn(Object key, int column);

    /**
     * ����key�̊J�n�s�C�J�n��C�I���s�C�I������Z�b�g����
     * @param key�@�����Z�b�g�������v�f
     * @param startLine �J�n�s
     * @param startColumn�@�J�n��
     * @param endLine�@�I���s
     * @param endColumn�@�I����
     */
    public void setPosition(Object key, int startLine, int startColumn, int endLine, int endColumn);

}