package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StaticOrInstance;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.Visualizable;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �������̌Ăяo�����P�ʂ̃u���b�N�i���\�b�h��R���X�g���N�^�j��\���N���X
 * 
 * @author higo
 * @param <T> �����ς݂̌^
 */
public abstract class UnresolvedCallableUnitInfo<T extends CallableUnitInfo> extends
        UnresolvedLocalSpaceInfo<T> implements Visualizable, StaticOrInstance, ModifierSetting {

    protected UnresolvedCallableUnitInfo(final UnresolvedClassInfo ownerClass) {

        super(ownerClass);

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();

        this.modifiers = new HashSet<ModifierInfo>();
        this.typeParameters = new LinkedList<UnresolvedTypeParameterInfo>();
        this.parameters = new LinkedList<UnresolvedParameterInfo>();
        this.thrownExceptions = new LinkedList<UnresolvedClassTypeInfo>();
    }

    protected UnresolvedCallableUnitInfo(final UnresolvedClassInfo ownerClass, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        this(ownerClass);

        this.setFromLine(fromLine);
        this.setFromColumn(fromColumn);
        this.setToLine(toLine);
        this.setToColumn(toColumn);
    }

    /**
     * �R���X�g���N�^�Ɉ�����ǉ�����
     * 
     * @param parameterInfo �ǉ��������
     */
    public final void addParameter(final UnresolvedParameterInfo parameterInfo) {
        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == parameterInfo) {
            throw new NullPointerException();
        }

        this.parameters.add(parameterInfo);
    }

    /**
     * �C���q�� Set ��Ԃ�
     * 
     * @return �C���q�� Set
     */
    public final Set<ModifierInfo> getModifiers() {
        return Collections.unmodifiableSet(this.modifiers);
    }

    /**
     * �C���q��ǉ�����
     * 
     * @param modifier �ǉ�����C���q
     */
    public final void addModifier(final ModifierInfo modifier) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == modifier) {
            throw new NullPointerException();
        }

        this.modifiers.add(modifier);
    }

    /**
     * �R���X�g���N�^�̈����̃��X�g��Ԃ�
     * 
     * @return ���\�b�h�̈����̃��X�g
     */
    public final List<UnresolvedParameterInfo> getParameters() {
        return Collections.unmodifiableList(this.parameters);
    }

    /**
     * �������^�p�����[�^�� List ��Ԃ�
     * 
     * @return �������^�p�����[�^�� List
     */
    public final List<UnresolvedTypeParameterInfo> getTypeParameters() {
        return Collections.unmodifiableList(this.typeParameters);
    }

    /**
     * �������^�p�����[�^��ǉ�����
     * 
     * @param typeParameter �ǉ����関�����^�p�����[�^
     */
    public final void addTypeParameter(final UnresolvedTypeParameterInfo typeParameter) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == typeParameter) {
            throw new NullPointerException();
        }

        this.typeParameters.add(typeParameter);
    }

    /**
     * ��������O�� List ��Ԃ�
     * 
     * @return ��������O�� List
    */
    public final List<UnresolvedClassTypeInfo> getThrownExceptions() {
        return Collections.unmodifiableList(this.thrownExceptions);
    }

    /**
      * ��������O��ǉ�����
      * 
      * @param typeParameter �ǉ����関������O
      */
    public final void addTypeParameter(final UnresolvedClassTypeInfo thrownException) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == thrownException) {
            throw new NullPointerException();
        }

        this.thrownExceptions.add(thrownException);
    }

    /**
     * ���̃��\�b�h���`���Ă���N���X��Ԃ�
     * 
     * @return ���̃��\�b�h���`���Ă���N���X
     */
    public final UnresolvedClassInfo getOwnerClass() {
        return this.getOuterClass();
    }

    /**
     * �q�N���X����Q�Ɖ\���ǂ�����Ԃ�
     * 
     * @return �q�N���X����Q�Ɖ\�ȏꍇ�� true, �����łȂ��ꍇ�� false
     */
    @Override
    public final boolean isInheritanceVisible() {
        final UnresolvedClassInfo ownerClass = this.getOwnerClass();
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
        final UnresolvedClassInfo ownerClass = this.getOwnerClass();
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
        final UnresolvedClassInfo ownerClass = this.getOwnerClass();
        return ownerClass.isInterface() ? true : ModifierInfo.isPublicVisible(this.getModifiers());
    }

    /**
     * �C���X�^���X�����o�[���ǂ�����Ԃ�
     * 
     * @return �C���X�^���X�����o�[�Ȃ̂� true ��Ԃ�
     */
    @Override
    public boolean isInstanceMember() {
        final UnresolvedClassInfo ownerClass = this.getOwnerClass();
        return ownerClass.isInterface() ? true : ModifierInfo.isInstanceMember(this.getModifiers());
    }

    /**
     * �X�^�e�B�b�N�����o�[���ǂ�����Ԃ�
     * 
     * @return �X�^�e�B�b�N�����o�[�ł͂Ȃ��̂� false ��Ԃ�
     */
    @Override
    public boolean isStaticMember() {
        final UnresolvedClassInfo ownerClass = this.getOwnerClass();
        return ownerClass.isInterface() ? false : ModifierInfo.isStaticMember(this.getModifiers());
    }

    /**
     * �������^�p�����[�^����������
     * ���ł�resolve���\�b�h���Ăяo���ꂽ��Ԃŗp���Ȃ���΂Ȃ�Ȃ�
     * 
     * @param classInfoManager
     * @return
     */
    public final T resolveTypeParameter(final ClassInfoManager classInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfoManager) {
            throw new IllegalArgumentException();
        }

        final T resolved = this.getResolved();
        final TargetClassInfo ownerClass = this.getOwnerClass().getResolved();

        for (final UnresolvedTypeParameterInfo unresolvedTypeParameter : this.getTypeParameters()) {

            final TypeParameterInfo typeParameter = unresolvedTypeParameter.getResolved();
            for (final UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo> unresolvedExtendsType : unresolvedTypeParameter
                    .getExtendsTypes()) {
                final ReferenceTypeInfo extendsType = unresolvedExtendsType.resolve(ownerClass,
                        resolved, classInfoManager, null, null);
                typeParameter.addExtendsType(extendsType);
            }
        }

        return resolved;
    }

    /**
     * �������^�p�����[�^����������
     * ���ł�resolve���\�b�h���Ăяo���ꂽ��Ԃŗp���Ȃ���΂Ȃ�Ȃ�
     * 
     * @param classInfoManager
     * @return
     */
    public final T resolveParameter(final ClassInfoManager classInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfoManager) {
            throw new IllegalArgumentException();
        }

        final T resolved = this.getResolved();
        final TargetClassInfo ownerClass = this.getOwnerClass().getResolved();

        for (final UnresolvedParameterInfo unresolvedParameter : this.getParameters()) {
            final TargetParameterInfo parameter = unresolvedParameter.resolve(ownerClass, resolved,
                    classInfoManager, null, null);
            resolved.addParameter(parameter);
        }

        return resolved;
    }

    /**
     * �������̃X���[������O������������
     * ���ł�resolve���\�b�h���Ăяo���ꂽ��Ԃŗp���Ȃ���΂Ȃ�Ȃ�
     * 
     * @param classInfoManager
     * @return
     */
    public final T resolveThrownException(final ClassInfoManager classInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfoManager) {
            throw new IllegalArgumentException();
        }

        final T resolved = this.getResolved();
        final TargetClassInfo ownerClass = this.getOwnerClass().getResolved();

        for (final UnresolvedClassTypeInfo unresolvedThrownException : this.getThrownExceptions()) {
            final ReferenceTypeInfo thrownException = unresolvedThrownException.resolve(ownerClass,
                    resolved, classInfoManager, null, null);
            this.resolvedInfo.addThrownException(thrownException);
        }

        return resolved;
    }

    /**
     * �������^�p�����[�^����ۑ����邽�߂̕ϐ�
     */
    private final List<UnresolvedTypeParameterInfo> typeParameters;

    /**
     * �R���X�g���N�^������ۑ����邽�߂̕ϐ�
     */
    private final List<UnresolvedParameterInfo> parameters;

    /**
     * throw������O��ۑ����邽�߂̕ϐ�
     */
    private final List<UnresolvedClassTypeInfo> thrownExceptions;

    /**
     * �C���q��ۑ�����
     */
    private Set<ModifierInfo> modifiers;

}
