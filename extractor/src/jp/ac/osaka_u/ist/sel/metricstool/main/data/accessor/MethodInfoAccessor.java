package jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;


/**
 * ���̃C���^�[�t�F�[�X�́C���\�b�h�����擾���邽�߂̃��\�b�h�S��񋟂���D
 * 
 * @author higo
 *
 */
public interface MethodInfoAccessor extends Iterable<TargetMethodInfo> {

    /**
     * �Ώۃ��\�b�h�̂̐���Ԃ����\�b�h.
     * @return �Ώۃ��\�b�h�̐�
     */
    public int getMethodCount();
}
