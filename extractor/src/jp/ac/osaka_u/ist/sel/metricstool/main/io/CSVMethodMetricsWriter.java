package jp.ac.osaka_u.ist.sel.metricstool.main.io;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MethodMetricsInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricNotRegisteredException;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin.PluginInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.METRIC_TYPE;


/**
 * ���\�b�h���g���N�X��CSV�@�C���ɏ����o���N���X
 * 
 * @author higo
 * 
 */
public final class CSVMethodMetricsWriter implements MethodMetricsWriter, CSVWriter, MessageSource {

    /**
     * CSV�t�@�C����^����
     * 
     * @param fileName CSV�t�@�C����
     */
    public CSVMethodMetricsWriter(final String fileName) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == fileName) {
            throw new NullPointerException();
        }

        this.fileName = fileName;
    }

    /**
     * ���\�b�h���g���N�X��CSV�t�@�C���ɏ����o��
     */
    public void write() {

        try {

            BufferedWriter writer = new BufferedWriter(new FileWriter(this.fileName));

            // ���g���N�X���Ȃǂ������o��
            writer.write(METHOD_NAME);
            for (final AbstractPlugin plugin : DataManager.getInstance().getPluginManager()
                    .getPlugins()) {
                final PluginInfo pluginInfo = plugin.getPluginInfo();
                if (METRIC_TYPE.METHOD_METRIC == pluginInfo.getMetricType()) {
                    String metricName = pluginInfo.getMetricName();
                    writer.write(SEPARATOR);
                    writer.write(metricName);
                }
            }

            writer.newLine();

            // ���g���N�X�l�������o��
            for (final MethodMetricsInfo methodMetricsInfo : DataManager.getInstance()
                    .getMethodMetricsInfoManager()) {
                final MethodInfo methodInfo = methodMetricsInfo.getMeasuredObject();

                final String measuredName = methodInfo.getMeasuredUnitName();
                writer.write(measuredName);
                for (final AbstractPlugin plugin : DataManager.getInstance().getPluginManager()
                        .getPlugins()) {
                    final PluginInfo pluginInfo = plugin.getPluginInfo();
                    if (METRIC_TYPE.METHOD_METRIC == pluginInfo.getMetricType()) {

                        try {
                            writer.write(SEPARATOR);
                            Number value = methodMetricsInfo.getMetric(plugin);
                            writer.write(value.toString());
                        } catch (MetricNotRegisteredException e) {
                            writer.write(NO_METRIC);
                        }
                    }
                }
                writer.newLine();
            }

            writer.close();

        } catch (IOException e) {

            MessagePrinter printer = new DefaultMessagePrinter(this,
                    MessagePrinter.MESSAGE_TYPE.ERROR);
            printer.println("IO Error Happened on " + this.fileName);
        }
    }

    /**
     * MessagerPrinter ��p���邽�߂ɕK�v�ȃ��\�b�h
     * 
     * @see MessagePrinter
     * @see MessageSource
     * 
     * @return ���b�Z�[�W���M�Җ���Ԃ�
     */
    public String getMessageSourceName() {
        return this.getClass().toString();
    }

    /**
     * ���\�b�h���g���N�X�����������t�@�C������ۑ����邽�߂̕ϐ�
     */
    private final String fileName;
}
