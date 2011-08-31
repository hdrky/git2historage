package jp.ac.osaka_u.ist.sel.metricstool.main.data.metric;


import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultMessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageSource;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �N���X���g���N�X���Ǘ�����N���X�D
 * 
 * @author higo
 * 
 */
public final class ClassMetricsInfoManager implements Iterable<ClassMetricsInfo>, MessageSource {

    /**
     * ���g���N�X���ꗗ�̃C�e���[�^��Ԃ��D
     * 
     * @return ���g���N�X���̃C�e���[�^
     */
    public Iterator<ClassMetricsInfo> iterator() {
        MetricsToolSecurityManager.getInstance().checkAccess();
        Collection<ClassMetricsInfo> unmodifiableClassMetricsInfoCollection = Collections
                .unmodifiableCollection(this.classMetricsInfos.values());
        return unmodifiableClassMetricsInfoCollection.iterator();
    }

    /**
     * �����Ŏw�肳�ꂽ�N���X�̃��g���N�X����Ԃ��D �����Ŏw�肳�ꂽ�N���X�̃��g���N�X��񂪑��݂��Ȃ��ꍇ�́C null ��Ԃ��D
     * 
     * @param classInfo �ق������g���N�X���̃N���X
     * @return ���g���N�X���
     */
    public ClassMetricsInfo get(final ClassInfo classInfo) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfo) {
            throw new NullPointerException();
        }

        return this.classMetricsInfos.get(classInfo);
    }

    /**
     * ���g���N�X��o�^����
     * 
     * @param classInfo ���g���N�X�v���Ώۂ̃N���X�I�u�W�F�N�g
     * @param plugin ���g���N�X�̃v���O�C��
     * @param value ���g���N�X�l
     * @throws MetricAlreadyRegisteredException �o�^���悤�Ƃ��Ă��郁�g���N�X�����ɓo�^����Ă���
     */
    public void putMetric(final TargetClassInfo classInfo, final AbstractPlugin plugin,
            final Number value) throws MetricAlreadyRegisteredException {

        ClassMetricsInfo classMetricsInfo = this.classMetricsInfos.get(classInfo);

        // �ΏۃN���X�� classMetricsInfo �������ꍇ�́Cnew ���� Map �ɓo�^����
        if (null == classMetricsInfo) {
            classMetricsInfo = new ClassMetricsInfo(classInfo);
            this.classMetricsInfos.put(classInfo, classMetricsInfo);
        }

        classMetricsInfo.putMetric(plugin, value);
    }

    /**
     * �N���X���g���N�X�ɓo�^�R�ꂪ�Ȃ������`�F�b�N����
     * 
     * @throws MetricNotRegisteredException �o�^�R�ꂪ�������ꍇ�ɃX���[�����
     */
    public void checkMetrics() throws MetricNotRegisteredException {

        MetricsToolSecurityManager.getInstance().checkAccess();

        for (final TargetClassInfo classInfo : DataManager.getInstance().getClassInfoManager()
                .getTargetClassInfos()) {

            ClassMetricsInfo classMetricsInfo = this.get(classInfo);
            if (null == classMetricsInfo) {
                String message = "Class \"" + classInfo.getFullQualifiedName(".")
                        + "\" metrics are not registered!";
                MessagePrinter printer = new DefaultMessagePrinter(this,
                        MessagePrinter.MESSAGE_TYPE.ERROR);
                printer.println(message);
                throw new MetricNotRegisteredException(message);
            }
            classMetricsInfo.checkMetrics(DataManager.getInstance().getPluginManager()
                    .getClassMetricPlugins());
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
     * �N���X���g���N�X�}�l�[�W���̃I�u�W�F�N�g�𐶐�����D 
     */
    public ClassMetricsInfoManager() {
        //MetricsToolSecurityManager.getInstance().checkAccess();
        this.classMetricsInfos = Collections
                .synchronizedSortedMap(new TreeMap<TargetClassInfo, ClassMetricsInfo>());
    }

    /**
     * �N���X���g���N�X�̃}�b�v��ۑ����邽�߂̕ϐ�
     */
    private final SortedMap<TargetClassInfo, ClassMetricsInfo> classMetricsInfos;
}
