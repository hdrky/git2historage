package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * UnresolvedClassInfoManager ���Ǘ�����N���X
 * 
 * @author higo
 * 
 */
public class UnresolvedClassInfoManager {

    /**
     * �N���X����ǉ�����
     * 
     * @param classInfo �N���X���
     */
    public synchronized void addClass(final UnresolvedClassInfo classInfo) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfo) {
            throw new IllegalArgumentException();
        }

        final String fqName = classInfo.getFullQualifiedName(".");

        if (this.classInfos.containsKey(fqName)) {
            final StringBuilder text = new StringBuilder();
            text.append(fqName);
            text.append(" : duplicate class registration!");
            throw new IllegalStateException(text.toString());
        }

        this.classInfos.put(fqName, classInfo);
    }

    /**
     * �N���X���̃Z�b�g��Ԃ�
     * 
     * @return �N���X���̃Z�b�g
     */
    public Collection<UnresolvedClassInfo> getClassInfos() {
        return Collections.unmodifiableCollection(this.classInfos.values());
    }

    public UnresolvedClassInfo getClassInfo(final String name) {

        // �����N���X�������N���X�ꗗ���擾        
        for(final UnresolvedClassInfo classInfo : this.getClassInfos()){
            if(classInfo.getClassName().equals(name)){
                return classInfo;
            }
        }
        return null;
    }
    
    /**
     * �����Ȃ��R���X�g���N�^
     * 
     */
    public UnresolvedClassInfoManager() {
        this.classInfos = new ConcurrentHashMap<String, UnresolvedClassInfo>();
    }

    /**
     * UnresolvedClassInfo ��ۑ����邽�߂̃Z�b�g
     */
    private final ConcurrentMap<String, UnresolvedClassInfo> classInfos;
}
