package jp.ac.osaka_u.ist.sel.metricstool.main.plugin;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor.ClassInfoAccessor;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricAlreadyRegisteredException;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.LanguageUtil;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.METRIC_TYPE;


/**
 * �N���X�̃��g���N�X���v������v���O�C�������p�̒��ۃN���X.
 * 
 * {@link AbstractPlugin} �̈ꕔ�̃��\�b�h���I�[�o�[���C�h����Ă���.
 * 
 * ���̃N���X���p������N���X�� {@link #measureClassMetric(TargetClassInfo)} ����������K�v������. 
 * �K�v������� {@link #setupExecute()}, {@link #teardownExecute()} ���I�[�o�[���C�h����.
 * 
 * @author rniitani
 */
public abstract class AbstractClassMetricPlugin extends AbstractPlugin {

    public AbstractClassMetricPlugin() {
        super();
    }

    /**
     * �N���X���Ƀ��g���N�X���v������.
     * 
     * @see #registClassMetric(TargetClassInfo)
     */
    @Override
    protected void execute() {

        setupExecute();
        try {
            // �N���X���A�N�Z�T���擾
            final ClassInfoAccessor classAccessor = this.getClassInfoAccessor();

            // �i���񍐗p
            int measuredClassCount = 0;
            final int maxClassCount = classAccessor.getClassCount();

            // �S�N���X�ɂ���
            for (final TargetClassInfo targetClass : classAccessor) {

                // �N���X�̃��g���N�X��o�^����
                registClassMetric(targetClass);

                // 1�N���X���Ƃ�%�Ői����
                this.reportProgress(++measuredClassCount * 100 / maxClassCount);
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
    protected void registClassMetric(TargetClassInfo targetClass) {
        try {
            this.registMetric(targetClass, measureClassMetric(targetClass));
        } catch (final MetricAlreadyRegisteredException e) {
            this.err.println(e);
        }
    }

    /**
     * �N���X�̃��g���N�X���v������.
     * 
     * @param targetClass �Ώۂ̃N���X
     */
    abstract protected Number measureClassMetric(TargetClassInfo targetClass);

    /**
     * ���̃v���O�C�������g���N�X���v���ł��錾���Ԃ�.
     * 
     * �N���X�ɂ��Čv������̂��O��Ȃ̂ŃI�u�W�F�N�g�w�������ΏۂƂ���.
     * 
     * @return �I�u�W�F�N�g�w������̔z��
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE
     */
    @Override
    protected LANGUAGE[] getMeasurableLanguages() {
        return LanguageUtil.getObjectOrientedLanguages();
    }

    /**
     * ���̃v���O�C�����v�����郁�g���N�X�̃^�C�v��Ԃ�.
     * 
     * @return �N���X�̃��g���N�X���v������̂� {@link METRIC_TYPE#CLASS_METRIC} ��Ԃ�.
     */
    @Override
    protected METRIC_TYPE getMetricType() {
        return METRIC_TYPE.CLASS_METRIC;
    }

    /**
     * ���̃v���O�C�����N���X�Ɋւ�����𗘗p���邩�ǂ�����Ԃ����\�b�h.
     * 
     * @return �N���X�̃��g���N�X���v������̂� true ��Ԃ�.
     */
    @Override
    protected boolean useClassInfo() {
        return true;
    }

}