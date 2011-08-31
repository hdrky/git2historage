package jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricAlreadyRegisteredException;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFieldInfo;


/**
 * ���̃C���^�[�t�F�[�X�́C�t�B�[���h���g���N�X��o�^���邽�߂̃��\�b�h�Q��񋟂���D
 * 
 * @author higo
 * 
 */
public interface FieldMetricsRegister {

    /**
     * �������̃t�B�[���h�̃��g���N�X�l�i�������j��o�^����
     * 
     * @param fieldInfo ���g���N�X�̌v���Ώۃt�B�[���h
     * @param value ���g���N�X�l
     * @throws MetricAlreadyRegisteredException �o�^���悤�Ƃ��Ă��郁�g���N�X�����ɓo�^����Ă���ꍇ�ɃX���[�����
     */
    void registMetric(TargetFieldInfo fieldInfo, Number value)
            throws MetricAlreadyRegisteredException;
}
