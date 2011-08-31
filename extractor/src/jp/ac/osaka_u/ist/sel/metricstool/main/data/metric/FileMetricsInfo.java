package jp.ac.osaka_u.ist.sel.metricstool.main.data.metric;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;


/**
 * �t�@�C�����g���N�X��o�^���邽�߂̃f�[�^�N���X
 * 
 * @author higo
 * 
 */
public final class FileMetricsInfo extends MetricsInfo<FileInfo> {

    /**
     * �v���Ώۃt�@�C����^���ď�����
     * 
     * @param fileInfo �v���Ώۃt�@�C��
     */
    public FileMetricsInfo(final FileInfo fileInfo) {
        super(fileInfo);
    }

    /**
     * ���b�Z�[�W�̑��M�Җ���Ԃ�
     * 
     * @return ���b�Z�[�W�̑��M�Җ�
     */
    public String getMessageSourceName() {
        return this.getClass().getName();
    }
}
