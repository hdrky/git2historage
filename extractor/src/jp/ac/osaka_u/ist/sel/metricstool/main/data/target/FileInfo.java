package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.io.Serializable;
import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricMeasurable;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 
 * @author higo
 * 
 * �t�@�C���̏���\���N���X�D
 */
@SuppressWarnings("serial")
public final class FileInfo implements Comparable<FileInfo>, MetricMeasurable, Serializable {

    /**
     * �w�肳�ꂽ�t�@�C�����̃I�u�W�F�N�g������������D
     * 
     * @param name �t�@�C����
     */
    public FileInfo(final String name) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == name) {
            throw new NullPointerException();
        }

        this.name = name;
        this.definedClasses = new TreeSet<TargetClassInfo>();
        this.comments = new TreeSet<CommentInfo>();
    }

    /**
     * ���̃t�@�C���ɒ�`����Ă���N���X��ǉ�����D
     * 
     * @param definedClass ��`���ꂽ�N���X�D
     */
    public void addDefinedClass(final TargetClassInfo definedClass) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == definedClass) {
            throw new NullPointerException();
        }

        this.definedClasses.add(definedClass);
    }

    /**
     * ���̃N���X�̃C���X�^���X���m���r���郁�\�b�h
     * 
     * @param o ��r�Ώۂ̃C���X�^���X
     * @return ���̃C���X�^���X����r�Ώۂ̃C���X�^���X��菇���I�ɏ�������Ε��̐��C���������0�C�傫����ΐ��̐�.
     */
    public int compareTo(FileInfo o) {
        return this.getName().compareTo(o.getName());
    }

    /**
     * ���̃t�@�C���ɒ�`����Ă���N���X��SortedSet��Ԃ�
     * 
     * @return ���̃t�@�C���ɒ�`����Ă���N���X��SortedSet
     */
    public SortedSet<TargetClassInfo> getDefinedClasses() {
        return Collections.unmodifiableSortedSet(this.definedClasses);
    }

    /**
     * �����Ƃ��̃t�@�C�������������𔻒肷��D����ɂ́C�ϐ�name��p����D
     * 
     * @param o ��r�Ώۃt�@�C��
     * @return �������ꍇ�� true, �������Ȃ��ꍇ�� false
     */
    @Override
    public boolean equals(Object o) {

        if (null == o) {
            throw new IllegalArgumentException();
        }

        if (!(o instanceof FileInfo)) {
            return false;
        }

        String thisName = this.getName();
        String correspondName = ((FileInfo) o).getName();
        return thisName.equals(correspondName);
    }

    /**
     * �s����Ԃ��D
     * 
     * @return �s��
     */
    public int getLOC() {
        return this.loc;
    }

    /**
     * ���g���N�X�v���ΏۂƂ��Ă̖��O��Ԃ�
     * 
     * @return ���g���N�X�v���ΏۂƂ��Ă̖��O
     */
    public String getMeasuredUnitName() {
        return this.getName();
    }

    /**
     * �t�@�C������Ԃ��D ���݃t���p�X�ŕԂ����C�f�B���N�g���ƃt�@�C�����𕪂��������ǂ������D
     * 
     * @return �t�@�C����
     */
    public String getName() {
        return this.name;
    }

    /**
     * �t�@�C���̃n�b�V���R�[�h��Ԃ��D�n�b�V���R�[�h�̓t�@�C�����i�t���p�X�j��p���Čv�Z�����
     * 
     * @return ���̃t�@�C���̃n�b�V���R�[�h
     */
    @Override
    public int hashCode() {
        String name = this.getName();
        return name.hashCode();
    }

    /**
     * �ϐ� loc �� setter�D�s�������Z�b�g����D
     * 
     * @param loc �s��
     */
    public void setLOC(final int loc) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (loc < 0) {
            throw new IllegalArgumentException("LOC must be 0 or more!");
        }

        this.loc = loc;
    }

    /**
     * �R�����g��ǉ�����
     * 
     * @param comment
     */
    public void addComment(final CommentInfo comment) {

        if (null == comment) {
            throw new IllegalArgumentException();
        }

        this.comments.add(comment);
    }

    /**
     * �R�����g�̃Z�b�g��ǉ�����
     * @param commentSet
     */
    public void addAllComments(final SortedSet<CommentInfo> commentSet){
        this.comments.addAll(commentSet);
    }
    
    /**
     * �R�����g�ꗗ��Ԃ�
     * 
     * @return
     */
    public Set<CommentInfo> getComments() {
        return Collections.unmodifiableSet(this.comments);
    }

    /**
     * �t�@�C���̍s����\���ϐ��D
     */
    private int loc;

    /**
     * �t�@�C������\���ϐ�. �n�b�V���R�[�h�̌v�Z�Ɏg���Ă���D
     */
    private final String name;

    /**
     * ���̃t�@�C���Ő錾����Ă���N���X�ꗗ��ۑ����邽�߂̕ϐ�
     */
    private final SortedSet<TargetClassInfo> definedClasses;

    /**
     * ���̃t�@�C�����̃R�����g�ꗗ��ۑ����邽�߂̕ϐ�
     */
    private final SortedSet<CommentInfo> comments;

    // TODO import���Ă���N���X�̏���ǉ�
    // TODO include���Ă���t�@�C���̏���ǉ�
}
