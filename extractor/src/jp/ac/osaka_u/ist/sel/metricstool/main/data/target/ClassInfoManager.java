package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultMessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageSource;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter.MESSAGE_TYPE;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �N���X�����Ǘ�����N���X�D
 * 
 * @author higo
 * 
 */
public final class ClassInfoManager {

    /**
     * �ΏۃN���X��ǉ�����
     * 
     * @param classInfo �ǉ�����N���X���
     * @return �����N���X��ǉ������ꍇ�� true,���Ȃ������ꍇ��false
     */
    public boolean add(final ClassInfo classInfo) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfo) {
            throw new IllegalArgumentException();
        }

        // �ǉ�����N���X���ΏۃN���X�̎�
        if (classInfo instanceof TargetClassInfo) {

            //��d�o�^�`�F�b�N
            if (this.targetClassInfos.contains(classInfo)) {
                err.println(classInfo.getFullQualifiedName(".") + " is already registered!");
                return false;
            }

            // ���łɊO���N���X�Ƃ��ēo�^����Ă���ꍇ�́C���̏����폜����
            if (this.externalClassInfos.contains(classInfo)) {
                this.externalClassInfos.remove(classInfo);

                {
                    final String name = classInfo.getClassName();
                    SortedSet<ClassInfo> classInfos = this.classNameMap.get(name);
                    classInfos.remove(classInfo);
                }

                {
                    final NamespaceInfo namespace = classInfo.getNamespace();
                    SortedSet<ClassInfo> classInfos = this.namespaceMap.get(namespace);
                    classInfos.remove(classInfo);
                }
            }

            if (this.targetClassInfos.contains((TargetClassInfo) classInfo)) {
                final StringBuilder text = new StringBuilder();
                text.append(classInfo.getFullQualifiedName("."));
                text.append(" : duplicate class registration!");
                throw new IllegalStateException(text.toString());
            }
            this.targetClassInfos.add((TargetClassInfo) classInfo);
        }

        else if (classInfo instanceof ExternalClassInfo) {

            // ���łɑΏۃN���X�ɓo�^����Ă���ꍇ�͉������Ȃ�
            if (this.targetClassInfos.contains(classInfo)) {
                return false;
            }

            // ��d�o�^�`�F�b�N�C�������G���[�͏o���Ȃ�
            if (this.externalClassInfos.contains(classInfo)) {
                return false;
            }

            this.externalClassInfos.add((ExternalClassInfo) classInfo);
        }

        else {
            assert false : "Here shouldn't be reached!";
        }

        // �N���X������N���X�I�u�W�F�N�g�𓾂邽�߂̃}�b�v�ɒǉ�
        {
            final String name = classInfo.getClassName();
            SortedSet<ClassInfo> classInfos = this.classNameMap.get(name);
            if (null == classInfos) {
                classInfos = new TreeSet<ClassInfo>();
                this.classNameMap.put(name, classInfos);
            }
            classInfos.add(classInfo);
        }

        //�@���O��Ԃ���N���X�I�u�W�F�N�g�𓾂邽�߂̃}�b�v�ɒǉ�
        {
            final NamespaceInfo namespace = classInfo.getNamespace();
            SortedSet<ClassInfo> classInfos = this.namespaceMap.get(namespace);
            if (null == classInfos) {
                classInfos = new TreeSet<ClassInfo>();
                this.namespaceMap.put(namespace, classInfos);
            }
            classInfos.add(classInfo);
        }

        return true;
    }

    /**
     * �ΏۃN���X��SortedSet��Ԃ�
     * 
     * @return �ΏۃN���X��SortedSet
     */
    public SortedSet<TargetClassInfo> getTargetClassInfos() {
        return Collections.unmodifiableSortedSet(this.targetClassInfos);
    }

    /**
     * �O���N���X��SortedSet��Ԃ�
     * 
     * @return �O���N���X��SortedSet
     */
    public SortedSet<ExternalClassInfo> getExternalClassInfos() {
        return Collections.unmodifiableSortedSet(this.externalClassInfos);
    }

    /**
     * �ΏۃN���X�̐���Ԃ�
     * 
     * @return �ΏۃN���X�̐�
     */
    public int getTargetClassCount() {
        return this.targetClassInfos.size();
    }

    /**
     * �O���N���X�̐���Ԃ�
     * 
     * @return �O���N���X�̐�
     */
    public int getExternalClassCount() {
        return this.externalClassInfos.size();
    }

    /**
     * �����Ŏw�肵�����S���薼�����N���X����Ԃ�.
     * �w�肳�ꂽ���S���薼�����N���X�����݂��Ȃ��Ƃ���null��Ԃ�
     * 
     * @param fullQualifiedName ���S���薼
     * @return �N���X���
     */
    public ClassInfo getClassInfo(final String[] fullQualifiedName) {

        if ((null == fullQualifiedName) || (0 == fullQualifiedName.length)) {
            throw new IllegalArgumentException();
        }

        final int namespaceLength = fullQualifiedName.length - 1;
        final String[] namespace = Arrays.<String> copyOf(fullQualifiedName, namespaceLength);
        final String className = fullQualifiedName[namespaceLength];

        // �����N���X�������N���X�ꗗ���擾        
        final SortedSet<ClassInfo> classInfos = this.classNameMap.get(className);
        if (null != classInfos) {
            // ���O��Ԃ��������N���X��Ԃ�
            for (final ClassInfo classInfo : classInfos) {
                if (classInfo.getNamespace().equals(namespace)) {
                    return classInfo;
                }
            }
        }
        return null;
    }

    /**
     * �����Ŏw�肵�����S���薼�����N���X�����邩���肷��
     * 
     * @param fullQualifiedName �����������N���X�̊��S���薼
     * @return �N���X������ꍇ��true, �Ȃ��ꍇ��false
     */
    public boolean hasClassInfo(final String[] fullQualifiedName) {

        if ((null == fullQualifiedName) || (0 == fullQualifiedName.length)) {
            throw new IllegalArgumentException();
        }

        final int namespaceLength = fullQualifiedName.length - 1;
        final String[] namespace = Arrays.<String> copyOf(fullQualifiedName, namespaceLength);
        final String className = fullQualifiedName[namespaceLength];

        //�����N���X�������N���X�ꗗ���擾
        final SortedSet<ClassInfo> classInfos = this.classNameMap.get(className);
        if (null != classInfos) {

            // ���O��Ԃ��������N���X������΁Ctrue��Ԃ�
            for (final ClassInfo classInfo : classInfos) {
                if (classInfo.getNamespace().equals(namespace)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * �����Ŏw�肵�����O��Ԃ����N���X���� Collection ��Ԃ�
     * 
     * @param namespace ���O���
     * @return �����Ŏw�肵�����O��Ԃ����N���X���� Collection
     */
    public Collection<ClassInfo> getClassInfos(final String[] namespace) {

        if (null == namespace) {
            throw new IllegalArgumentException();
        }

        return this.getClassInfos(new NamespaceInfo(namespace));
    }

    /**
     * �����Ŏw�肵�����O��Ԃ����N���X���� Collection ��Ԃ�
     * 
     * @param namespace ���O���
     * @return �����Ŏw�肵�����O��Ԃ����N���X���� Collection
     */
    public Collection<ClassInfo> getClassInfos(final NamespaceInfo namespace) {

        if (null == namespace) {
            throw new IllegalArgumentException();
        }

        final SortedSet<ClassInfo> classInfos = this.namespaceMap.get(namespace);
        return null != classInfos ? Collections.unmodifiableSortedSet(classInfos) : Collections
                .unmodifiableSortedSet(new TreeSet<ClassInfo>());
    }

    public Collection<ClassInfo> getClassInfosWithPrefix(final String[] prefix) {

        final SortedSet<ClassInfo> matchedClasses = new TreeSet<ClassInfo>();
        for (final ClassInfo classInfo : this.getTargetClassInfos()) {
            final String[] fqName = classInfo.getFullQualifiedName();
            if (isMatch(fqName, prefix)) {
                matchedClasses.add(classInfo);
            }
        }

        for (final ClassInfo classInfo : this.getExternalClassInfos()) {
            final String[] fqName = classInfo.getFullQualifiedName();
            if (isMatch(fqName, prefix)) {
                matchedClasses.add(classInfo);
            }
        }

        return matchedClasses;
    }

    public Collection<ClassInfo> getClassInfosWithSuffix(final String[] suffix) {

        final SortedSet<ClassInfo> matchedClasses = new TreeSet<ClassInfo>();
        for (final ClassInfo classInfo : this.getTargetClassInfos()) {
            final String[] fqName = classInfo.getFullQualifiedName();
            if (isMatch(this.reverse(fqName), this.reverse(suffix))) {
                matchedClasses.add(classInfo);
            }
        }

        for (final ClassInfo classInfo : this.getExternalClassInfos()) {
            final String[] fqName = classInfo.getFullQualifiedName();
            if (isMatch(this.reverse(fqName), this.reverse(suffix))) {
                matchedClasses.add(classInfo);
            }
        }

        return matchedClasses;
    }

    private boolean isMatch(final String[] fqName, final String[] prefix) {

        for (int index = 0; index < prefix.length; index++) {

            if (fqName.length <= index) {
                return false;
            }

            if (!fqName[index].equals(prefix[index])) {
                return false;
            }
        }

        return true;
    }

    private String[] reverse(final String[] array) {
        final String[] reverseArray = new String[array.length];
        for (int index = 0; index < array.length; index++) {
            reverseArray[array.length - index - 1] = array[index];
        }
        return reverseArray;
    }

    /**
     * �����Ŏw�肵���N���X�������N���X���� Collection ��Ԃ�
     * 
     * @param className �N���X��
     * @return �����Ŏw�肵���N���X�������N���X���� Collection
     */
    public Collection<ClassInfo> getClassInfos(final String className) {

        if (null == className) {
            throw new IllegalArgumentException();
        }

        final SortedSet<ClassInfo> classInfos = this.classNameMap.get(className);
        return null != classInfos ? Collections.unmodifiableSortedSet(classInfos) : Collections
                .unmodifiableSortedSet(new TreeSet<ClassInfo>());
    }

    /**
     * �G���[���b�Z�[�W�o�͗p�̃v�����^
     */
    private static final MessagePrinter err = new DefaultMessagePrinter(new MessageSource() {
        public String getMessageSourceName() {
            return "main";
        }
    }, MESSAGE_TYPE.ERROR);

    /**
     * 
     * �R���X�g���N�^�D 
     */
    public ClassInfoManager() {

        this.classNameMap = new HashMap<String, SortedSet<ClassInfo>>();
        this.namespaceMap = new HashMap<NamespaceInfo, SortedSet<ClassInfo>>();

        this.targetClassInfos = new TreeSet<TargetClassInfo>();
        this.externalClassInfos = new TreeSet<ExternalClassInfo>();
    }

    /**
     * �N���X������C�N���X�I�u�W�F�N�g�𓾂邽�߂̃}�b�v
     */
    private final Map<String, SortedSet<ClassInfo>> classNameMap;

    /**
     * ���O��Ԗ�����C�N���X�I�u�W�F�N�g�𓾂邽�߂̃}�b�v
     */
    private final Map<NamespaceInfo, SortedSet<ClassInfo>> namespaceMap;

    /**
     * �ΏۃN���X�ꗗ��ۑ����邽�߂̃Z�b�g
     */
    private final SortedSet<TargetClassInfo> targetClassInfos;

    /**
     * �O���N���X�ꗗ��ۑ����邽�߂̃Z�b�g
     */
    private final SortedSet<ExternalClassInfo> externalClassInfos;
}
