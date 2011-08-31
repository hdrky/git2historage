package jp.ac.osaka_u.ist.sel.metricstool.main.data.metric;


import java.util.Collections;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultMessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageSource;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.MetricTypeAndNamePluginComparator;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin.PluginInfo;

/**
 * ���g���N�X��\�����ۃN���X
 * 
 * @author higo
 *
 * @param <T> ���g���N�X�̌v���P��
 */
public abstract class MetricsInfo<T extends MetricMeasurable> implements MessageSource {

    /**
     * �v���ΏۃI�u�W�F�N�g��^���ď�����
     * 
     * @param measuredObject �v���ΏۃI�u�W�F�N�g
     */
    public MetricsInfo(final T measuredObject) {

        if (null == measuredObject) {
            throw new NullPointerException();
        }

        this.measuredObject = measuredObject;
        this.metrics = Collections.synchronizedSortedMap(new TreeMap<AbstractPlugin, Number>(
                new MetricTypeAndNamePluginComparator()));
    }

    /**
     * ���̃��g���N�X�̌v���ΏۃI�u�W�F�N�g��Ԃ�
     * 
     * @return ���̃��g���N�X�̌v���ΏۃI�u�W�F�N�g
     */
    public final T getMeasuredObject() {
        return this.measuredObject;
    }

    /**
     * �����Ŏw�肵���v���O�C���ɂ���ēo�^���ꂽ���g���N�X�����擾���郁�\�b�h�D
     * 
     * @param key �ق������g���N�X��o�^�����v���O�C��
     * @return ���g���N�X�l
     * @throws MetricNotRegisteredException ���g���N�X���o�^����Ă��Ȃ��Ƃ��ɃX���[�����
     */
    public final Number getMetric(final AbstractPlugin key) throws MetricNotRegisteredException {

        if (null == key) {
            throw new NullPointerException();
        }

        final Number value = this.metrics.get(key);
        if (null == value) {
            throw new MetricNotRegisteredException();
        }

        return value;
    }

    /**
     * �������ŗ^����ꂽ�v���O�C���Ōv�����ꂽ���g���N�X�l�i�������j��o�^����D
     * 
     * @param key �v�������v���O�C���C���X�^���X�CMap �̃L�[�Ƃ��ėp����D
     * @param value �o�^���郁�g���N�X�l
     * @throws MetricAlreadyRegisteredException �o�^���悤�Ƃ��Ă������g���N�X�����ɓo�^����Ă����ꍇ�ɃX���[�����
     */
    public final void putMetric(final AbstractPlugin key, final Number value)
            throws MetricAlreadyRegisteredException {

        if ((null == key) || (null == value)) {
            throw new NullPointerException();
        }

        if (this.metrics.containsKey(key)) {
            throw new MetricAlreadyRegisteredException();
        }

        this.metrics.put(key, value);
    }

    /**
     * ���̃��g���N�X���ɕs�����Ȃ������`�F�b�N����
     * 
     * @throws MetricNotRegisteredException
     */
    final void checkMetrics(final Set<AbstractPlugin> usingPlugins)
            throws MetricNotRegisteredException {

        for (final AbstractPlugin plugin : usingPlugins) {
            final Number value = this.getMetric(plugin);
            if (null == value) {
                final PluginInfo pluginInfo = plugin.getPluginInfo();
                final String metricName = pluginInfo.getMetricName();
                final String measuredUnitName = this.measuredObject.getMeasuredUnitName();
                final String message = "Metric \"" + metricName + "\" of " + measuredUnitName
                        + " is not registered!";
                final MessagePrinter printer = new DefaultMessagePrinter(this,
                        MessagePrinter.MESSAGE_TYPE.ERROR);
                printer.println(message);
                throw new MetricNotRegisteredException(message);
            }
        }
    }

    private final T measuredObject;

    /**
     * ���g���N�X��ۑ����邽�߂̕ϐ�
     */
    private final SortedMap<AbstractPlugin, Number> metrics;
}
