package jp.ac.osaka_u.ist.sel.metricstool.main.io;


/**
 * ���b�Z�[�W���o�͕��ɑ��邽�߂̃C���^�t�F�[�X
 * 
 * @author kou-tngt
 *
 */
public interface MessagePrinter {
    /**
     * ���b�Z�[�W�̎��
     * @author kou-tngt
     */
    public static enum MESSAGE_TYPE {
        /**
         * �W���o�͗p
         */
        OUT,

        /**
         * ���o�͗p
         */
        INFO,

        /**
         * �x���o�͗p
         */
        WARNING,

        /**
         * �G���[�o�͗p
         */
        ERROR
    };

    /**
     * ���b�Z�[�W�����̂܂܏o�͂���
     * @param o �o�͂��郁�b�Z�[�W
     */
    public void print(Object o);

    /**
     * ���s����
     */
    public void println();

    /**
     * ���b�Z�[�W���o�͂��ĉ��s����
     * @param o �o�͂��郁�b�Z�[�W
     */
    public void println(Object o);

    /**
     * �����s�̃��b�Z�[�W�̊ԂɁC���̃��b�Z�[�W�̊��荞�݂��Ȃ��悤�ɏo�͂���.
     * @param o �o�͂��郁�b�Z�[�W�̔z��
     */
    public void println(Object[] o);
}
