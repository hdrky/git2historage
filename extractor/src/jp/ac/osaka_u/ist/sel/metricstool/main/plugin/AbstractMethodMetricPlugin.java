package jp.ac.osaka_u.ist.sel.metricstool.main.plugin;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor.MethodInfoAccessor;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricAlreadyRegisteredException;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.METRIC_TYPE;

public abstract class AbstractMethodMetricPlugin extends AbstractPlugin {

    @Override
    protected void execute() {
        setupExecute();
        try {
            // �N���X���A�N�Z�T���擾
            final MethodInfoAccessor methodAccessor = this.getMethodInfoAccessor();

            // �i���񍐗p
            int measuredMethodCount = 0;
            final int maxMethodCount = methodAccessor.getMethodCount();

            // �S�N���X�ɂ���
            for (final TargetMethodInfo targetMethod : methodAccessor) {
                
                // �N���X�̃��g���N�X��o�^����
                registMethodMetric(targetMethod);

                // 1�N���X���Ƃ�%�Ői����
                this.reportProgress(++measuredMethodCount * 100 / maxMethodCount);
            }
        } finally {
            teardownExecute();
        }

    }
    
    /**
     * {@link #execute()} �̍ŏ��Ɏ��s����鏈��.
     * 
     * �K�v������΃I�[�o�[���C�h����.
     */
    protected void setupExecute() {
    }

    /**
     * {@link #execute()} �̍Ō�Ɏ��s����鏈��.
     * 
     * �K�v������΃I�[�o�[���C�h����.
     */
    protected void teardownExecute() {
    }
    
    /**
     * �N���X�̃��g���N�X���v�����ēo�^����.
     * 
     * {@link MetricAlreadyRegisteredException} �ɑ΂���W���̗�O������񋟂���.
     * �v���� {@link #measureClassMetric(TargetClassInfo)} ���I�[�o�[���C�h���Ď�������.
     * 
     * @param targetClass �Ώۂ̃N���X
     */
    protected void registMethodMetric(TargetMethodInfo targetMethod) {
        try {
            this.registMetric(targetMethod, measureMethodMetric(targetMethod));
        } catch (final MetricAlreadyRegisteredException e) {
            this.err.println(e);
        }
    }

    /**
     * ���\�b�h�̃��g���N�X���v������.
     * 
     * @param targetMethod �Ώۂ̃��\�b�h
     */
    abstract protected Number measureMethodMetric(TargetMethodInfo targetMethod);
    

    @Override
    protected METRIC_TYPE getMetricType() {
        return METRIC_TYPE.METHOD_METRIC;
    }

}
