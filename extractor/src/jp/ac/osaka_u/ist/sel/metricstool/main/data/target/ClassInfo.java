package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricMeasurable;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �N���X�����i�[���邽�߂̒��ۃN���X
 * 
 * @author higo
 * 
 */
@SuppressWarnings("serial")
public abstract class ClassInfo extends UnitInfo implements MetricMeasurable, Modifier,
        Visualizable, StaticOrInstance, TypeParameterizable {

    /**
     * InnerClassInfo<?>��SortedSet��ClassInfo<?,?,?,?>��SortedSet�ɕϊ�����
     * @param innerClasses
     * @return
     */
    public static SortedSet<ClassInfo> convert(final SortedSet<InnerClassInfo> innerClasses) {

        if (null == innerClasses) {
            throw new IllegalArgumentException();
        }

        final SortedSet<ClassInfo> classes = new TreeSet<ClassInfo>();

        for (final InnerClassInfo innerClass : innerClasses) {
            classes.add((ClassInfo) innerClass);
        }

        return Collections.unmodifiableSortedSet(classes);
    }

    /**
     * ���O��Ԗ��ƃN���X������I�u�W�F�N�g�𐶐�����
     * 
     * @param modifiers �C���q��Set
     * @param namespace ���O��Ԗ�
     * @param className �N���X��
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    ClassInfo(final Set<ModifierInfo> modifiers, final NamespaceInfo namespace,
            final String className, final boolean isInterface, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == namespace) || (null == className)) {
            throw new NullPointerException();
        }

        this.definedMethods = new TreeSet<MethodInfo>();
        this.definedConstructors = new TreeSet<ConstructorInfo>();
        this.definedFields = new TreeSet<FieldInfo>();
        this.innerClasses = new TreeSet<InnerClassInfo>();

        this.namespace = namespace;
        this.className = className;
        this.superClasses = new LinkedList<ClassTypeInfo>();
        this.subClasses = new TreeSet<ClassInfo>();

        this.typeParameters = new LinkedList<TypeParameterInfo>();

        this.modifiers = new HashSet<ModifierInfo>();
        this.modifiers.addAll(modifiers);

        this.isInterface = isInterface;

    }

    /**
     * ���S���薼����N���X���I�u�W�F�N�g�𐶐�����
     * 
     * @param modifiers �C���q��Set
     * @param fullQualifiedName ���S���薼
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public ClassInfo(final Set<ModifierInfo> modifiers, final String[] fullQualifiedName,
            final boolean isInterface, final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == fullQualifiedName) {
            throw new NullPointerException();
        }
        if (0 == fullQualifiedName.length) {
            throw new IllegalArgumentException("Full Qualified Name must has at least 1 word!");
        }

        this.definedMethods = new TreeSet<MethodInfo>();
        this.definedConstructors = new TreeSet<ConstructorInfo>();
        this.definedFields = new TreeSet<FieldInfo>();
        this.innerClasses = new TreeSet<InnerClassInfo>();

        String[] namespace = new String[fullQualifiedName.length - 1];
        System.arraycopy(fullQualifiedName, 0, namespace, 0, fullQualifiedName.length - 1);
        this.namespace = new NamespaceInfo(namespace);
        this.className = fullQualifiedName[fullQualifiedName.length - 1];
        this.superClasses = new LinkedList<ClassTypeInfo>();
        this.subClasses = new TreeSet<ClassInfo>();

        this.typeParameters = new LinkedList<TypeParameterInfo>();

        this.modifiers = new HashSet<ModifierInfo>();
        this.modifiers.addAll(modifiers);

        this.isInterface = isInterface;

    }

    /**
     * ���̃N���X�̃N���X����Ԃ�
     * 
     * @return �N���X��
     */
    public String getClassName() {
        return this.className;
    }

    /**
     * ���̃N���X�̖��O��Ԗ���Ԃ�
     * 
     * @return ���O��Ԗ�
     */
    public NamespaceInfo getNamespace() {
        return this.namespace;
    }

    /**
     * �N���X�I�u�W�F�N�g�̔�r�̏ꍇ�́C���O��ԂɊ�Â��������ɂ��邽�߂ɒ�`���Ă���D
     */
    @Override
    public final int compareTo(final Position o) {

        if (null == o) {
            throw new IllegalArgumentException();
        }

        if (o instanceof ClassInfo) {

            final NamespaceInfo namespace = this.getNamespace();
            final NamespaceInfo correspondNamespace = ((ClassInfo) o).getNamespace();
            final int namespaceOrder = namespace.compareTo(correspondNamespace);
            if (namespaceOrder != 0) {
                return namespaceOrder;
            }

            final String name = this.getClassName();
            final String correspondName = ((ClassInfo) o).getClassName();
            return name.compareTo(correspondName);

        } else {
            return super.compareTo(o);
        }
    }

    /**
     * ���g���N�X�v���ΏۂƂ��Ă̖��O��Ԃ�
     * 
     * @return ���g���N�X�v���ΏۂƂ��Ă̖��O
     */
    @Override
    public final String getMeasuredUnitName() {
        return this.getFullQualifiedName(Settings.getInstance().getLanguage()
                .getNamespaceDelimiter());
    }

    /**
     * ���̃N���X�̏C���q�� Set ��Ԃ�
     * 
     * @return ���̃N���X�̏C���q�� Set
     */
    @Override
    public final Set<ModifierInfo> getModifiers() {
        return Collections.unmodifiableSet(this.modifiers);
    }

    /**
     * ���̃N���X�ɐe�N���X�i�̌^�j��ǉ�����D�v���O�C������ĂԂƃ����^�C���G���[�D
     * 
     * @param referenceType �ǉ�����e�N���X�̌^
     */
    public void addSuperClass(final ClassTypeInfo referenceType) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == referenceType) {
            throw new NullPointerException();
        }

        this.superClasses.add(referenceType);
    }

    /**
     * ���̃N���X�Ɏq�N���X��ǉ�����D�v���O�C������ĂԂƃ����^�C���G���[�D
     * 
     * @param subClass �ǉ�����q�N���X
     */
    public void addSubClass(final ClassInfo subClass) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == subClass) {
            throw new NullPointerException();
        }

        this.subClasses.add(subClass);
    }

    /**
     * ���̃N���X�̃X�[�p�[�N���X�� SortedSet ��Ԃ��D
     * 
     * @return �X�[�p�[�N���X�� SortedSet
     */
    public List<ClassTypeInfo> getSuperClasses() {
        return Collections.unmodifiableList(this.superClasses);
    }

    /**
     * ���̃N���X�̃T�u�N���X�� SortedSet ��Ԃ��D
     * 
     * @return �T�u�N���X�� SortedSet
     */
    public SortedSet<ClassInfo> getSubClasses() {
        return Collections.unmodifiableSortedSet(this.subClasses);
    }

    /**
     * ���̃N���X�̊��S���薼��Ԃ�
     * 
     * @return ���̃N���X�̊��S���薼
     */
    public final String[] getFullQualifiedName() {

        final String[] namespace = this.getNamespace().getName();
        final String[] fullQualifiedName = new String[namespace.length + 1];
        System.arraycopy(namespace, 0, fullQualifiedName, 0, namespace.length);
        fullQualifiedName[fullQualifiedName.length - 1] = this.getClassName();

        return fullQualifiedName;
    }

    /**
     * ���̃N���X�̊��S���薼��Ԃ��D���S���薼�͈����ŗ^����ꂽ������ɂ��A������C�Ԃ����D
     * 
     * @param delimiter ��؂蕶��
     * @return ���̃N���X�̊��S���薼
     */
    public final String getFullQualifiedName(final String delimiter) {

        if (null == delimiter) {
            throw new NullPointerException();
        }

        StringBuffer buffer = new StringBuffer();
        String[] namespace = this.getNamespace().getName();
        for (int i = 0; i < namespace.length; i++) {
            buffer.append(namespace[i]);
            buffer.append(delimiter);
        }
        buffer.append(this.getClassName());
        return buffer.toString();
    }

    /**
     * �n�b�V���R�[�h��Ԃ�
     * 
     * @return �n�b�V���R�[�h
     */
    @Override
    public final int hashCode() {
        return this.getFullQualifiedName(".").hashCode();
    }

    /**
     * ���������ǂ����̃`�F�b�N
     * 
     * @return �������ꍇ�� true, �������Ȃ��ꍇ�� false
     */
    @Override
    public final boolean equals(final Object o) {

        if (!(o instanceof ClassInfo)) {
            return false;
        }

        if (this == o) {
            return true;
        }

        final NamespaceInfo namespace = this.getNamespace();
        final NamespaceInfo correspondNamespace = ((ClassInfo) o).getNamespace();
        if (!namespace.equals(correspondNamespace)) {
            return false;
        }

        final String className = this.getClassName();
        final String correspondClassName = ((ClassInfo) o).getClassName();
        return className.equals(correspondClassName);
    }

    /**
     * ���̃N���X�������ŗ^����ꂽ�N���X�̐e�N���X�ł��邩�𔻒肷��
     * 
     * @param classInfo �ΏۃN���X
     * @return ���̃N���X�������ŗ^����ꂽ�N���X�̐e�N���X�ł���ꍇ�� true�C�����łȂ��ꍇ�� false
     */
    public final boolean isSuperClass(final ClassInfo classInfo) {

        // �����̒��ڂ̐e�N���X�ɑ΂���
        for (final ClassInfo superClass : ClassTypeInfo.convert(classInfo.getSuperClasses())) {

            // �ΏۃN���X�̒��ڂ̐e�N���X�����̃N���X�Ɠ������ꍇ�� true ��Ԃ�
            if (this.equals(superClass)) {
                return true;
            }

            // �ΏۃN���X�̐e�N���X�ɑ΂��čċA�I�ɏ����Ctrue ���Ԃ��ꂽ�ꍇ�́C���̃��\�b�h�� true ��Ԃ�
            if (this.isSuperClass(superClass)) {
                return true;
            }
        }

        return false;
    }

    /**
     * ���̃N���X�������ŗ^����ꂽ�N���X�̎q�N���X�ł��邩�𔻒肷��
     * 
     * @param classInfo �ΏۃN���X
     * @return ���̃N���X�������ŗ^����ꂽ�N���X�̎q�N���X�ł���ꍇ�� true�C�����łȂ��ꍇ�� false
     */
    public final boolean isSubClass(final ClassInfo classInfo) {

        // �����̒��ڂ̎q�N���X�ɑ΂���
        for (final ClassInfo subClassInfo : classInfo.getSubClasses()) {

            // �ΏۃN���X�̒��ڂ̐e�N���X�����̃N���X�Ɠ������ꍇ�� true ��Ԃ�
            if (this.equals(subClassInfo)) {
                return true;
            }

            // �ΏۃN���X�̐e�N���X�ɑ΂��čċA�I�ɏ����Ctrue ���Ԃ��ꂽ�ꍇ�́C���̃��\�b�h�� true ��Ԃ�
            if (this.isSubClass(subClassInfo)) {
                return true;
            }
        }

        return false;
    }

    /**
     * ���̃N���X�������ŗ^����ꂽ�N���X�̃C���i�[�N���X�ł��邩�𔻒肷��
     * 
     * @param classInfo �ΏۃN���X
     * @return ���̃N���X�������ŗ^����ꂽ�N���X�̃C���i�[�N���X�ł���ꍇ�� true�C�����łȂ��ꍇ�� false
     */
    public final boolean isInnerClass(final ClassInfo classInfo) {

        // ������null�̂Ƃ���false
        if (null == classInfo) {
            return false;
        }

        for (final InnerClassInfo innerClassInfo : classInfo.getInnerClasses()) {

            // ���̃N���X�������̒��ڂ̎q�N���X�Ɠ������ꍇ�� true ��Ԃ�
            if (innerClassInfo.equals(this)) {
                return true;
            }

            // ���̃N���X�������̊ԐړI�Ȏq�N���X�ł���ꍇ�� true ��Ԃ�
            if (this.isInnerClass((ClassInfo) innerClassInfo)) {
                return true;
            }
        }

        // �q�N���X���ċA�I�ɒ��ׂ����ʁC���̃N���X�ƈ�v����N���X��������Ȃ������̂� false ��Ԃ�
        return false;
    }

    public final boolean isPrefixMatch(final String[] prefix) {

        final String[] fqName = this.getFullQualifiedName();
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

    public final boolean isSuffixMatch(final String[] suffix) {

        final String[] fqName = this.getFullQualifiedName();
        for (int index = 0; index < suffix.length; index++) {

            if (fqName.length <= index) {
                return false;
            }

            if (!fqName[fqName.length - index - 1].equals(suffix[suffix.length - index - 1])) {
                return false;
            }
        }

        return true;
    }

    /**
     * �����Ŏw�肳�ꂽ�^�p�����[�^��ǉ�����
     * 
     * @param typeParameter �ǉ�����^�p�����[�^
     */
    @Override
    public final void addTypeParameter(final TypeParameterInfo typeParameter) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == typeParameter) {
            throw new NullPointerException();
        }

        this.typeParameters.add(typeParameter);
    }

    /**
     * �w�肳�ꂽ�C���f�b�N�X�̌^�p�����[�^��Ԃ�
     * 
     * @param index �^�p�����[�^�̃C���f�b�N�X
     * @return�@�w�肳�ꂽ�C���f�b�N�X�̌^�p�����[�^
     */
    @Override
    public final TypeParameterInfo getTypeParameter(final int index) {
        return this.typeParameters.get(index);
    }

    /**
     * ���̃N���X�̌^�p�����[�^�� List ��Ԃ��D
     * 
     * @return ���̃N���X�̌^�p�����[�^�� List
     */
    @Override
    public final List<TypeParameterInfo> getTypeParameters() {
        return Collections.unmodifiableList(this.typeParameters);
    }

    /**
     * �����ŗ^����ꂽ�^�p�����[�^�����̃N���X����ѐe�N���X�Œ�`����Ă��̂ł��邩��Ԃ�
     * 
     * @param typeParameter
     * @return
     */
    public final boolean isDefined(final TypeParameterInfo typeParameter) {

        final List<TypeParameterInfo> typeParameters = this.getTypeParameters();
        if (typeParameters.contains(typeParameter)) {
            return true;
        }

        for (final ClassTypeInfo superClassType : this.getSuperClasses()) {
            final ClassInfo superClass = superClassType.getReferencedClass();
            if (superClass.isDefined(typeParameter)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public TypeParameterizable getOuterTypeParameterizableUnit() {
        return null;
    }

    /**
     * ���̃N���X�ɒ�`���ꂽ���\�b�h����ǉ�����D�v���O�C������ĂԂƃ����^�C���G���[�D
     * 
     * @param definedMethod �ǉ������`���ꂽ���\�b�h
     */
    public final void addDefinedMethod(final MethodInfo definedMethod) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == definedMethod) {
            throw new NullPointerException();
        }

        this.definedMethods.add(definedMethod);
    }

    /**
     * ���̃N���X�ɒ�`���ꂽ�R���X�g���N�^����ǉ�����D�v���O�C������ĂԂƃ����^�C���G���[�D
     * 
     * @param definedConstructor �ǉ������`���ꂽ�R���X�g���N�^
     */
    public final void addDefinedConstructor(final ConstructorInfo definedConstructor) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == definedConstructor) {
            throw new NullPointerException();
        }

        this.definedConstructors.add(definedConstructor);
    }

    /**
     * ���̃N���X�ɒ�`���ꂽ�t�B�[���h����ǉ�����D�v���O�C������ĂԂƃ����^�C���G���[�D
     * 
     * @param definedField �ǉ������`���ꂽ�t�B�[���h
     */
    public final void addDefinedField(final FieldInfo definedField) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == definedField) {
            throw new NullPointerException();
        }

        this.definedFields.add(definedField);
    }

    /**
     * ���̃N���X�ɒ�`����Ă��郁�\�b�h�� SortedSet ��Ԃ��D
     * 
     * @return ��`����Ă��郁�\�b�h�� SortedSet
     */
    public final SortedSet<MethodInfo> getDefinedMethods() {
        return Collections.unmodifiableSortedSet(this.definedMethods);
    }

    /**
     * ���̃N���X�ɒ�`����Ă���R���X�g���N�^�� SortedSet ��Ԃ��D
     * 
     * @return ��`����Ă��郁�\�b�h�� SortedSet
     */
    public final SortedSet<ConstructorInfo> getDefinedConstructors() {
        return Collections.unmodifiableSortedSet(this.definedConstructors);
    }

    /**
     * ���̃N���X�ɒ�`����Ă���t�B�[���h�� SortedSet ��Ԃ��D
     * 
     * @return ��`����Ă���t�B�[���h�� SortedSet
     */
    public final SortedSet<FieldInfo> getDefinedFields() {
        return Collections.unmodifiableSortedSet(this.definedFields);
    }

    /**
     * ���̃N���X�ɃC���i�[�N���X��ǉ�����D�v���O�C������ĂԂƃ����^�C���G���[�D
     * 
     * @param innerClass �ǉ�����C���i�[�N���X
     */
    public final void addInnerClass(final InnerClassInfo innerClass) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == innerClass) {
            throw new NullPointerException();
        }

        this.innerClasses.add(innerClass);
    }

    /**
     * ���̃N���X�̃C���i�[�N���X�� SortedSet ��Ԃ��D
     * 
     * @return �C���i�[�N���X�� SortedSet
     */
    public final SortedSet<InnerClassInfo> getInnerClasses() {
        return Collections.unmodifiableSortedSet(this.innerClasses);
    }

    /**
     * �q�N���X����Q�Ɖ\���ǂ�����Ԃ�
     * 
     * @return �q�N���X����Q�Ɖ\�ȏꍇ�� true, �����łȂ��ꍇ�� false
     */
    public final boolean isInheritanceVisible() {
        return ModifierInfo.isInheritanceVisible(this.modifiers);
    }

    /**
     * �������O��Ԃ���Q�Ɖ\���ǂ�����Ԃ�
     * 
     * @return �������O��Ԃ���Q�Ɖ\�ȏꍇ�� true, �����łȂ��ꍇ�� false
     */
    public final boolean isNamespaceVisible() {
        return ModifierInfo.isNamespaceVisible(this.modifiers);
    }

    /**
     * �ǂ�����ł��Q�Ɖ\���ǂ�����Ԃ�
     * 
     * @return �ǂ�����ł��Q�Ɖ\�ȏꍇ�� true, �����łȂ��ꍇ�� false
     */
    public final boolean isPublicVisible() {
        return ModifierInfo.isPublicVisible(this.modifiers);
    }

    /**
     * �C���X�^���X�����o�[���ǂ�����Ԃ�
     * 
     * @return �C���X�^���X�����o�[�̏ꍇ true�C�����łȂ��ꍇ false
     */
    public final boolean isInstanceMember() {
        return ModifierInfo.isInstanceMember(this.modifiers);
    }

    /**
     * �X�^�e�B�b�N�����o�[���ǂ�����Ԃ�
     * 
     * @return �X�^�e�B�b�N�����o�[�̏ꍇ true�C�����łȂ��ꍇ false
     */
    public final boolean isStaticMember() {
        return ModifierInfo.isStaticMember(this.modifiers);
    }

    /**
     * �C���^�[�t�F�[�X���ǂ����Ԃ��D
     * 
     * @return �C���^�[�t�F�[�X�̏ꍇ true�C�N���X�̏ꍇ false
     */
    public final boolean isInterface() {
        return this.isInterface;
    }

    /**
     * �N���X���ǂ����Ԃ��D
     * 
     *  @return �N���X�̏ꍇ true�C�C���^�[�t�F�[�X�̏ꍇ false
     */
    public final boolean isClass() {
        return !this.isInterface;
    }

    /**
     * �N���X����ۑ����邽�߂̕ϐ�
     */
    private final String className;

    /**
     * ���O��Ԗ���ۑ����邽�߂̕ϐ�
     */
    private final NamespaceInfo namespace;

    /**
     * �C���q��ۑ�����ϐ�
     */
    private final Set<ModifierInfo> modifiers;

    /**
     * ���̃N���X���p�����Ă���N���X�ꗗ��ۑ����邽�߂̕ϐ��D ���ڂ̐e�N���X�݂̂�ۗL���邪�C���d�p�����l���� Set �ɂ��Ă���D
     */
    private final List<ClassTypeInfo> superClasses;

    /**
     * ���̃N���X���p�����Ă���N���X�ꗗ��ۑ����邽�߂̕ϐ��D���ڂ̎q�N���X�݂̂�ۗL����D
     */
    private final SortedSet<ClassInfo> subClasses;

    /**
     * �^�p�����[�^��ۑ�����ϐ�
     */
    private final List<TypeParameterInfo> typeParameters;

    /**
     * ���̃N���X�Œ�`����Ă��郁�\�b�h�ꗗ��ۑ����邽�߂̕ϐ��D
     */
    protected final SortedSet<MethodInfo> definedMethods;

    /**
     * ���̃N���X�Œ�`����Ă���R���X�g���N�^�ꗗ��ۑ����邽�߂̕ϐ��D
     */
    protected final SortedSet<ConstructorInfo> definedConstructors;

    /**
     * ���̃N���X�Œ�`����Ă���t�B�[���h�ꗗ��ۑ����邽�߂̕ϐ��D
     */
    protected final SortedSet<FieldInfo> definedFields;

    /**
     * ���̃N���X�̓����N���X�ꗗ��ۑ����邽�߂̕ϐ��D���ڂ̓����N���X�݂̂�ۗL����D
     */
    private final SortedSet<InnerClassInfo> innerClasses;

    /**
     * �C���^�[�t�F�[�X�ł��邩�ǂ�����ۑ����邽�߂̕ϐ�
     */
    private final boolean isInterface;

}
