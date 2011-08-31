package jp.ac.osaka_u.ist.sel.metricstool.main.data.metric;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;


/**
 * ���\�b�h���g���N�X��o�^���邽�߂̃f�[�^�N���X
 * 
 * @author higo
 * 
 */
public final class MethodMetricsInfo extends MetricsInfo<TargetMethodInfo> {

    /**
     * �v���ΏۃI�u�W�F�N�g��^���ď�����
     * 
     * @param method �v���Ώۃ��\�b�h
     */
    public MethodMetricsInfo(final TargetMethodInfo method) {
        super(method);
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
