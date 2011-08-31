package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.HavingOuterUnit;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.InnerClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.InstanceInitializerInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StaticInitializerInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StaticOrInstance;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetAnonymousClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetInnerClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.Visualizable;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * AST�p�[�X�Ŏ擾�����N���X�����ꎞ�I�Ɋi�[���邽�߂̃N���X�D �ȉ��̏�������
 * 
 * <ul>
 * <li>�C���q</li>
 * <li>���������O���</li>
 * <li>�^�p�����[�^���ꗗ</li>
 * <li>�N���X��</li>
 * <li>�s��</li>
 * <li>�������e�N���X���ꗗ</li>
 * <li>�������q�N���X���ꗗ</li>
 * <li>�������C���i�[�N���X�ꗗ</li>
 * <li>��������`���\�b�h�ꗗ</li>
 * <li>��������`�t�B�[���h�ꗗ</li>
 * </ul>
 * 
 * @author higo
 * 
 */
public final class UnresolvedClassInfo extends UnresolvedUnitInfo<TargetClassInfo> implements
        Visualizable, StaticOrInstance, ModifierSetting, UnresolvedHavingOuterUnit {

    /**
     * ���̃N���X���L�q����Ă���t�@�C������^���ď�����
     * 
     * @param fileInfo ���̃N���X���L�q���ꂢ�Ă�t�@�C�����
     * @param outerUnit ���̃N���X�̊O���̃��j�b�g
     */
    public UnresolvedClassInfo(final FileInfo fileInfo,
            final UnresolvedUnitInfo<? extends UnitInfo> outerUnit) {

        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == fileInfo) {
            throw new IllegalArgumentException("fileInfo is null");
        }

        this.outerUnit = outerUnit;

        this.fileInfo = fileInfo;
        this.namespace = null;
        this.className = null;

        this.modifiers = new HashSet<ModifierInfo>();
        this.typeParameters = new LinkedList<UnresolvedTypeParameterInfo>();
        this.superClasses = new ArrayList<UnresolvedClassTypeInfo>();
        this.innerClasses = new HashSet<UnresolvedClassInfo>();
        this.definedMethods = new HashSet<UnresolvedMethodInfo>();
        this.definedConstructors = new HashSet<UnresolvedConstructorInfo>();
        this.definedFields = new HashSet<UnresolvedFieldInfo>();
        this.implicitStaticInitializer = new UnresolvedStaticInitializerInfo(this);
        this.implicitInstanceInitializer = new UnresolvedInstanceInitializerInfo(this);
        this.instanceInitializers = new HashSet<UnresolvedInstanceInitializerInfo>();
        this.staticInitializers = new HashSet<UnresolvedStaticInitializerInfo>();
        this.importStatements = new LinkedList<UnresolvedImportStatementInfo<?>>();
   //     this.isInterface = false;

        this.anonymous = false;

        this.resolvedInfo = null;
    }

    /**
     * �C���q��ǉ�����
     * 
     * @param modifier �ǉ�����C���q
     */
    public void addModifier(final ModifierInfo modifier) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == modifier) {
            throw new NullPointerException();
        }

        this.modifiers.add(modifier);
    }

    /**
     * �������^�p�����[�^��ǉ�����
     * 
     * @param type �ǉ����関�����^�p�����[�^
     */
    public void addTypeParameter(final UnresolvedTypeParameterInfo type) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == type) {
            throw new NullPointerException();
        }

        this.typeParameters.add(type);
    }

    /**
     * �������C���X�^���X�C�j�V�����C�U��ǉ�
     * 
     * @param instanceInitializer �������C���X�^���X�C�j�V�����C�U
     */
    public void addInstanceInitializer(final UnresolvedInstanceInitializerInfo instanceInitializer) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == instanceInitializer) {
            throw new NullPointerException("instanceInitializer is null");
        }
        this.instanceInitializers.add(instanceInitializer);
    }

    /**
     * �������X�^�e�B�b�N�C�j�V�����C�U��ǉ�
     * 
     * @param staticInitialzer �������X�^�e�B�b�N�C�j�V�����C�U
     */
    public void addStaticInitializer(final UnresolvedStaticInitializerInfo staticInitialzer) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == staticInitialzer) {
            throw new NullPointerException("staticInitializer is null");
        }
        this.staticInitializers.add(staticInitialzer);
    }

    /**
     * ���̃N���X�ƑΏۃN���X�����������ǂ����𔻒肷��
     * 
     * @param o ��r�ΏۃN���X
     */
    @Override
    public boolean equals(final Object o) {

        if (null == o) {
            throw new NullPointerException();
        }

        if (!(o instanceof UnresolvedClassInfo)) {
            return false;
        }

        final String[] fullQualifiedName = this.getFullQualifiedName();
        final String[] correspondFullQualifiedName = ((UnresolvedClassInfo) o)
                .getFullQualifiedName();

        if (fullQualifiedName.length != correspondFullQualifiedName.length) {
            return false;
        }

        for (int i = 0; i < fullQualifiedName.length; i++) {
            if (!fullQualifiedName[i].equals(correspondFullQualifiedName[i])) {
                return false;
            }
        }

        return true;
    }

    /**
     * ���̃N���X�̃n�b�V���R�[�h��Ԃ�
     * 
     * @return ���̃N���X�̃n�b�V���R�[�h
     */
    @Override
    public int hashCode() {

        final StringBuffer buffer = new StringBuffer();
        final String[] fullQualifiedName = this.getFullQualifiedName();
        for (int i = 0; i < fullQualifiedName.length; i++) {
            buffer.append(fullQualifiedName[i]);
        }

        return buffer.toString().hashCode();
    }

    /**
     * ���̃N���X���L�q����Ă���t�@�C������Ԃ�
     * 
     * @return ���̃N���X���L�q����Ă���t�@�C�����
     */
    public FileInfo getFileInfo() {
        return this.fileInfo;
    }

    /**
     * ���O��Ԗ���Ԃ�
     * 
     * @return ���O��Ԗ�
     */
    public String[] getNamespace() {
        return Arrays.<String> copyOf(this.namespace, this.namespace.length);
    }

    /**
     * �N���X�����擾����
     * 
     * @return �N���X��
     */
    public String getClassName() {
        return this.className;
    }

    /**
     * ���̃N���X�̊��S�C������Ԃ�
     * 
     * @return ���̃N���X�̊��S�C����
     */
    public String[] getFullQualifiedName() {

        final String[] namespace = this.getNamespace();
        final String[] fullQualifiedName = new String[namespace.length + 1];

        for (int i = 0; i < namespace.length; i++) {
            fullQualifiedName[i] = namespace[i];
        }
        fullQualifiedName[fullQualifiedName.length - 1] = this.getClassName();

        return fullQualifiedName;
    }

    public String getFullQualifiedName(final String delimiter) {

        final StringBuilder text = new StringBuilder();
        for (final String name : this.getFullQualifiedName()) {
            text.append(name);
            text.append(delimiter);
        }
        text.delete(text.length() - delimiter.length(), text.length());
        return text.toString();
    }

    /**
     * �C���q�� Set ��Ԃ�
     * 
     * @return �C���q�� Set
     */
    public Set<ModifierInfo> getModifiers() {
        return Collections.unmodifiableSet(this.modifiers);
    }

    /**
     * �������^�p�����[�^�� List ��Ԃ�
     * 
     * @return �������^�p�����[�^�� List
     */
    public List<UnresolvedTypeParameterInfo> getTypeParameters() {
        return Collections.unmodifiableList(this.typeParameters);
    }

    /**
     * ���O��Ԗ���ۑ�����.���O��Ԗ����Ȃ��ꍇ�͒���0�̔z���^���邱�ƁD
     * 
     * @param namespace ���O��Ԗ�
     */
    public void setNamespace(final String[] namespace) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == namespace) {
            throw new NullPointerException();
        }

        this.namespace = Arrays.<String> copyOf(namespace, namespace.length);
    }

    /**
     * �N���X����ۑ�����
     * 
     * @param className
     */
    public void setClassName(final String className) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == className) {
            throw new NullPointerException();
        }

        this.className = className;
    }

    /**
     * �e�N���X��ǉ�����
     * 
     * @param superClass �e�N���X��
     */
    public void addSuperClass(final UnresolvedClassTypeInfo superClass) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == superClass) {
            throw new NullPointerException();
        }

        this.superClasses.add(superClass);
    }

    /**
     * �C���i�[�N���X��ǉ�����
     * 
     * @param innerClass �C���i�[�N���X
     */
    public void addInnerClass(final UnresolvedClassInfo innerClass) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == innerClass) {
            throw new NullPointerException();
        }

        this.innerClasses.add(innerClass);
    }

    /**
     * ��`���Ă��郁�\�b�h��ǉ�����
     * 
     * @param definedMethod ��`���Ă��郁�\�b�h
     */
    public void addDefinedMethod(final UnresolvedMethodInfo definedMethod) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == definedMethod) {
            throw new NullPointerException();
        }

        this.definedMethods.add(definedMethod);
    }

    /**
     * ��`���Ă���R���X�g���N�^��ǉ�����
     * 
     * @param definedConstructor ��`���Ă���R���X�g���N�^���\�b�h
     */
    public void addDefinedConstructor(final UnresolvedConstructorInfo definedConstructor) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == definedConstructor) {
            throw new NullPointerException();
        }

        this.definedConstructors.add(definedConstructor);
    }

    /**
     * ��`���Ă���t�B�[���h��ǉ�����
     * 
     * @param definedField ��`���Ă���t�B�[���h
     */
    public void addDefinedField(final UnresolvedFieldInfo definedField) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == definedField) {
            throw new NullPointerException();
        }

        this.definedFields.add(definedField);
    }

    /**
     * ���̃N���X�ɂ����ė��p�\�ȁi�C���|�[�g����Ă���j�N���X��ǉ�����
     * 
     * @param importStatement ���̃N���X�ɂ����ė��p�\�ȁi�C���|�[�g����Ă���j�N���X
     */
    public void addImportStatement(final UnresolvedClassImportStatementInfo importStatement) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == importStatement) {
            throw new IllegalArgumentException();
        }

        this.importStatements.add(importStatement);
    }

    /**
     * ���̃N���X�ɂ����ė��p�\�ȁi�C���|�[�g����Ă���j�N���X�Q��ǉ�����
     * 
     * @param importStatements ���̃N���X�ɂ����ė��p�\�ȁi�C���|�[�g����Ă���j�N���X�Q
     */
    public void addImportStatements(final List<UnresolvedImportStatementInfo<?>> importStatements) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == importStatements) {
            throw new IllegalArgumentException();
        }

        this.importStatements.addAll(importStatements);
    }

    /**
     * �e�N���X���̃Z�b�g��Ԃ�
     * 
     * @return �e�N���X���̃Z�b�g
     */
    public List<UnresolvedClassTypeInfo> getSuperClasses() {
        return Collections.unmodifiableList(this.superClasses);
    }

    /**
     * �C���i�[�N���X�̃Z�b�g��Ԃ�
     * 
     * @return �C���i�[�N���X�̃Z�b�g
     */
    public Set<UnresolvedClassInfo> getInnerClasses() {
        return Collections.unmodifiableSet(this.innerClasses);
    }

    /**
     * �O���̏��L�҂�Ԃ�
     * 
     * @return �O���̏��L��. �Ȃ��ꍇ��null
     */
    @Override
    public UnresolvedUnitInfo<? extends UnitInfo> getOuterUnit() {
        return this.outerUnit;
    }

    /**
     * �O���̃��j�b�g���Z�b�g����
     * 
     * @param outerUnit �O���̃��j�b�g
     */
    @Override
    public void setOuterUnit(final UnresolvedUnitInfo<? extends UnitInfo> outerUnit) {
        this.outerUnit = outerUnit;
    }

    /**
     * �O���̃N���X��Ԃ�.
     * 
     * @return�@�O���̃N���X
     */
    @Override
    public final UnresolvedClassInfo getOuterClass() {

        UnresolvedUnitInfo<? extends UnitInfo> outer = this.getOuterUnit();

        while (true) {

            // �C���i�[�N���X�Ȃ̂ł��Ȃ炸�O���̃N���X������
            if (null == outer) {
                throw new IllegalStateException();
            }

            if (outer instanceof UnresolvedClassInfo) {
                return (UnresolvedClassInfo) outer;
            }

            outer = ((UnresolvedHavingOuterUnit) outer).getOuterUnit();
        }
    }

    /**
     * �O���̃��\�b�h��Ԃ�.
     * 
     * @return�@�O���̃��\�b�h
     */
    @Override
    public final UnresolvedCallableUnitInfo<? extends CallableUnitInfo> getOuterCallableUnit() {

        UnresolvedUnitInfo<? extends UnitInfo> outer = this.getOuterUnit();

        while (true) {

            if (null == outer) {
                return null;
            }

            if (outer instanceof UnresolvedCallableUnitInfo<?>) {
                return (UnresolvedCallableUnitInfo<? extends CallableUnitInfo>) outer;
            }

            if (!(outer instanceof HavingOuterUnit)) {
                return null;
            }

            outer = ((UnresolvedHavingOuterUnit) outer).getOuterUnit();
        }
    }

    /**
     * ��`���Ă��郁�\�b�h�̃Z�b�g��Ԃ�
     * 
     * @return ��`���Ă��郁�\�b�h�̃Z�b�g
     */
    public Set<UnresolvedMethodInfo> getDefinedMethods() {
        return Collections.unmodifiableSet(this.definedMethods);
    }

    /**
     * ��`���Ă���R���X�g���N�^�̃Z�b�g��Ԃ�
     * 
     * @return ��`���Ă���R���X�g���N�^�̃Z�b�g
     */
    public Set<UnresolvedConstructorInfo> getDefinedConstructors() {
        return Collections.unmodifiableSet(this.definedConstructors);
    }

    /**
     * ��`���Ă���t�B�[���h�̃Z�b�g
     * 
     * @return ��`���Ă���t�B�[���h�̃Z�b�g
     */
    public Set<UnresolvedFieldInfo> getDefinedFields() {
        return Collections.unmodifiableSet(this.definedFields);
    }

    /**
     * �C���X�^���X�C�j�V�����C�U�̃Z�b�g��Ԃ�
     * @return �C���X�^���X�C�j�V�����C�U�̃Z�b�g
     */
    public final Set<UnresolvedInstanceInitializerInfo> getInstanceInitializers() {
        return Collections.unmodifiableSet(this.instanceInitializers);
    }

    /**
     * �X�^�e�B�b�N�C�j�V�����C�U�̃Z�b�g��Ԃ�
     * @return �X�^�e�B�b�N�C�j�V�����C�U�̃Z�b�g
     */
    public final Set<UnresolvedStaticInitializerInfo> getStaticInitializers() {
        return Collections.unmodifiableSet(this.staticInitializers);
    }

    /**
     * ���p�\�ȃN���X�i�C���|�[�g����Ă���N���X�j�ƃ����o�i�C���|�[�g����Ă��郁���o�j��List��Ԃ�
     * 
     * @return�@���p�\�ȃN���X�i�C���|�[�g����Ă���N���X�j�ƃ����o�i�C���|�[�g����Ă��郁���o�j��List��Ԃ�
     */
    public List<UnresolvedImportStatementInfo<?>> getImportStatements() {
        return Collections.unmodifiableList(this.importStatements);
    }

    /**
     * �C���X�^���X�C�j�V�����C�U��Ԃ�
     * 
     * @return �C���X�����X�C�j�V�����C�U
     */
    public UnresolvedInstanceInitializerInfo getImplicitInstanceInitializer() {
        return this.implicitInstanceInitializer;
    }

    /**
     * �X�^�e�B�b�N�C�j�V�����C�U��Ԃ�
     * 
     * @return �X�^�e�B�b�N�C�j�V�����C�U
     */
    public UnresolvedStaticInitializerInfo getImplicitStaticInitializer() {
        return this.implicitStaticInitializer;
    }

    /**
     * �q�N���X����Q�Ɖ\���ǂ�����Ԃ�
     * 
     * @return �q�N���X����Q�Ɖ\�ȏꍇ�� true, �����łȂ��ꍇ�� false
     */
    @Override
    public boolean isInheritanceVisible() {
        return ModifierInfo.isInheritanceVisible(this.modifiers);
    }

    /**
     * �������O��Ԃ���Q�Ɖ\���ǂ�����Ԃ�
     * 
     * @return �������O��Ԃ���Q�Ɖ\�ȏꍇ�� true, �����łȂ��ꍇ�� false
     */
    @Override
    public boolean isNamespaceVisible() {
        return ModifierInfo.isNamespaceVisible(this.modifiers);
    }

    /**
     * �ǂ�����ł��Q�Ɖ\���ǂ�����Ԃ�
     * 
     * @return �ǂ�����ł��Q�Ɖ\�ȏꍇ�� true, �����łȂ��ꍇ�� false
     */
    public boolean isPublicVisible() {
        return ModifierInfo.isPublicVisible(this.modifiers);
    }

    /**
     * �C���X�^���X�����o�[���ǂ�����Ԃ�
     * 
     * @return �C���X�^���X�����o�[�̏ꍇ true�C�����łȂ��ꍇ false
     */
    @Override
    public boolean isInstanceMember() {
        return ModifierInfo.isInstanceMember(this.getModifiers());
    }

    /**
     * �X�^�e�B�b�N�����o�[���ǂ�����Ԃ�
     * 
     * @return �X�^�e�B�b�N�����o�[�̏ꍇ true�C�����łȂ��ꍇ false
     */
    @Override
    public boolean isStaticMember() {
        return ModifierInfo.isStaticMember(this.getModifiers());
    }

    /**
     * �C���^�[�t�F�[�X���ǂ�����Ԃ�
     * 
     * @return �C���^�[�t�F�[�X�̏ꍇ��true, �����łȂ��ꍇ��false
     */
    public final boolean isInterface() {
        return CLASS_CATEGORY.INTERFACE == this.classCategory;
    }
    
    /**
     * �񋓌^���ǂ�����Ԃ�
     * 
     * @return �C���^�[�t�F�[�X�̏ꍇ��true, �����łȂ��ꍇ��false
     */
    public final boolean isEnum() {
        return CLASS_CATEGORY.ENUM == this.classCategory;
    }

    /**
     * �C���^�[�t�F�[�X�ł���Ƃ��������Z�b�g����D
     * 
     */
    public void setIsInterface() {
        this.classCategory = CLASS_CATEGORY.INTERFACE;
    }
    
    public void setIsEnum(){
        this.classCategory = CLASS_CATEGORY.ENUM;
    }
    

    /**
     * �����N���X���ǂ������Z�b�g����
     * 
     * @param anonymous �����N���X�̏ꍇ�� true�C�����łȂ��ꍇ�� false
     */
    public void setAnonymous(final boolean anonymous) {
        this.anonymous = anonymous;
    }

    /**
     * �����N���X���ǂ�����Ԃ�
     * 
     * @return �����N���X�ł���ꍇ��true, �����łȂ��ꍇ��false
     */
    public boolean isAnonymous() {
        return this.anonymous;
    }

    /**
     * ���̖������N���X������������
     * 
     * @param usingClass �����N���X�C���̃��\�b�h�Ăяo���̍ۂ� null ���Z�b�g����Ă���Ǝv����D
     * @param usingMethod �������\�b�h�C���̃��\�b�h�Ăяo���̍ۂ� null ���Z�b�g����Ă���Ǝv����D
     * @param classInfoManager �p����N���X�}�l�[�W��
     * @param fieldInfoManager �p����t�B�[���h�}�l�[�W��
     * @param methodInfoManager �p���郁�\�b�h�}�l�[�W��
     */
    @Override
    public TargetClassInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        // �C���q�C���S���薼�C�s���C�����C�C���X�^���X�����o�[���ǂ������擾
        final Set<ModifierInfo> modifiers = this.getModifiers();
        final String[] fullQualifiedName = this.getFullQualifiedName();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        // ClassInfo �I�u�W�F�N�g���쐬���CClassInfoManager�ɓo�^

        final UnresolvedUnitInfo<? extends UnitInfo> unresolvedOuterUnit = this.getOuterUnit();

        // ���L�҂�����ꍇ�� �C���i�[�N���X�������N���X
        if (null != unresolvedOuterUnit) {

            // �����N���X�̂Ƃ�
            if (this.isAnonymous()) {
                this.resolvedInfo = new TargetAnonymousClassInfo(fullQualifiedName, this.fileInfo,
                        fromLine, fromColumn, toLine, toColumn);
            }

            //�@�C���i�[�N���X�̂Ƃ�
            else {
                this.resolvedInfo = new TargetInnerClassInfo(modifiers, fullQualifiedName,
                        this.isInterface(), this.fileInfo, fromLine, fromColumn, toLine, toColumn);
            }
        }

        // ���L�҂��Ȃ��ꍇ�͍ł��O���̃N���X
        else {

            this.resolvedInfo = new TargetClassInfo(modifiers, fullQualifiedName, this.isInterface(),
                    this.fileInfo, fromLine, fromColumn, toLine, toColumn);
        }

        // �����N���X������
        for (final UnresolvedClassInfo unresolvedInnerClass : this.getInnerClasses()) {
            final TargetClassInfo innerClass = unresolvedInnerClass.resolve(this.resolvedInfo,
                    null, classInfoManager, fieldInfoManager, methodInfoManager);
            this.resolvedInfo.addInnerClass((InnerClassInfo) innerClass);
        }

        // �^�C�v�p�����[�^������ꍇ�͉�������D�����������ł́Cexntends �܂ł͉������Ȃ�
        for (final UnresolvedTypeParameterInfo unresolvedTypeParameter : this.getTypeParameters()) {
            final TypeParameterInfo typeParameter = unresolvedTypeParameter.resolve(
                    this.resolvedInfo, usingMethod, classInfoManager, fieldInfoManager,
                    methodInfoManager);
            this.resolvedInfo.addTypeParameter(typeParameter);
        }

        //�@���̃N���X�Œ�`���Ă��郁�\�b�h������
        for (final UnresolvedMethodInfo unresolvedMethod : this.getDefinedMethods()) {
            final TargetMethodInfo method = unresolvedMethod.resolve(this.resolvedInfo, null,
                    classInfoManager, fieldInfoManager, methodInfoManager);
            this.resolvedInfo.addDefinedMethod(method);
            methodInfoManager.add(method);
        }

        //�@���̃N���X�Œ�`���Ă���R���X�g���N�^������
        for (final UnresolvedConstructorInfo unresolvedConstructor : this.getDefinedConstructors()) {
            final TargetConstructorInfo constructor = unresolvedConstructor.resolve(
                    this.resolvedInfo, null, classInfoManager, fieldInfoManager, methodInfoManager);
            this.resolvedInfo.addDefinedConstructor(constructor);
            methodInfoManager.add(constructor);
        }

        // �R���X�g���N�^���S����`����Ă��Ȃ��ꍇ�̓f�t�H���g�R���X�g���N�^��1�p��
        if (0 == this.getDefinedConstructors().size()) {
            final TargetConstructorInfo constructor = new TargetConstructorInfo(
                    Collections.<ModifierInfo> emptySet(), 0, 0, 0, 0);
            constructor.setOuterUnit(this.resolvedInfo);
            this.resolvedInfo.addDefinedConstructor(constructor);
        }

        //�@���̃N���X�Œ�`���Ă���t�B�[���h������
        for (final UnresolvedFieldInfo unresolvedConstructor : this.getDefinedFields()) {
            final TargetFieldInfo field = unresolvedConstructor.resolve(this.resolvedInfo, null,
                    classInfoManager, fieldInfoManager, methodInfoManager);
            this.resolvedInfo.addDefinedField(field);
            fieldInfoManager.add(field);
        }

        // ���̃N���X�Œ�`����Ă���C���X�^���X�C�j�V�����C�U������
        for (final UnresolvedInstanceInitializerInfo unresolvedInitializer : this
                .getInstanceInitializers()) {
            final InstanceInitializerInfo initializer = unresolvedInitializer.resolve(
                    this.resolvedInfo, null, classInfoManager, fieldInfoManager, methodInfoManager);
            this.resolvedInfo.addInstanceInitializer(initializer);
        }

        // ���̃N���X�Œ�`����Ă���X�^�e�B�b�N�C�j�V�����C�U������
        for (final UnresolvedStaticInitializerInfo unresolvedInitializer : this
                .getStaticInitializers()) {
            final StaticInitializerInfo initializer = unresolvedInitializer.resolve(
                    this.resolvedInfo, null, classInfoManager, fieldInfoManager, methodInfoManager);
            this.resolvedInfo.addStaticInitializer(initializer);
        }

        // ���̃N���X�̃C���|�[�g���������C�o�^
        for (final UnresolvedImportStatementInfo<?> unresolvedImport : this.getImportStatements()) {
            this.resolvedInfo.addImportStatement(unresolvedImport.resolve(usingClass, usingMethod,
                    classInfoManager, fieldInfoManager, methodInfoManager));
        }

        return this.resolvedInfo;
    }

    /**
     * �O���̃��j�b�g����������
     * 
     * @param classInfoManager
     * @return
     */
    public TargetClassInfo resolveOuterUnit(final ClassInfoManager classInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfoManager) {
            throw new IllegalArgumentException();
        }

        final TargetClassInfo resolved = this.getResolved();

        final UnresolvedUnitInfo<? extends UnitInfo> unresolvedOuterUnit = this.getOuterUnit();
        if (null != unresolvedOuterUnit) {
            final UnitInfo outerUnit = unresolvedOuterUnit.resolve(null, null, classInfoManager,
                    null, null);
            ((TargetInnerClassInfo) resolved).setOuterUnit(outerUnit);
        }

        return resolved;
    }

    /**
     * �������X�[�p�[�N���X������������D
     * ���ł�resolve���\�b�h���Ăяo���ꂽ��Ԃŗp���Ȃ���΂Ȃ�Ȃ�
     * 
     * @param classInfoManager
     * @return
     */
    public TargetClassInfo resolveSuperClass(final ClassInfoManager classInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfoManager) {
            throw new IllegalArgumentException();
        }

        final TargetClassInfo resolved = this.getResolved();

        for (final UnresolvedClassTypeInfo unresolvedSuperType : this.getSuperClasses()) {

            // �X�[�p�[�N���X��ݒ�
            final ReferenceTypeInfo superType = unresolvedSuperType.resolveAsSuperType(resolved,
                    null, classInfoManager, null, null);
            resolved.addSuperClass((ClassTypeInfo) superType);

            // �T�u�N���X��ݒ�
            final ClassInfo superClass = ((ClassTypeInfo) superType).getReferencedClass();
            superClass.addSubClass(resolved);
        }

        return resolved;
    }

    /**
     * �������^�p�����[�^������������D
     * ���ł�resolve���\�b�h���Ăяo���ꂽ��Ԃŗp���Ȃ���΂Ȃ�Ȃ�
     * 
     * @param classInfoManager
     * @return
     */
    public TargetClassInfo resolveTypeParameter(final ClassInfoManager classInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfoManager) {
            throw new IllegalArgumentException();
        }

        final TargetClassInfo resolved = this.getResolved();

        for (final UnresolvedTypeParameterInfo unresolvedTypeParameter : this.getTypeParameters()) {

            final TypeParameterInfo typeParameter = unresolvedTypeParameter.getResolved();
            for (final UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo> unresolvedExtendsType : unresolvedTypeParameter
                    .getExtendsTypes()) {
                final ReferenceTypeInfo extendsType = unresolvedExtendsType.resolve(resolved, null,
                        classInfoManager, null, null);
                typeParameter.addExtendsType(extendsType);
            }
        }

        return resolved;
    }

    /**
     * ���̖������N���X��`���̖������Q�ƌ^��Ԃ�
     * 
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I���� 
     * @return ���̖������N���X��`���̖������Q�ƌ^
     */
    public UnresolvedClassReferenceInfo getClassReference(
            final UnresolvedUnitInfo<? extends UnitInfo> outerUnit, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        final UnresolvedClassReferenceInfo classReference = new UnresolvedFullQualifiedNameClassReferenceInfo(
                this);
        classReference.setOuterUnit(outerUnit);
        classReference.setFromLine(fromLine);
        classReference.setFromColumn(fromColumn);
        classReference.setToLine(toLine);
        classReference.setToColumn(toColumn);

        // �^�p�����[�^�̐������C�^������ǉ�
        for (final UnresolvedTypeParameterInfo typeParameter : this.typeParameters) {
            classReference.addTypeArgument(new UnresolvedClassTypeInfo(new String[] { "java",
                    "lang", "Object" }));
        }
        return classReference;
    }

    public UnresolvedClassTypeInfo getClassType() {
        if (null != this.classType) {
            return this.classType;
        }
        final List<UnresolvedClassImportStatementInfo> namespaces = new LinkedList<UnresolvedClassImportStatementInfo>();
        final UnresolvedClassImportStatementInfo namespace = new UnresolvedClassImportStatementInfo(
                this.getFullQualifiedName(), false);
        namespaces.add(namespace);
        this.classType = new UnresolvedClassTypeInfo(namespaces, this.getFullQualifiedName());
        return this.classType;
    }

    @Override
    public String toString() {
        return "class \"" + this.className + "\" in file \"" + this.fileInfo.getName() + "\"";
    }

    /**
     * �N���X���L�q����Ă���t�@�C������ۑ����邽�߂̕ϐ�
     */
    private final FileInfo fileInfo;

    /**
     * ���O��Ԗ���ۑ����邽�߂̕ϐ�
     */
    private String[] namespace;

    /**
     * �N���X����ۑ����邽�߂̕ϐ�
     */
    private String className;

    /**
     * �C���q��ۑ����邽�߂̕ϐ�
     */
    private final Set<ModifierInfo> modifiers;

    /**
     * �^�p�����[�^��ۑ����邽�߂̕ϐ�
     */
    private final List<UnresolvedTypeParameterInfo> typeParameters;

    /**
     * �e�N���X��ۑ����邽�߂̃��X�g
     */
    private final List<UnresolvedClassTypeInfo> superClasses;

    /**
     * �C���i�[�N���X��ۑ����邽�߂̃Z�b�g
     */
    private final Set<UnresolvedClassInfo> innerClasses;

    /**
     * �O���̃��j�b�g��ێ�����ϐ�
     */
    private UnresolvedUnitInfo<? extends UnitInfo> outerUnit;

    /**
     * ��`���Ă��郁�\�b�h��ۑ����邽�߂̃Z�b�g
     */
    private final Set<UnresolvedMethodInfo> definedMethods;

    /**
     * ��`���Ă���R���X�g���N�^��ۑ����邽�߂̃Z�b�g
     */
    private final Set<UnresolvedConstructorInfo> definedConstructors;

    /**
     * ��`���Ă���t�B�[���h��ۑ����邽�߂̃Z�b�g
     */
    private final Set<UnresolvedFieldInfo> definedFields;

    /**
     * �Öق̃X�^�e�B�b�N�C�j�V�����C�U��ۑ����邽�߂̕ϐ�
     */
    private final UnresolvedStaticInitializerInfo implicitStaticInitializer;

    /**
     * �Öق̃C���X�^���X�C�j�V�����C�U��ۑ����邽�߂̕ϐ�
     */
    private final UnresolvedInstanceInitializerInfo implicitInstanceInitializer;

    /**
     * �X�^�e�B�b�N�C�j�V�����C�U�ꗗ��ۑ����邽�߂̕ϐ�
     */
    private final Set<UnresolvedStaticInitializerInfo> staticInitializers;

    /**
     * �C���X�^���X�C�j�V�����C�U�ꗗ��ۑ����邽�߂̕ϐ�
     */
    private final Set<UnresolvedInstanceInitializerInfo> instanceInitializers;

    /**
     * ���p�\�Ȗ��O��Ԃ�ۑ����邽�߂̃Z�b�g
     */
    private final List<UnresolvedImportStatementInfo<?>> importStatements;

    /**
     * �����N���X���ǂ�����\���ϐ�
     */
    private boolean anonymous;

    private UnresolvedClassTypeInfo classType = null;
    
    /**
     * �N���X�̎�ނ��N���X�C�C���^�[�t�F�[�X�C�񋓂̂ǂꂩ��\��
     * @author a-saitoh
     *
     */
    public enum CLASS_CATEGORY{
        CLASS, INTERFACE, ENUM
    }
    
    private CLASS_CATEGORY classCategory = CLASS_CATEGORY.CLASS;

}
