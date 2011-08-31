package jp.ac.osaka_u.ist.sel.metricstool.main.data.metric;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFieldInfo;


/**
 * �t�B�[���h���g���N�X��o�^���邽�߂̃f�[�^�N���X
 * 
 * @author higo
 * 
 */
public final class FieldMetricsInfo extends MetricsInfo<TargetFieldInfo> {

    /**
     * �v���Ώۃt�B�[���h��^���ď�����
     * 
     * @param fieldInfo �v���Ώۃt�B�[���h
     */
    public FieldMetricsInfo(final TargetFieldInfo fieldInfo) {
        super(fieldInfo);
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
