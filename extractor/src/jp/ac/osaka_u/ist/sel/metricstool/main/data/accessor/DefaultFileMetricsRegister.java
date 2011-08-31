package jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.FileMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricAlreadyRegisteredException;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin.PluginInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.METRIC_TYPE;


/**
 * �v���O�C�����t�@�C�����g���N�X��o�^���邽�߂ɗp����N���X�D
 * 
 * @author higo
 */
public class DefaultFileMetricsRegister implements FileMetricsRegister {

    /**
     * �o�^�����p�̃I�u�W�F�N�g�̏��������s���D�v���O�C���͎��g�������Ƃ��ė^���Ȃ���΂Ȃ�Ȃ��D
     * 
     * @param plugin ���������s���v���O�C���̃C���X�^���X
     */
    public DefaultFileMetricsRegister(final AbstractPlugin plugin) {

        if (null == plugin) {
            throw new NullPointerException();
        }
        final PluginInfo pluginInfo = plugin.getPluginInfo();
        if (METRIC_TYPE.FILE_METRIC != pluginInfo.getMetricType()) {
            throw new IllegalArgumentException("plugin must be file type!");
        }

        this.plugin = plugin;
    }

    /**
     * �������̃t�@�C���̃��g���N�X�l�i�������j��o�^����
     * 
     * @param fileInfo ���g���N�X��o�^����t�@�C��
     * @param value �o�^���郁�g���N�X�l
     * @throws MetricAlreadyRegisteredException ���łɃ��g���N�X���o�^����Ă���ꍇ�ɃX���[������O
     */
    @Override
    public void registMetric(final FileInfo fileInfo, final Number value)
            throws MetricAlreadyRegisteredException {

        if ((null == fileInfo) || (null == value)) {
            throw new NullPointerException();
        }

        final FileMetricsInfoManager manager = DataManager.getInstance()
                .getFileMetricsInfoManager();
        manager.putMetric(fileInfo, this.plugin, value);
    }

    /**
     * �v���O�C���I�u�W�F�N�g��ۑ����Ă������߂̕ϐ�
     */
    private final AbstractPlugin plugin;
}
