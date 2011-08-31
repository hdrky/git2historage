package jp.ac.osaka_u.ist.sel.metricstool.main.data.metric;


import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultMessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageSource;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �t�@�C�����g���N�X���Ǘ�����N���X�D
 * 
 * @author higo
 * 
 */
public final class FileMetricsInfoManager implements Iterable<FileMetricsInfo>, MessageSource {

    /**
     * ���g���N�X���ꗗ�̃C�e���[�^��Ԃ��D
     * 
     * @return ���g���N�X���̃C�e���[�^
     */
    public Iterator<FileMetricsInfo> iterator() {
        MetricsToolSecurityManager.getInstance().checkAccess();
        Collection<FileMetricsInfo> unmodifiableFileMetricsInfoCollection = Collections
                .unmodifiableCollection(this.fileMetricsInfos.values());
        return unmodifiableFileMetricsInfoCollection.iterator();
    }

    /**
     * �����Ŏw�肳�ꂽ�t�@�C���̃��g���N�X����Ԃ��D �����Ŏw�肳�ꂽ�t�@�C���̃��g���N�X��񂪑��݂��Ȃ��ꍇ�́C null ��Ԃ��D
     * 
     * @param fileInfo �ق������g���N�X���̃t�@�C��
     * @return ���g���N�X���
     */
    public FileMetricsInfo get(final FileInfo fileInfo) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == fileInfo) {
            throw new NullPointerException();
        }

        return this.fileMetricsInfos.get(fileInfo);
    }

    /**
     * ���g���N�X��o�^����
     * 
     * @param fileInfo ���g���N�X�v���Ώۂ̃t�@�C���I�u�W�F�N�g
     * @param plugin ���g���N�X�̃v���O�C��
     * @param value ���g���N�X�l
     * @throws MetricAlreadyRegisteredException �o�^���悤�Ƃ��Ă��郁�g���N�X�����ɓo�^����Ă���
     */
    public void putMetric(final FileInfo fileInfo, final AbstractPlugin plugin, final Number value)
            throws MetricAlreadyRegisteredException {

        FileMetricsInfo fileMetricsInfo = this.fileMetricsInfos.get(fileInfo);

        // �Ώۃt�@�C���� fileMetricsInfo �������ꍇ�́Cnew ���� Map �ɓo�^����
        if (null == fileMetricsInfo) {
            fileMetricsInfo = new FileMetricsInfo(fileInfo);
            this.fileMetricsInfos.put(fileInfo, fileMetricsInfo);
        }

        fileMetricsInfo.putMetric(plugin, value);
    }

    /**
     * �t�@�C�����g���N�X�ɓo�^�R�ꂪ�Ȃ������`�F�b�N����
     * 
     * @throws MetricNotRegisteredException �o�^�R�ꂪ�������ꍇ�ɃX���[�����
     */
    public void checkMetrics() throws MetricNotRegisteredException {

        MetricsToolSecurityManager.getInstance().checkAccess();

        for (final FileInfo fileInfo : DataManager.getInstance().getFileInfoManager()
                .getFileInfos()) {

            final FileMetricsInfo fileMetricsInfo = this.get(fileInfo);
            if (null == fileMetricsInfo) {
                final String message = "File \"" + fileInfo.getName()
                        + "\" metrics are not registered!";
                final MessagePrinter printer = new DefaultMessagePrinter(this,
                        MessagePrinter.MESSAGE_TYPE.ERROR);
                printer.println(message);
                throw new MetricNotRegisteredException(message);
            }
            fileMetricsInfo.checkMetrics(DataManager.getInstance().getPluginManager()
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
     * �t�@�C�����g���N�X�}�l�[�W���̃I�u�W�F�N�g�𐶐�����D
     * 
     */
    public FileMetricsInfoManager() {
        //MetricsToolSecurityManager.getInstance().checkAccess();
        this.fileMetricsInfos = Collections
                .synchronizedSortedMap(new TreeMap<FileInfo, FileMetricsInfo>());
    }

    /**
     * �t�@�C�����g���N�X�̃}�b�v��ۑ����邽�߂̕ϐ�
     */
    private final SortedMap<FileInfo, FileMetricsInfo> fileMetricsInfos;
}
