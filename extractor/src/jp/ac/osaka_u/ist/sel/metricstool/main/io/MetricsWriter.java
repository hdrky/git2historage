package jp.ac.osaka_u.ist.sel.metricstool.main.io;


/**
 * ���g���N�X�������o��j�N���X���������Ȃ���΂Ȃ�Ȃ��C���^�[�t�F�[�X
 * 
 * @author higo
 *
 */
public interface MetricsWriter {

    /**
     * ���g���N�X���t�@�C���ɏ����o��
     */
    public abstract void write();

    /**
     * ���g���N�X�l���Ȃ����Ƃ�\������
     */
    char NO_METRIC = '-';

}