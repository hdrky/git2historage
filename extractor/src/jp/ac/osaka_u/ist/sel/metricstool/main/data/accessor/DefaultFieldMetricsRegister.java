package jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.FieldMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricAlreadyRegisteredException;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin.PluginInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.METRIC_TYPE;


/**
 * �v���O�C�����t�B�[���h���g���N�X��o�^���邽�߂ɗp����N���X�D
 * 
 * @author higo
 */
public class DefaultFieldMetricsRegister implements FieldMetricsRegister {

    /**
     * �o�^�����p�̃I�u�W�F�N�g�̏��������s���D�v���O�C���͎��g�������Ƃ��ė^���Ȃ���΂Ȃ�Ȃ��D
     * 
     * @param plugin ���������s���v���O�C���̃C���X�^���X
     */
    public DefaultFieldMetricsRegister(final AbstractPlugin plugin) {

        if (null == plugin) {
            throw new NullPointerException();
        }
        final PluginInfo pluginInfo = plugin.getPluginInfo();
        if (METRIC_TYPE.FIELD_METRIC != pluginInfo.getMetricType()) {
            throw new IllegalArgumentException("plugin must be field type!");
        }

        this.plugin = plugin;
    }

    /**
     * �������̃t�@�C���̃��g���N�X�l�i�������j��o�^����
     * 
     * @param fieldInfo ���g���N�X��o�^����t�B�[���h
     * @param value �o�^���郁�g���N�X�l
     * @throws MetricAlreadyRegisteredException ���łɃ��g���N�X���o�^����Ă���ꍇ�ɃX���[������O
     */
    public void registMetric(final TargetFieldInfo fieldInfo, final Number value)
            throws MetricAlreadyRegisteredException {

        if ((null == fieldInfo) || (null == value)) {
            throw new NullPointerException();
        }

        final FieldMetricsInfoManager manager = DataManager.getInstance()
                .getFieldMetricsInfoManager();
        manager.putMetric(fieldInfo, this.plugin, value);
    }

    /**
     * �v���O�C���I�u�W�F�N�g��ۑ����Ă������߂̕ϐ�
     */
    private final AbstractPlugin plugin;
}
