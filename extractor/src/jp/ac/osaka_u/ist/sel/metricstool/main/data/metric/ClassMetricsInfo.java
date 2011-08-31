package jp.ac.osaka_u.ist.sel.metricstool.main.data.metric;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;


/**
 * �N���X���g���N�X��o�^���邽�߂̃f�[�^�N���X
 * 
 * @author higo
 * 
 */
public final class ClassMetricsInfo extends MetricsInfo<TargetClassInfo> {

    /**
     * �v���ΏۃN���X��^���ď�����
     * 
     * @param classInfo �v���ΏۃN���X
     */
    public ClassMetricsInfo(final TargetClassInfo classInfo) {
        super(classInfo);
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
