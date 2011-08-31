package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 
 * @author higo
 * 
 * �Ώۃt�@�C�����i�[���邽�߂̃N���X�D TargetFile ��v�f�Ƃ��Ď��D
 * 
 * since 2006.11.12
 */
public final class TargetFileManager implements Iterable<TargetFile> {

    /**
     * 
     * @param targetFile �ǉ�����Ώۃt�@�C�� (TargetFile)
     */
    public void add(final TargetFile targetFile) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == targetFile) {
            throw new NullPointerException();
        }

        final String filename = targetFile.getName();
        this.targetFiles.put(filename, targetFile);
    }

    @Override
    public Iterator<TargetFile> iterator() {
        return this.getFiles().iterator();
    }

    /**
     * �Ώۃt�@�C���̐���Ԃ�
     * 
     * @return �Ώۃt�@�C���̐�
     */
    public int size() {
        return this.targetFiles.size();
    }

    /**
     * �Ώۃt�@�C�����N���A
     */
    public void clear() {
        this.targetFiles.clear();
    }

    /**
     * �o�^����Ă���Ώۃt�@�C����SortedSet��Ԃ�
     * 
     * @return �o�^����Ă���Ώۃt�@�C����SortedSet
     */
    public SortedSet<TargetFile> getFiles() {
        final SortedSet<TargetFile> files = new TreeSet<TargetFile>();
        files.addAll(this.targetFiles.values());
        return files;
    }

    /**
     * �����ŗ^����ꂽ�p�X�̃t�@�C����Ԃ�
     * 
     * @param filepath
     * @return
     */
    public TargetFile getFile(final String filepath) {
        return this.targetFiles.get(filepath);
    }

    /**
     * 
     * �R���X�g���N�^�D 
     * �ȑO�� HashSet ��p���Ă������C�����f�B���N�g���̃t�@�C���͂܂Ƃ߂ĕԂ��ق����悢�̂ŁCTreeSet �ɕύX�����D
     */
    public TargetFileManager() {
        this.targetFiles = new ConcurrentHashMap<String, TargetFile>();
    }

    /**
     * 
     * �Ώۃt�@�C�� (TargetFile) ���i�[����ϐ��D
     */
    private final ConcurrentMap<String, TargetFile> targetFiles;
}
