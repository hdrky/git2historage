package jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFieldInfo;


/**
 * ���̃C���^�[�t�F�[�X�́C�t�B�[���h�����擾���邽�߂̃��\�b�h�S��񋟂���D
 * 
 * @author higo
 *
 */
public interface FieldInfoAccessor extends Iterable<TargetFieldInfo> {

    /**
     * �Ώۃt�B�[���h�̐���Ԃ����\�b�h.
     * @return �Ώۃt�B�[���h�̐�
     */
    public int getFieldCount();
}
