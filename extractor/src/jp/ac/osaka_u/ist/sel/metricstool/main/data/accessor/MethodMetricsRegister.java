package jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricAlreadyRegisteredException;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;


/**
 * ���̃C���^�[�t�F�[�X�́C���\�b�h���g���N�X��o�^���邽�߂̃��\�b�h�Q��񋟂���D
 * 
 * @author higo
 * 
 */
public interface MethodMetricsRegister {

    /**
     * �������̃��\�b�h�̃��g���N�X�l�i�������j��o�^����
     * 
     * @param methodInfo ���g���N�X�̌v���Ώۃ��\�b�h
     * @param value ���g���N�X�l
     * @throws MetricAlreadyRegisteredException �o�^���悤�Ƃ��Ă��郁�g���N�X�����ɓo�^����Ă���ꍇ�ɃX���[�����
     */
    void registMetric(TargetMethodInfo methodInfo, Number value)
            throws MetricAlreadyRegisteredException;
}
