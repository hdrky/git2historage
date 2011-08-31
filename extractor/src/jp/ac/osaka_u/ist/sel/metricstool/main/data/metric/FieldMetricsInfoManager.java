package jp.ac.osaka_u.ist.sel.metricstool.main.data.metric;


import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultMessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageSource;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �t�B�[���h���g���N�X���Ǘ�����N���X�D
 * 
 * @author higo
 * 
 */
public final class FieldMetricsInfoManager implements Iterable<FieldMetricsInfo>, MessageSource {

    /**
    * ���g���N�X���ꗗ�̃C�e���[�^��Ԃ��D
    * 
    * @return ���g���N�X���̃C�e���[�^
    */
    public Iterator<FieldMetricsInfo> iterator() {
        MetricsToolSecurityManager.getInstance().checkAccess();
        Collection<FieldMetricsInfo> unmodifiableFieldMetricsInfoCollection = Collections
                .unmodifiableCollection(this.fieldMetricsInfos.values());
        return unmodifiableFieldMetricsInfoCollection.iterator();
    }

    /**
     * �����Ŏw�肳�ꂽ�t�B�[���h�̃��g���N�X����Ԃ��D �����Ŏw�肳�ꂽ�t�B�[���h�̃��g���N�X��񂪑��݂��Ȃ��ꍇ�́C null ��Ԃ��D
     * 
     * @param fieldInfo �t�B�[���h
     * @return ���g���N�X���
     */
    public FieldMetricsInfo get(final TargetFieldInfo fieldInfo) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == fieldInfo) {
            throw new NullPointerException();
        }

        return this.fieldMetricsInfos.get(fieldInfo);
    }

    /**
     * ���g���N�X��o�^����
     * 
     * @param fieldInfo ���g���N�X�v���Ώۂ̃t�B�[���h�I�u�W�F�N�g
     * @param plugin ���g���N�X�̃v���O�C��
     * @param value ���g���N�X�l
     * @throws MetricAlreadyRegisteredException �o�^���悤�Ƃ��Ă��郁�g���N�X�����ɓo�^����Ă���
     */
    public void putMetric(final TargetFieldInfo fieldInfo, final AbstractPlugin plugin,
            final Number value) throws MetricAlreadyRegisteredException {

        FieldMetricsInfo fieldMetricsInfo = this.fieldMetricsInfos.get(fieldInfo);

        // �Ώۃt�B�[���h�� fieldMetricsInfo �������ꍇ�́Cnew ���� Map �ɓo�^����
        if (null == fieldMetricsInfo) {
            fieldMetricsInfo = new FieldMetricsInfo(fieldInfo);
            this.fieldMetricsInfos.put(fieldInfo, fieldMetricsInfo);
        }

        fieldMetricsInfo.putMetric(plugin, value);
    }

    /**
     * �t�B�[���h���g���N�X�ɓo�^�R�ꂪ�Ȃ������`�F�b�N����
     * 
     * @throws MetricNotRegisteredException �o�^�R�ꂪ�������ꍇ�ɃX���[�����
     */
    public void checkMetrics() throws MetricNotRegisteredException {

        MetricsToolSecurityManager.getInstance().checkAccess();

        for (final TargetFieldInfo fieldInfo : DataManager.getInstance().getFieldInfoManager()
                .getTargetFieldInfos()) {

            final FieldMetricsInfo fieldMetricsInfo = this.get(fieldInfo);
            if (null == fieldMetricsInfo) {
                final String message = "Field \"" + fieldInfo.getName()
                        + "\" metrics are not registered!";
                final MessagePrinter printer = new DefaultMessagePrinter(this,
                        MessagePrinter.MESSAGE_TYPE.ERROR);
                printer.println(message);
                throw new MetricNotRegisteredException(message);
            }
            fieldMetricsInfo.checkMetrics(DataManager.getInstance().getPluginManager()
                    .getFileMetricPlugins());
        }
    }

    /**
     * ���b�Z�[�W���M�Җ���Ԃ�
     * 
     * @return ���b�Z�[�W���M��
     */
    public String getMessageSourceName() {
        return this.getClass().getName();
    }

    /**
     * �t�B�[���h���g���N�X�}�l�[�W���̃I�u�W�F�N�g�𐶐�����D 
     * 
     */
    public FieldMetricsInfoManager() {
        //MetricsToolSecurityManager.getInstance().checkAccess();
        this.fieldMetricsInfos = Collections
                .synchronizedSortedMap(new TreeMap<TargetFieldInfo, FieldMetricsInfo>());
    }

    /**
    * �t�@�C�����g���N�X�̃}�b�v��ۑ����邽�߂̕ϐ�
    */
    private final SortedMap<TargetFieldInfo, FieldMetricsInfo> fieldMetricsInfos;
}
