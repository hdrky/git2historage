package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �t�B�[���h�����Ǘ�����N���X�D FieldInfo ��v�f�Ƃ��Ď��D
 * 
 * @author higo
 * 
 */
public final class FieldInfoManager {

    /**
     * �Ώۃt�B�[���h��ǉ�����
     * 
     * @param fieldInfo �ǉ�����Ώۃt�B�[���h���
     */
    public void add(final TargetFieldInfo fieldInfo) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == fieldInfo) {
            throw new NullPointerException();
        }

        this.targetFieldInfos.add(fieldInfo);
    }

    /**
     * �O���t�B�[���h��ǉ�����
     * 
     * @param fieldInfo �ǉ�����O���t�B�[���h
     */
    public void add(final ExternalFieldInfo fieldInfo) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == fieldInfo) {
            throw new NullPointerException();
        }

        this.externalFieldInfos.add(fieldInfo);
    }

    /**
     * �Ώۃt�B�[���h�� SortedSet ��Ԃ��D
     * 
     * @return �Ώۃt�B�[���h�� SortedSet
     */
    public SortedSet<TargetFieldInfo> getTargetFieldInfos() {
        return Collections.unmodifiableSortedSet(this.targetFieldInfos);
    }

    /**
     * �O���t�B�[���h�� SortedSet ��Ԃ��D
     * 
     * @return �O���t�B�[���h�� SortedSet
     */
    public SortedSet<ExternalFieldInfo> getExternalFieldInfos() {
        return Collections.unmodifiableSortedSet(this.externalFieldInfos);
    }

    /**
     * �Ώۃt�B�[���h�̌���Ԃ�
     * 
     * @return �Ώۃt�B�[���h�̌�
     */
    public int getTargetFieldCount() {
        return this.targetFieldInfos.size();
    }

    /**
     * �O���t�B�[���h�̌���Ԃ�
     * 
     * @return �O���t�B�[���h�̌�
     */
    public int getExternalFieldCount() {
        return this.externalFieldInfos.size();
    }

    /**
     * �t�B�[���h�����N���A
     */
    public void clear() {
        this.targetFieldInfos.clear();
        this.externalFieldInfos.clear();
    }

    /**
     * 
     * �R���X�g���N�^�D �V���O���g���p�^�[���Ŏ������Ă��邽�߂� private �����Ă���D
     */
    public FieldInfoManager() {
        this.targetFieldInfos = new TreeSet<TargetFieldInfo>();
        this.externalFieldInfos = new TreeSet<ExternalFieldInfo>();
    }

    /**
     * 
     * �Ώۃt�B�[���h�����i�[����ϐ��D
     */
    private final SortedSet<TargetFieldInfo> targetFieldInfos;

    /**
     * 
     * �O���t�B�[���h�����i�[����ϐ��D
     */
    private final SortedSet<ExternalFieldInfo> externalFieldInfos;
}
