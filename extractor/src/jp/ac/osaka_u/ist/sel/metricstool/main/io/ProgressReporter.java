package jp.ac.osaka_u.ist.sel.metricstool.main.io;


/**
 * �i�����񍐗p�C���^�t�F�[�X
 * @author kou-tngt
 *
 */
public interface ProgressReporter {
    /**
     * �i����񑗐M�̏I����񍐂��郁�\�b�h
     */
    public void reportProgressEnd();

    /**
     * �i������񍐂��郁�\�b�h
     * @param percentage �i���l�i%�j
     * @throws IllegalArgumentException percentage��0-100�̊Ԃɓ����ĂȂ��ꍇ
     * @throws IllegalStateException percentage���O��񍐂����l��艺�������ꍇ
     */
    public void reportProgress(int percentage);
}
