package jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;




/**
 * ���̃C���^�[�t�F�[�X�́C�t�@�C�������擾���邽�߂̃��\�b�h�S��񋟂���D
 * 
 * @author higo
 *
 */
public interface FileInfoAccessor extends Iterable<FileInfo>{

    /**
     * �Ώۃt�@�C���̐���Ԃ����\�b�h.
     * @return �Ώۃt�@�C���̐�
     */
    public int getFileCount();
}
