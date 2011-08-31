package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricMeasurable;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �t�B�[���h�I�u�W�F�N�g��\���N���X�D �ȉ��̏������D
 * <ul>
 * <li>�t�B�[���h��</li>
 * <li>�t�B�[���h�̌^</li>
 * <li>�t�B�[���h�̏C���q</li>
 * <li>�t�B�[���h���`���Ă���N���X</li>
 * <li>�t�B�[���h���Q�Ƃ��Ă��郁�\�b�h�Q</li>
 * <li>�t�B�[���h�ɑ΂��đ�����s���Ă��郁�\�b�h�Q</li>
 * </ul>
 * 
 * @author higo
 */
@SuppressWarnings("serial")
public abstract class FieldInfo extends VariableInfo<ClassInfo> implements MetricMeasurable,
        Member, Visualizable, StaticOrInstance {

    /**
     * �t�B�[���h�I�u�W�F�N�g������������D �t�B�[���h���ƌ^�C��`���Ă���N���X���^�����Ȃ���΂Ȃ�Ȃ��D
     * 
     * @param modifiers �C���q�̃Z�b�g
     * @param name �t�B�[���h��
     * @param definitionClass �t�B�[���h���`���Ă���N���X
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    FieldInfo(final Set<ModifierInfo> modifiers, final String name,
            final ClassInfo definitionClass, final boolean instance, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(modifiers, name, null, definitionClass, fromLine, fromColumn, toLine, toColumn);

        if (null == definitionClass) {
            throw new NullPointerException();
        }

        this.instance = instance;

        this.ownerClass = definitionClass;
        this.referencers = Collections.synchronizedSortedSet(new TreeSet<CallableUnitInfo>());
        this.assignmenters = Collections.synchronizedSortedSet(new TreeSet<CallableUnitInfo>());
    }

    /**
     * �^����ꂽ�ϐ���Set�Ɋ܂܂�Ă���t�B�[���h��Set�Ƃ��ĕԂ�
     * @param variables �ϐ���Set
     * @return �^����ꂽ�ϐ���Set�Ɋ܂܂��t�B�[���h��Set
     */
    public static Set<FieldInfo> getLocalVariables(Collection<VariableInfo<?>> variables) {
        final Set<FieldInfo> fields = new HashSet<FieldInfo>();
        for (final VariableInfo<?> variable : variables) {
            if (variable instanceof FieldInfo) {
                fields.add((FieldInfo) variable);
            }
        }
        return Collections.unmodifiableSet(fields);
    }

    /**
     * ���g���N�X�v���ΏۂƂ��Ă̖��O��Ԃ�
     * 
     * @return ���g���N�X�v���ΏۂƂ��Ă̖��O
     */
    public final String getMeasuredUnitName() {

        final StringBuilder sb = new StringBuilder(this.getName());
        sb.append("#");
        sb.append(this.getOwnerClass().getFullQualifiedName(
                Settings.getInstance().getLanguage().getNamespaceDelimiter()));
        return sb.toString();
    }

    /**
     * ���̃t�B�[���h���Q�Ƃ��Ă��郁�\�b�h�܂��̓R���X�g���N�^��ǉ�����
     * 
     * @param referencer ���̃t�B�[���h���Q�Ƃ��Ă��郁�\�b�h�܂��̓R���X�g���N�^
     */
    public final void addReferencer(final CallableUnitInfo referencer) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == referencer) {
            return;
        }

        this.referencers.add(referencer);
    }

    /**
     * ���̃t�B�[���h�ɑ΂��đ�����s���Ă��郁�\�b�h��ǉ�����
     * 
     * @param assignmenter ���̃t�B�[���h�ɑ΂��đ�����s���Ă��郁�\�b�h
     */
    public final void addAssignmenter(final CallableUnitInfo assignmenter) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == assignmenter) {
            throw new NullPointerException();
        }

        this.assignmenters.add(assignmenter);
    }

    /**
     * �t�B�[���h�I�u�W�F�N�g�̏������`���郁�\�b�h�D���̃t�B�[���h���`���Ă���N���X�̏����ɏ]���D�����N���X���ɒ�`����Ă���ꍇ�́C
     * 
     * @param fieldInfo ��r�ΏۃI�u�W�F�N�g
     * @return �t�B�[���h�̏����֌W
     */
    //    @Override
    //    public final int compareTo(final TargetFieldInfo fieldInfo) {
    //
    //        if (null == fieldInfo) {
    //            throw new NullPointerException();
    //        }
    //        final ClassInfo classInfo = this.getOwnerClass();
    //        final ClassInfo correspondClassInfo = this.getOwnerClass();
    //        final int classOrder = classInfo.compareTo(correspondClassInfo);
    //        return 0 != classOrder ? classOrder : super.compareTo(fieldInfo);
    //    }
    /**
     * ���̃t�B�[���h���`���Ă���N���X��Ԃ�
     * 
     * @return ���̃t�B�[���h���`���Ă���N���X
     */
    public final ClassInfo getOwnerClass() {
        return this.ownerClass;
    }

    /**
     * ���̃t�B�[���h���Q�Ƃ��Ă��郁�\�b�h�܂��̓R���X�g���N�^�� SortedSet ��Ԃ��D
     * 
     * @return ���̃t�B�[���h���Q�Ƃ��Ă��郁�\�b�h�܂��̓R���X�g���N�^�� SortedSet
     */
    public final SortedSet<CallableUnitInfo> getReferences() {
        return Collections.unmodifiableSortedSet(this.referencers);
    }

    /**
     * ���̃t�B�[���h�ɑ΂��đ�����s���Ă��郁�\�b�h�܂��̓R���X�g���N�^�� SortedSet ��Ԃ��D
     * 
     * @return ���̃t�B�[���h�ɑ΂��đ�����s���Ă��郁�\�b�h�܂��̓R���X�g���N�^�� SortedSet
     */
    public final SortedSet<CallableUnitInfo> getAssignmenters() {
        return Collections.unmodifiableSortedSet(this.assignmenters);
    }

    /**
     * �q�N���X����Q�Ɖ\���ǂ�����Ԃ�
     * 
     * @return �q�N���X����Q�Ɖ\�ȏꍇ�� true, �����łȂ��ꍇ�� false
     */
    @Override
    public final boolean isInheritanceVisible() {
        final ClassInfo ownerClass = this.getOwnerClass();
        return ownerClass.isInterface() ? true : ModifierInfo.isInheritanceVisible(this
                .getModifiers());
    }

    /**
     * �������O��Ԃ���Q�Ɖ\���ǂ�����Ԃ�
     * 
     * @return �������O��Ԃ���Q�Ɖ\�ȏꍇ�� true, �����łȂ��ꍇ�� false
     */
    @Override
    public final boolean isNamespaceVisible() {
        final ClassInfo ownerClass = this.getOwnerClass();
        return ownerClass.isInterface() ? true : ModifierInfo.isNamespaceVisible(this
                .getModifiers());
    }

    /**
     * �ǂ�����ł��Q�Ɖ\���ǂ�����Ԃ�
     * 
     * @return �ǂ�����ł��Q�Ɖ\�ȏꍇ�� true, �����łȂ��ꍇ�� false
     */
    @Override
    public final boolean isPublicVisible() {
        final ClassInfo ownerClass = this.getOwnerClass();
        return ownerClass.isInterface() ? true : ModifierInfo.isPublicVisible(this.getModifiers());
    }

    /**
     * �C���X�^���X�����o�[���ǂ�����Ԃ�
     * 
     * @return �C���X�^���X�����o�[�̏ꍇ true�C�����łȂ��ꍇ false
     */
    @Override
    public final boolean isInstanceMember() {
        return this.instance;
    }

    /**
     * �X�^�e�B�b�N�����o�[���ǂ�����Ԃ�
     * 
     * @return �X�^�e�B�b�N�����o�[�̏ꍇ true�C�����łȂ��ꍇ false
     */
    public final boolean isStaticMember() {
        return !this.instance;
    }

    /**
     * ���̃t�B�[���h���`���Ă���N���X��ۑ�����ϐ�
     */
    protected final ClassInfo ownerClass;

    /**
     * ���̃t�B�[���h���Q�Ƃ��Ă��郁�\�b�h�Q��ۑ����邽�߂̕ϐ�
     */
    protected final SortedSet<CallableUnitInfo> referencers;

    /**
     * ���̃t�B�[���h�ɑ΂��đ�����s���Ă��郁�\�b�h�Q��ۑ����邽�߂̕ϐ�
     */
    protected final SortedSet<CallableUnitInfo> assignmenters;

    /**
     * �C���X�^���X�����o�[���ǂ�����ۑ����邽�߂̕ϐ�
     */
    private final boolean instance;
}
